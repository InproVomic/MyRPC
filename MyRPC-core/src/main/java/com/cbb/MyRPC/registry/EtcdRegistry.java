package com.cbb.MyRPC.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.cbb.MyRPC.config.RegistryConfig;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry {
    // 正在监听的key集合
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    // 定义一个本地注册节点 key 集合
    private final Map<String,Integer> localRegisterNodeKeySet = new HashMap<>();

    // 注册中心服务缓存
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    private Client client;

    private KV kvClient;

    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig config) {
        client = Client.builder().endpoints(config.getAddress())
                .connectTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartbeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 创建lease和KV客户端
         Lease leaseClient = client.getLeaseClient();
        // 创建一个30秒的租约
         long leaseId = leaseClient.grant(30).get().getID();

        // 设置存储键值对
        String serviceKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(serviceKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(
                serviceMetaInfo),
                StandardCharsets.UTF_8);

        // 将键值对与租约绑定，并设置过期时间
        PutOption option = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, option).get();
        // 添加本地注册节点 key
        localRegisterNodeKeySet.put(serviceKey,0);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(
                ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(),
                StandardCharsets.UTF_8));
        // 删除本地注册节点 key
        localRegisterNodeKeySet.remove(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey());
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        List<ServiceMetaInfo> serviceMetaInfos = registryServiceCache.readCache(serviceKey);
        if(serviceMetaInfos != null){
            return serviceMetaInfos;
        }
        String searchKey = ETCD_ROOT_PATH + serviceKey + "/";
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            // 前缀查询
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchKey,
                    StandardCharsets.UTF_8), getOption).
                    get().getKvs();

            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaList = keyValues.stream().map(
                    keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        watch(keyValue.getKey().toString(StandardCharsets.UTF_8));
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
            // 更新缓存
            registryServiceCache.writeCache(serviceMetaList, serviceKey);
            return serviceMetaList;
        } catch (ExecutionException  | InterruptedException e) {
            throw new RuntimeException("获取列表失败",e);
        }
    }

    @Override
    public void templateDelete() {
        kvClient.delete(ByteSequence.from("/rpc/com.cbb.example.common.service.UserService:1.0/localhost:8091",
                StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");

        // 遍历本地注册节点 key，删除注册中心中的节点
        for (String key : localRegisterNodeKeySet.keySet()){
            try {
                // 后面的get()是重点，表示同步操作，如果不加是异步操作！当jvm退出后不会正常删除！会出问题！
                kvClient.delete(ByteSequence.from(key,
                        StandardCharsets.UTF_8)).get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartbeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (Map.Entry<String,Integer> entry : localRegisterNodeKeySet.entrySet()) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(entry.getKey(),
                                StandardCharsets.UTF_8)).get().getKvs();
                        // 检测在注册中心中是否存在节点
                        if (CollUtil.isEmpty(keyValues)) {
                            entry.setValue(entry.getValue() + 1);
                            // 如果超过30次心跳检测都失败，则认为该节点下线，删除本地注册节点 key
                            if (entry.getValue() > 3) {
                                System.out.println("当前节点下线");
                                localRegisterNodeKeySet.remove(entry.getKey());
                            }
                            continue;
                        }
                        // 节点未过期，重新注册节点
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    }catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        // 支持秒级定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();

        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if(newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()){
                        case DELETE:
                            registryServiceCache.clearCache(serviceNodeKey);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
