package com.cbb.MyRPC.fault.tolerant;

import com.cbb.MyRPC.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// 容错策略：静默处理
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("容错处理：静默处理异常", e);
        return new RpcResponse();
    }
}
