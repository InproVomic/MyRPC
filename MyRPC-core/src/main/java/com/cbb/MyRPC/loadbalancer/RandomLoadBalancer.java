package com.cbb.MyRPC.loadbalancer;

import com.cbb.MyRPC.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

// 随机负载均衡
public class RandomLoadBalancer implements LoadBalancer{
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceList) {
        int size = serviceList.size();
        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return serviceList.get(0);
        }
        return serviceList.get(random.nextInt(size));
    }
}
