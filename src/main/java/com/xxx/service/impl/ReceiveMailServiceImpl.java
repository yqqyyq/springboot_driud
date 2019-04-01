package com.xxx.service.impl;

import com.xxx.dao.IaEmailDao;
import com.xxx.pojo.IaEmailPojo;
import com.xxx.pojo.OaEmailPojo;
import com.xxx.quartz.Result;
import com.xxx.service.ReceiveMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiveMailServiceImpl implements ReceiveMailService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveMailServiceImpl.class);

    @Autowired
    private IaEmailDao iaEmailDao;


    @Override
    public List<IaEmailPojo> findBySendEmail(String sendEmail) {
        return iaEmailDao.findBySendEmail(sendEmail);
    }

    @Override
    public List<IaEmailPojo> findBySendEmailPage(String sendEmail, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;

        Integer _start = (pageNo - 1) * pageSize;
        Integer _end = pageNo * pageSize;

        Map map=new HashMap();
        map.put("receiveEmail",sendEmail);
        map.put("_start",_start);
        map.put("_end",_end);

        return iaEmailDao.findBySendEmailPage(map);
    }

    @Override
    public int insertBatch(List<IaEmailPojo> list) {
        return iaEmailDao.insertBatch(list);
    }

}