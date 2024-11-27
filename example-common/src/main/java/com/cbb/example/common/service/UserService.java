package com.cbb.example.common.service;

import com.cbb.example.common.model.User;

public interface UserService {
    User getUser(User user);

    default int getNumber() {
        return 1;
    }
}
