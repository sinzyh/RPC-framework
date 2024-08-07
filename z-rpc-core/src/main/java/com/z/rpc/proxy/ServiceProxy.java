package com.z.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.z.rpc.RpcApplication;
import com.z.rpc.config.RpcConfig;
import com.z.rpc.constant.RpcConstant;
import com.z.rpc.loadbalancer.LoadBalancer;
import com.z.rpc.loadbalancer.LoadBalancerFactory;
import com.z.rpc.model.RpcRequest;
import com.z.rpc.model.RpcResponse;
import com.z.rpc.model.ServiceMetaInfo;
import com.z.rpc.registry.Registry;
import com.z.rpc.registry.RegistryFactory;
import com.z.rpc.serializer.JdkSerializer;
import com.z.rpc.serializer.Serializer;
import com.z.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * jdk动态代理
 */
public class ServiceProxy implements InvocationHandler {


    //consumer每次调用注册的方法时，会先调用Invoke()
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 这里地址被硬编码了，需要使用注册中心和服务发现机制
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

//            //负载均衡
//            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
//            //将调用方法名（请求路径）作为负载均衡参数
//            HashMap<String, Object> requestParam = new HashMap<>();
//            requestParam.put("methodName",rpcRequest.getMethodName());
//            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParam, serviceMetaInfoList);
            //暂时取第一个
            ServiceMetaInfo selectServiceMetaInfo_zore = serviceMetaInfoList.get(0);


            try(HttpResponse httpResponse= HttpRequest.post(selectServiceMetaInfo_zore.getServiceAddress())
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
