package com.z.rpc.config;

import lombok.Data;

/**
 * RPC框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */

    private String name = "z-rpc";

    /**
     * 版本号
     */

    private String version = "1.0.0";

    /**
     * 服务器主机名
     */

    private String serverHost = "127.0.0.1";

    /**
     * 服务器端口
     */

    private int serverPort = 8081;
}
