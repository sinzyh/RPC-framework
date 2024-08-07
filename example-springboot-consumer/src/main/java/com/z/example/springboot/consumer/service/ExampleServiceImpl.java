package com.z.example.springboot.consumer.service;

import com.z.example.common.model.User;
import com.z.example.common.service.UserService;
import com.z.rpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test(){
        User user = new User();
        user.setName("zzzzz");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
