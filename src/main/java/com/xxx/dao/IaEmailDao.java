package com.xxx.dao;

import com.xxx.pojo.IaEmailPojo;
import com.xxx.pojo.OaEmailPojo;

import java.util.List;
import java.util.Map;

public interface IaEmailDao {
    int deleteByPrimaryKey(Long id);

    int insert(IaEmailPojo record);

    int insertSelective(IaEmailPojo record);

    OaEmailPojo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IaEmailPojo record);

    int updateByPrimaryKeyWithBLOBs(IaEmailPojo record);

    int updateByPrimaryKey(IaEmailPojo record);

    List<OaEmailPojo> findAll();

    List<OaEmailPojo> findBySendEmail(String sendEmail);

    List<OaEmailPojo> findBySendEmailPage(Map map);

    int insertBatch(List<IaEmailPojo> list);
}