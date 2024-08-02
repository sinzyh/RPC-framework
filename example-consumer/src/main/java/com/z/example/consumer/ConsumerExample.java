package com.z.example.consumer;

import cn.hutool.core.util.RandomUtil;
import com.z.example.common.model.User;
import com.z.example.common.service.UserService;
import com.z.rpc.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) {

//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println(rpc);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("zhangsan"+ RandomUtil.randomString(5));
        //调用
        User result = userService.getUser(user);
        if (result != null) {
            System.out.println(result.getName());

        } else {
            System.out.println("user is null");

        }
//        short number = userService.getNumber();
//
//        System.out.println(number);
    }
}
