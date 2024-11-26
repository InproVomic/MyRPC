package com.cbb.example.provider;

import com.cbb.MyRPC.RpcApplication;
import com.cbb.MyRPC.registry.LocalRegistry;
import com.cbb.MyRPC.service.HttpServer;
import com.cbb.MyRPC.service.VertxHttpServer;
import com.cbb.example.common.service.UserService;

public class ProviderExample {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
