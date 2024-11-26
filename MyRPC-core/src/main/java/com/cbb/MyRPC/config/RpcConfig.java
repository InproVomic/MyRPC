package com.cbb.MyRPC.config;

import lombok.Data;

// RPC框架配置
@Data
public class RpcConfig {
    // 名称
    private String name = "MyRPC";
    // 端口号
    private int serverPort = 8091;
    // 版本号
    private String version = "1.0";
    // 服务器主机名
    private String host = "localhost";
    // 模拟调用
    private boolean mock = false;

}
