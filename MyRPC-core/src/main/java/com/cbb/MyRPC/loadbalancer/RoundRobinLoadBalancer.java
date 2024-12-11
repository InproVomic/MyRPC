package com.cbb.MyRPC.loadbalancer;

import com.cbb.MyRPC.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// 轮询负载均衡算法
public class RoundRobinLoadBalancer implements LoadBalancer {
    AtomicInteger counter = new AtomicInteger(0);
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceList) {
        int size = serviceList.size();
        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return serviceList.get(0);
        }
        int index = counter.getAndIncrement() % size;
        return serviceList.get(index);
    }
}
