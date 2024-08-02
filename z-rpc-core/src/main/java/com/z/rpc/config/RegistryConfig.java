package com.z.rpc.config;

import com.z.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * RPC框架注册中心
 */
@Data
public class RegistryConfig {

    // 注册中心类别
    private String registry = RegistryKeys.ETCD;
    //注册中心地址
    private String address = "http://127.0.0.1:2380";
    //用户名
    private String username;
    //密码
    private String password;
    //超时时间
    private Long timeout = 10000L;
}
