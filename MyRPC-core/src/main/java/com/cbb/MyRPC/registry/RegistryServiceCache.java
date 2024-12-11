package com.cbb.MyRPC.registry;

import com.cbb.MyRPC.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceCache {

    private final Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写缓存
     */
    public void writeCache(List<ServiceMetaInfo> serviceMetaInfoList, String serviceName) {
        this.serviceCache.put(serviceName, serviceMetaInfoList);
    }

    /**
     * 读缓存
     */
    public List<ServiceMetaInfo> readCache(String serviceName) {
        return this.serviceCache.get(serviceName);
    }

    /**
     * 清除缓存
     */
    public void clearCache(String serviceName) {
        this.serviceCache.remove(serviceName);
    }

    public Map<String, List<ServiceMetaInfo>> getServiceCache() {
        return serviceCache;
    }
}
