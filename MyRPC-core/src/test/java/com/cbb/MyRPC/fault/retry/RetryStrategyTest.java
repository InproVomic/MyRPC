package com.cbb.MyRPC.fault.retry;

import com.cbb.MyRPC.model.RpcResponse;

public class RetryStrategyTest {
    static RetryStrategy strategy = new FixedIntervalRetryStrategy();

    public static void main(String[] args) {
        try{
            RpcResponse response = strategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟测试失败");
            });
        }catch (Exception e){
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
