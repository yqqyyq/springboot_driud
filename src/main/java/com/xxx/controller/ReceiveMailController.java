package com.xxx.controller;

import com.xxx.pojo.IaEmailPojo;
import com.xxx.quartz.Result;
import com.xxx.service.ReceiveMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mail/receive/")
public class ReceiveMailController {

    @Autowired
    private ReceiveMailService receiveMailService;

    @PostMapping("/list")
    public Result find(String sendEmail, Integer pageNo, Integer pageSize) {
        List<IaEmailPojo> list= receiveMailService.findBySendEmail(sendEmail);
        return Result.ok(list);
    }

    @PostMapping("/listpage")
    public Result findpage(String sendEmail, Integer pageNo, Integer pageSize) {
        List<IaEmailPojo> list= receiveMailService.findBySendEmailPage(sendEmail, pageNo, pageSize);
        return Result.ok(list);
    }
}
