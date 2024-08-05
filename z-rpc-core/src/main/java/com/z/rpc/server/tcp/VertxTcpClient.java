package com.z.rpc.server.tcp;

import com.z.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

public class VertxTcpClient {

    public void start() {
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8088, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("Connected to TCP server!!!");

                NetSocket socket = res.result();
                socket.write("Hello, Server!");
                socket.handler(buffer -> {
                    System.out.println("Received data from server: " + buffer.toString());
                });
            } else {
                System.out.println("Failed to connect to TCP server: " + res.cause().getMessage());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}

