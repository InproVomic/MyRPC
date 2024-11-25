package com.cbb.example.provider;
import com.cbb.MyRPC.registry.LocalRegistry;
import com.cbb.MyRPC.service.VertxHttpServer;
import com.cbb.example.common.service.UserService;

public class EasyProviderExample {
    public static void main(String[] args) {
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8091);
    }
}
