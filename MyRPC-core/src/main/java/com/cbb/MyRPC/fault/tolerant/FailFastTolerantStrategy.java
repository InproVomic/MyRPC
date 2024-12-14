package com.cbb.MyRPC.fault.tolerant;

import com.cbb.MyRPC.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// 容错策略：失败快速，返回外层
@Slf4j
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
