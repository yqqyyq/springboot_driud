package com.xxx.service;

import com.xxx.mail.Email;
import com.xxx.quartz.Result;

public interface SendMailService {

    void sendSimpleMail(Email mail);

    void sendHtmlMail(Email mail);

    void sendAttachmentsMail(Email mail);

    //void sendInlinResourceMail(String to, String subject, String content, String rscPath, String rscId);

    void sendFreemarker(Email mail);

    Result listMail(Email mail);
}
