package com.z.example.provider;

import com.z.example.common.service.UserService;
import com.z.rpc.RpcApplication;
import com.z.rpc.config.RegistryConfig;
import com.z.rpc.config.RpcConfig;
import com.z.rpc.model.ServiceMetaInfo;
import com.z.rpc.registry.LocalRegistry;
import com.z.rpc.registry.Registry;
import com.z.rpc.registry.RegistryFactory;
import com.z.rpc.server.VertxHttpServer;

import java.util.concurrent.ExecutionException;

public class ProviderExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("ProviderExample");
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName,UserServiceImpl.class);
        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceAddress(serviceMetaInfo.getServiceAddress());
        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException("注册服务失败",e);
        }

        //启动web服务
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
