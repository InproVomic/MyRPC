package com.cbb.MyRPC.fault.retry;

import com.cbb.MyRPC.model.RpcResponse;

import java.util.concurrent.Callable;

public interface RetryStrategy {
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
