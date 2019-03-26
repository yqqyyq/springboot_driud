package com.xxx.dao;

import com.xxx.pojo.UserPojo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPojo record);

    int insertSelective(UserPojo record);

    UserPojo selectByPrimaryKey(Integer id);
    
    UserPojo selectByUserId(String userId);

    int updateByPrimaryKeySelective(UserPojo record);

    int updateByPrimaryKey(UserPojo record);
}