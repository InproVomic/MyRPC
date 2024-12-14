package com.cbb.MyRPC.fault.tolerant;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.spi.SpiLoader;

public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    private static TolerantStrategy DEFAULT_TOLERANT_STRATEGY = getInstance(
            RpcApplication.getRpcConfig().getTolerantStrategy());

    public static TolerantStrategy getDefaultTolerantStrategy() {
        return DEFAULT_TOLERANT_STRATEGY;
    }

    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class,key);
    }
}
