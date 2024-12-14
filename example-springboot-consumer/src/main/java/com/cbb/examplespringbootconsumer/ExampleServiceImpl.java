package com.cbb.examplespringbootconsumer;

import com.cbb.MyRPC.springboot.starter.annotation.RpcReference;
import com.cbb.example.common.model.User;
import com.cbb.example.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("包皮");
        User result = userService.getUser(user);
        System.out.println(result.getName());
    }
}
