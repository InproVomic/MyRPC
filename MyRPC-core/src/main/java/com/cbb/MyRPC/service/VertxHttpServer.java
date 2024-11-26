package com.cbb.MyRPC.service;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 监听端口
        server.listen(port,result -> {
            if (result.succeeded()) {
                System.out.println("HTTP server started on port " + result.result().actualPort());
            }else {
                System.err.println("Failed to start HTTP server: " + result.cause().getMessage());
            }
        });
    }
}
