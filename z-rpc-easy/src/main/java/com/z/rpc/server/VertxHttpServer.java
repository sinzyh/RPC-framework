package com.z.rpc.server;

import io.vertx.core.Vertx;

/**
 * 编写基于 Vertx实现的web服务器，能够监听制定端口并处理请求
 */
public class VertxHttpServer implements HttpServer{

    public void doStart(int prot) {

        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

//        server.requestHandler(request ->{
//            System.out.println("Received request:"+request.method()+"+request.uri()");
//            request.response()
//                    .putHeader("content-type","text/plain")
//                    .end("Hello from Vert.x HttP Server!");
//        });
        //监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        //启动Http服务器并进行监听
        server.listen(prot,result -> {

            if (result.succeeded()) {

                System.out.println("HTTP server started on port "+prot);
            }else{

                System.out.println("HTTP server failed to start on port "+result.cause());
            }
        });

    }
}
