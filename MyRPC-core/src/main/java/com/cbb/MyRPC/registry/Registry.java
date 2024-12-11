package com.cbb.MyRPC.registry;

import com.cbb.MyRPC.config.RegistryConfig;
import com.cbb.MyRPC.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Registry {
    /**
     * 初始化
     */
    void init(RegistryConfig config);

    /**
     * 注册服务
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    /**
     * 注销服务
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;


    public String templateGetLocal();
    /**
     * 服务发现
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartbeat();

    /**
     * 监听服务(消费端)
     */
    void watch(String serviceNodeKey);
}
