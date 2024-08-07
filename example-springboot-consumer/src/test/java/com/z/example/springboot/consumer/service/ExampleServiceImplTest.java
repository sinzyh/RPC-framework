package com.z.example.springboot.consumer.service;

import com.z.rpc.springboot.starter.annotation.RpcReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ExampleServiceImplTest {

    @RpcReference
    private ExampleServiceImpl exampleService;

    @Test
    void test1(){
        exampleService.test();
    }
}
