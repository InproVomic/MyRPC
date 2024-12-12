package com.cbb.MyRPC.fault.retry;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.spi.SpiLoader;

public class RetryStrategyFactory {
    static{
        SpiLoader.load(RetryStrategy.class);
    }

    // 默认策略
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = getInstance(
            RpcApplication.getRpcConfig().getRetryStrategy());

    public static RetryStrategy getDefaultRetryStrategy(){
        return DEFAULT_RETRY_STRATEGY;
    }

    // 获取实例
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
