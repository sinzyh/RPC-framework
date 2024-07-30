package com.z.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC请求
 * 作用：处理接收到的请求，并根据请求参数找到对应的服务和方法，通过反射调用实现，最后封装返回结果并相应请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    //服务名称
    private String serviceName;


    //方法名称
    private String methodName;

    //参数
    private Class<?>[] parameterTypes;

    //参数值
    private Object[] args;

}
