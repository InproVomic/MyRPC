package com.cbb.MyRPC.server.tcp;

import com.cbb.MyRPC.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        return "ll".getBytes();
    }

    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();
        // 创建TCP服务器，监听指定端口
        NetServer server = vertx.createNetServer();

        server.connectHandler(new TcpServerHandler());

        server.listen(port,result->{
            if (result.succeeded()) {
                System.out.println("Server started on port: " + result.result().actualPort());
            }
            else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
