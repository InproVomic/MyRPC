package com.cbb.MyRPC.config;

import com.cbb.MyRPC.fault.retry.RetryStrategy;
import com.cbb.MyRPC.fault.retry.RetryStrategyKeys;
import com.cbb.MyRPC.loadbalancer.LoadBalancerKeys;
import com.cbb.MyRPC.serializer.SerializerKeys;
import lombok.Data;

// RPC框架配置
@Data
public class RpcConfig {
    // 名称
    private String name = "MyRPC";
    // 端口号
    private int serverPort = 8091;
    // 版本号
    private String version = "2.0";
    // 服务器主机名
    private String host = "localhost";
    // 模拟调用
    private boolean mock = false;
    // 序列化器
    private String serializer = SerializerKeys.JDK;
    // 注册中心
    private RegistryConfig registryConfig = new RegistryConfig();
    // 负载均衡器
    private String loadBalancer = LoadBalancerKeys.CONSISTENT_HASH;
    // 重试策略
    private String retryStrategy = RetryStrategyKeys.NO;
}
