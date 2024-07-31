package com.z.example.consumer.common.service;

import com.z.example.consumer.common.model.User;

public interface UserService {
    //获取一个用户的方法
    User getUser(User user);

    default short getNumber(){

        return 1;
    }

}
