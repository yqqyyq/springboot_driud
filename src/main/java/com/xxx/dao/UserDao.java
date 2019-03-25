package com.xxx.dao;

import com.xxx.pojo.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    
    User selectByUserId(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}