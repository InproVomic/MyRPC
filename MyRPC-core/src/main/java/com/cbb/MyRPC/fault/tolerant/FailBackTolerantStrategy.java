package com.cbb.MyRPC.fault.tolerant;

import com.cbb.MyRPC.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// 容错策略：服务降级,调用其他服务
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO 调用其他服务
        return null;
    }
}
