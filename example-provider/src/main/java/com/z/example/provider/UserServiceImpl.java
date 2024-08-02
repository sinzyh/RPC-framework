package com.z.example.provider;

import com.z.example.common.model.User;
import com.z.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    public User getUser(User user) {
        System.out.println("userServiceImpl getUser 用户名："+user.getName());
        return user;
    }
}
