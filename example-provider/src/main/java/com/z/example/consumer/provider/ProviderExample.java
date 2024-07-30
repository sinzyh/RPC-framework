package com.z.example.consumer.provider;

import com.z.example.consumer.common.service.UserService;
import com.z.rpc.RpcApplication;
import com.z.rpc.registry.LocalRegistry;
import com.z.rpc.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
        System.out.println("ProviderExample");
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //启动web服务
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
