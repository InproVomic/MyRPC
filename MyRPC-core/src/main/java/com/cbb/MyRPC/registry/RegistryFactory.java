package com.cbb.MyRPC.registry;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.spi.SpiLoader;

public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    public static Registry DEFAULT_REGISTRY = SpiLoader.getInstance(
            Registry.class,
            RpcApplication.getRpcConfig().getRegistryConfig().getRegistry());

    public static Registry getDefaultRegistry() {
        return DEFAULT_REGISTRY;
    }

    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class,key);
    }

}
