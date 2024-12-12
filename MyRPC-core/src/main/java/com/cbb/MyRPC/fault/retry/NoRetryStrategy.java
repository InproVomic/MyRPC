package com.cbb.MyRPC.fault.retry;

import com.cbb.MyRPC.model.RpcResponse;

import java.util.concurrent.Callable;

// 默认不重试策略
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
