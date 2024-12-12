package com.cbb.MyRPC.fault.retry;

public interface RetryStrategyKeys {
    // 不需要重试
    String NO = "no";

    // 固定间隔重试
    String FIXED_INTERVAL = "fixedInterval";
}
