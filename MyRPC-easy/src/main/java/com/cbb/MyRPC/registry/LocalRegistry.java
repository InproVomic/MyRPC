package com.cbb.MyRPC.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 本地注册中心
public class LocalRegistry {
    //注册信息存储
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    //注册
    public static void register(String key,Class<?> value){
        map.put(key,value);
    }

    // 获取
    public static Class<?> get(String key){
        return map.get(key);
    }

    // 移除
    public static void remove(String key){
        map.remove(key);
    }
}
