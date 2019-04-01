package com.xxx.dao;

import com.xxx.pojo.IaEmailPojo;

import java.util.List;
import java.util.Map;

public interface IaEmailDao {
    int deleteByPrimaryKey(Long id);

    int insert(IaEmailPojo record);

    int insertSelective(IaEmailPojo record);

    IaEmailPojo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IaEmailPojo record);

    int updateByPrimaryKeyWithBLOBs(IaEmailPojo record);

    int updateByPrimaryKey(IaEmailPojo record);

    List<IaEmailPojo> findAll();

    List<IaEmailPojo> findBySendEmail(String sendEmail);

    List<IaEmailPojo> findBySendEmailPage(Map map);

    int insertBatch(List<IaEmailPojo> list);
}