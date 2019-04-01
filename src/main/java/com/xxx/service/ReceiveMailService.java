package com.xxx.service;

import com.xxx.pojo.IaEmailPojo;

import java.util.List;

public interface ReceiveMailService {

    List<IaEmailPojo> findBySendEmail(String sendEmail);

    List<IaEmailPojo> findBySendEmailPage(String sendEmail, Integer pageNo, Integer pageSize);

    int insertBatch(List<IaEmailPojo> list);
}
