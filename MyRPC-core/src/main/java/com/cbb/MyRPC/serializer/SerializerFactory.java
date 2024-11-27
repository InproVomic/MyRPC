package com.cbb.MyRPC.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {
    /**
     * 序列化映射
     */
    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){{
        put(SerializerKeys.JDK,new JdkSerializer());
        put(SerializerKeys.JSON,new JsonSerializer());
        put(SerializerKeys.HESSIAN,new HessianSerializer());
        put(SerializerKeys.KRYO,new KryoSerializer());
    }};

    /**
     * 默认序列化
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getSerializer(String key){
        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }
}
