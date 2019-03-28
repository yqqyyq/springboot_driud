package com.xxx.mail.impl;

import com.xxx.dao.OaEmailDao;
import com.xxx.mail.Email;
import com.xxx.mail.SendMailService;
import com.xxx.pojo.OaEmailPojo;
import com.xxx.quartz.Result;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendMailServiceImpl implements SendMailService {

    private static final Logger logger = LoggerFactory.getLogger(SendMailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String whoAmI;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration configuration;//freemarker

    @Autowired
    private OaEmailDao oaEmailDao;

    @Override
    public void sendSimpleMail(Email mail) {
        logger.info("发送邮件：{}", mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setFrom(whoAmI);
            message.setTo(mail.getEmail());
            message.setSubject(mail.getSubject());
            message.setText(mail.getContent());
            mailSender.send(message);

            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendHtmlMail(Email mail) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(whoAmI);
            helper.setTo(mail.getEmail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);
            mailSender.send(message);

            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFreemarker(Email mail) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //这里可以自定义发信名称
            helper.setFrom(whoAmI, "yqqyyq");
            helper.setTo(mail.getEmail());
            helper.setSubject(mail.getSubject());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("mail", mail);
            //model.put("path", PATH);
            Template template = configuration.getTemplate(mail.getTemplate());
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(
                    template, model);
            helper.setText(text, true);
            mailSender.send(message);

            mail.setContent(text);
            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAttachmentsMail(Email mail) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(whoAmI);
            helper.setTo(mail.getEmail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);

            FileSystemResource file = new FileSystemResource(new File(mail.getFile()));
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);

            mailSender.send(message);

            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result listMail(Email mail) {
        List<OaEmailPojo> list =  oaEmailDao.findAll();
        return Result.ok(list);
    }

    /*@Override
    public void sendInlinResourceMail(String to, String subject, String content,
                                      String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(whoAmI);
            helper.setSubject(subject);
            helper.setText(content, true);

            //可以添加多个图片
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/

}