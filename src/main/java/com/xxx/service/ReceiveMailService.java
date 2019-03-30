package com.xxx.service;

import com.xxx.pojo.IaEmailPojo;
import com.xxx.quartz.Result;

import java.util.List;

public interface ReceiveMailService {

    Result findBySendEmail(String sendEmail);

    Result findBySendEmailPage(String sendEmail, Integer pageNo, Integer pageSize);

    int insertBatch(List<IaEmailPojo> list);
}
