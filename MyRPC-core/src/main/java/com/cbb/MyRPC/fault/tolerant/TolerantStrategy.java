package com.cbb.MyRPC.fault.tolerant;

import com.cbb.MyRPC.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {
    /**
     *
     * @param context 上下文,用于传递数据
     * @param e 异常
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
