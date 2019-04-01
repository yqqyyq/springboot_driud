package com.xxx.service;

import com.xxx.mail.Email;
import com.xxx.pojo.OaEmailPojo;
import com.xxx.quartz.Result;

import java.util.List;

public interface SendMailService {

    void sendSimpleMail(Email mail);

    void sendHtmlMail(Email mail);

    void sendAttachmentsMail(Email mail);

    void sendInlinResourceMail(Email mail);

    void sendFreemarker(Email mail);

    Result listMail(Email mail);

    List<OaEmailPojo> findByReceiveEmail(String receiveEmail);

    List<OaEmailPojo> findByReceiveEmailPage(String receiveEmail,Integer pageNo,Integer pageSize);
}
