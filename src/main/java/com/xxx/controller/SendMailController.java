package com.xxx.controller;

import com.xxx.quartz.Result;
import com.xxx.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail/send/")
public class SendMailController {

    @Autowired
    private SendMailService sendMailService;

    @PostMapping("/list")
    public Result find(String receiveEmail, Integer pageNo, Integer pageSize) {
        return sendMailService.findByReceiveEmail(receiveEmail);
    }

    @PostMapping("/listpage")
    public Result findpage(String receiveEmail, Integer pageNo, Integer pageSize) {
        return sendMailService.findByReceiveEmailPage(receiveEmail, pageNo, pageSize);
    }

    /*@PostMapping("/sendmail")
    public Result send(Email mail) {
        try {
            sendMailService.sendFreemarker(mail);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }*/
}
