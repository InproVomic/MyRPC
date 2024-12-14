package com.cbb.examplespringbootprovider;

import com.cbb.MyRPC.springboot.starter.annotation.RpcService;
import com.cbb.example.common.model.User;
import com.cbb.example.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名" + user.getName());
        return user;
    }
}
