package com.cbb.example.provider;

import com.cbb.example.common.model.User;
import com.cbb.example.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
