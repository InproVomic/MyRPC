package com.cbb.MyRPC.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RpcResponse implements Serializable {
    // 响应结果和类型
    private Object data;
    private Class<?> dataType;

    // 响应信息和异常信息
    private String message;
    private String exception;
}
