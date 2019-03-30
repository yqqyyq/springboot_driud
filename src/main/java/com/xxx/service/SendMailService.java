package com.xxx.service;

import com.xxx.mail.Email;
import com.xxx.quartz.Result;

public interface SendMailService {

    void sendSimpleMail(Email mail);

    void sendHtmlMail(Email mail);

    void sendAttachmentsMail(Email mail);

    void sendInlinResourceMail(Email mail);

    void sendFreemarker(Email mail);

    Result listMail(Email mail);

    Result findByReceiveEmail(String receiveEmail);

    Result findByReceiveEmailPage(String receiveEmail,Integer pageNo,Integer pageSize);
}
