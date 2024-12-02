package com.cbb.MyRPC.model;

import com.cbb.MyRPC.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    // 服务名和方法名
    private String serviceName;
    private String methodName;

    // 服务版本号
    private String version = RpcConstant.DEFAULT_SERVICE_VERSION;

    // 请求参数类型
    private Class<?>[] parameterTypes;

    // 请求参数
    private Object[] args;
}
