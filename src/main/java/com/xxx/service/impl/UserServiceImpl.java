package com.xxx.service.impl;

import com.xxx.dao.UserDao;
import com.xxx.pojo.UserPojo;
import com.xxx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserPojo getUserById(String userId) {
        return userDao.selectByUserId(userId);
    }

    public int updateUser(UserPojo user) {
        int flag = userDao.updateByPrimaryKeySelective(user);
        return flag;
    }
}
