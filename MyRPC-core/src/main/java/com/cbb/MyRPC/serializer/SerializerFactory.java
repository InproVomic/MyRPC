package com.cbb.MyRPC.serializer;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.spi.SpiLoader;

/**
 * @author cbb
 * 序列化工厂，用于获取序列化器
 */
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }
    /**
     * 默认序列化
     */
    private static final Serializer DEFAULT_SERIALIZER = SpiLoader.getInstance(
            Serializer.class,
            RpcApplication.getRpcConfig().getSerializer());

    public static Serializer getDefaultSerializer() {
        return DEFAULT_SERIALIZER;
    }

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
