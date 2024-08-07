package com.z.example.springboot.provider.service;

import com.z.example.common.model.User;
import com.z.example.common.service.UserService;
import com.z.rpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("=============用户名是："+user.getName());
        return user;
    }
}
