package com.z.example.provider;

import com.z.example.common.service.UserService;
import com.z.rpc.RpcApplication;
import com.z.rpc.bootstrap.ProviderBootstrap;
import com.z.rpc.config.RegistryConfig;
import com.z.rpc.config.RpcConfig;
import com.z.rpc.model.ServiceMetaInfo;
import com.z.rpc.model.ServiceRegisterInfo;
import com.z.rpc.registry.LocalRegistry;
import com.z.rpc.registry.Registry;
import com.z.rpc.registry.RegistryFactory;
import com.z.rpc.server.VertxHttpServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProviderExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("ProviderExample");
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> userServiceServiceRegisterInfo = new ServiceRegisterInfo<>();
        userServiceServiceRegisterInfo.setServiceName(UserService.class.getName());
        userServiceServiceRegisterInfo.setImplClass(UserServiceImpl.class);
        serviceRegisterInfoList.add(userServiceServiceRegisterInfo);

        //服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
