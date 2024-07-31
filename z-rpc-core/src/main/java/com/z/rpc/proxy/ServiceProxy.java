package com.z.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.z.rpc.RpcApplication;
import com.z.rpc.config.RpcConfig;
import com.z.rpc.model.RpcRequest;
import com.z.rpc.model.RpcResponse;
import com.z.rpc.serializer.JdkSerializer;
import com.z.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk动态代理
 */
public class ServiceProxy implements InvocationHandler {


    //consumer每次调用注册的方法时，会先调用Invoke()
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final JdkSerializer serializer = (JdkSerializer) SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerialize());

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //todo 这里地址被硬编码了，需要使用注册中心和服务发现机制
            try(HttpResponse httpResponse= HttpRequest.post("http://localhost:8123")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();

                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
