package com.z.example.consumer;

import com.z.example.consumer.common.model.User;
import com.z.example.consumer.common.service.UserService;
import com.z.rpc.proxy.ServiceProxyFactory;

/**
 * 简易消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        //todo 获取UserService的实现类对象
        UserService userService= ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("zyh");
        //调用
        User newUser = userService.getUser(user);
        if(newUser!=null) {
            System.out.println(newUser.getName());
        }else {
            System.out.println("没有找到用户");
        }
    }
}
