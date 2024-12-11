package com.cbb.example.provider;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.config.RegistryConfig;
import com.cbb.MyRPC.config.RpcConfig;
import com.cbb.MyRPC.model.ServiceMetaInfo;
import com.cbb.MyRPC.registry.LocalRegistry;
import com.cbb.MyRPC.registry.Registry;
import com.cbb.MyRPC.registry.RegistryFactory;
import com.cbb.MyRPC.server.tcp.VertxTcpServer;
import com.cbb.example.common.service.UserService;

public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 启动 web 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
