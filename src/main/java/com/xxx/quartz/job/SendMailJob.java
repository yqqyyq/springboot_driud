package com.xxx.quartz.job;

import com.xxx.mail.Email;
import com.xxx.service.SendMailService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@DisallowConcurrentExecution
public class SendMailJob implements Job, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(SendMailJob.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private SendMailService mailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //取得job详情
        JobDetail jobDetail = context.getJobDetail();
        //取得job的类
        logger.info("Job Class: " + jobDetail.getJobClass());
        //取得job开始时间
        logger.info("fired at " + context.getFireTime());


        Email mail = new Email();
        mail.setReceiveMail(new String[]{"yqqyyq@163.com"
                /*,"865707106@qq.com"
                , "1280239780@qq.com"
                , "dongowned@163.com"
                , "hsdyym123@163.com"
                , "longshun19941229@163.com"*/});
        mail.setSubject("for-mail");
        mail.setContent("<br>多学习!<br>多实践!<br>多反思!<br>");
        mail.setTemplate("yqqyyq.flt");
        mail.setFile(new String[]{"icon.png", "yqqyyq.jpeg"});
        mailService.sendInlinResourceMail(mail);

        //测试效果 保证上一个任务执行完后，再去执行下一个任务
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
