package com.xxx.service;

import com.xxx.pojo.UserPojo;

public interface UserService {

    UserPojo getUserById(String userid);
    int updateUser(UserPojo user);
}
