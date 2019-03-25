package com.xxx.service.impl;

import com.xxx.dao.UserDao;
import com.xxx.pojo.User;
import com.xxx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(String userId) {
        return userDao.selectByUserId(userId);
    }

    public int updateUser(User user) {
        int flag = userDao.updateByPrimaryKeySelective(user);
        return flag;
    }
}
