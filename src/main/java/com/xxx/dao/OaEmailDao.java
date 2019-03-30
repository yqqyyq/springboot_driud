package com.xxx.dao;

import com.xxx.pojo.OaEmailPojo;

import java.util.List;
import java.util.Map;

public interface OaEmailDao {
    int deleteByPrimaryKey(Long id);

    int insert(OaEmailPojo record);

    int insertSelective(OaEmailPojo record);

    OaEmailPojo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaEmailPojo record);

    int updateByPrimaryKeyWithBLOBs(OaEmailPojo record);

    int updateByPrimaryKey(OaEmailPojo record);

    List<OaEmailPojo> findAll();

    List<OaEmailPojo> findByReceiveEmail(String receiveEmail);

    List<OaEmailPojo> findByReceiveEmailPage(Map map);
}