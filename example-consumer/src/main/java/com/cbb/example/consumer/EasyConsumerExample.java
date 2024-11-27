package com.cbb.example.consumer;

import com.cbb.MyRPC.proxy.ServiceProxyFactory;
import com.cbb.example.common.model.User;
import com.cbb.example.common.service.UserService;

public class EasyConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("阿包");
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println("用户名：" + newUser.getName());
        } else {
            System.out.println("没有找到该用户");
        }
        int number = userService.getNumber();
        System.out.println("number:" + number);
    }
}
