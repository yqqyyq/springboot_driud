package com.xxx.service;

import com.xxx.pojo.User;

public interface UserService {

    User getUserById(String userid);
    int updateUser(User user);
}
