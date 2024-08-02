package com.z.example.provider;

import com.z.example.common.service.UserService;
import com.z.rpc.registry.LocalRegistry;
import com.z.rpc.server.VertxHttpServer;

public class EasyProviderExample {

    //main方法
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动web服务
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8123);
    }
}
