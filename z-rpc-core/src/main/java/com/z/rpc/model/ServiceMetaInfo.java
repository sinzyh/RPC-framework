package com.z.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion = "1.0";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    private String serviceAddress;

    /**
     * 服务分组(暂未实现)
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * @return
     */
    public String getServiceKey() {
        //后续扩展分组
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务节点键名
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s:%s:%s", getServiceKey(),serviceHost,servicePort);
    }


    /**
     * 获取完整服务地址
     */
    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost,"http"))
            return String.format("%s:%s", serviceHost, servicePort);
        return String.format("%s:%s", serviceHost, servicePort);
    }
}
