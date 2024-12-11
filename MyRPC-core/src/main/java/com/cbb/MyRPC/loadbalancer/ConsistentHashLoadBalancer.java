package com.cbb.MyRPC.loadbalancer;

import com.cbb.MyRPC.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// 一致性哈希算法的负载均衡算法
public class ConsistentHashLoadBalancer implements LoadBalancer{

    // 一致性哈希环，虚拟节点
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodeMap = new TreeMap<>();

    // 虚拟节点数量
    private static final int VIRTUAL_NODES = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceList) {
        if(serviceList == null || serviceList.isEmpty()){
            return null;
        }

        // 构造虚拟节点环
        for(ServiceMetaInfo serviceMetaInfo : serviceList){
            for(int i = 0;i < VIRTUAL_NODES;i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodeMap.put(hash, serviceMetaInfo);
            }
        }

        // 获取请求参数的哈希值
        int hash = getHash(requestParams);

        Map.Entry<Integer,ServiceMetaInfo> entry = virtualNodeMap.ceilingEntry(hash);

        if(entry == null){
            // 如果不存在，则从第一个节点开始查找
            entry = virtualNodeMap.firstEntry();
        }
        return entry.getValue();
    }

    private int getHash(Object key){
        return key.hashCode();
    }
}
