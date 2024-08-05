package com.z.rpc.server.tcp;

import com.z.rpc.server.HttpServer;
import com.z.rpc.server.HttpServerHandler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * 编写基于 Vertx实现的web服务器，能够监听制定端口并处理请求
 */
public class VertxTcpServer {

    private byte[] handlerRequest(byte[] requestData) {
        return "hello world".getBytes();
    }

    public void doStart(int prot) {

        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(socket->{
            //处理连接
            socket.handler(buffer->{
                //处理请求
                byte[] data = buffer.getBytes();
                //处理字节数组，例如：解析请求、调用服务、构造响应等
                byte[] responseData = handlerRequest(data);
                //发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });

        //启动Http服务器并进行监听
        netServer.listen(prot,result -> {

            if (result.succeeded()) {

                System.out.println("TCP server started on port "+prot);
            }else{

                System.out.println("TCP server failed to start on port "+result.cause());
            }
        });

    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8088);
    }
}
