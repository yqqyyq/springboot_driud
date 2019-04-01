package com.xxx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.dao.OaEmailDao;
import com.xxx.mail.Email;
import com.xxx.pojo.OaEmailPojo;
import com.xxx.quartz.Result;
import com.xxx.service.SendMailService;
import com.xxx.utils.WordDefined;
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
            message.setTo(mail.getReceiveMail());
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
            helper.setTo(mail.getReceiveMail());
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
            helper.setTo(mail.getReceiveMail());
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
            helper.setTo(mail.getReceiveMail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);

            FileSystemResource file = null;
            String filepath = WordDefined.MAIL_IMG_PATH;
            for (String filename : mail.getFile()) {
                file = new FileSystemResource(new File(filepath + filename));
                String fileName = file.getFilename();
                helper.addAttachment(fileName, file);
            }

            mailSender.send(message);

            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInlinResourceMail(Email mail) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(whoAmI, "yqqyyq");
            helper.setTo(mail.getReceiveMail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);

            Map<String, Object> model = new HashMap<String, Object>();
            model.put("mail", mail);

            Template template = configuration.getTemplate(mail.getTemplate());
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(
                    template, model);
            helper.setText(text, true);

            //可以添加多个图片
            File file = null;
            FileSystemResource resource = null;
            String filepath = WordDefined.MAIL_IMG_PATH;
            String imgId = "";
            for (int i = 0; i < mail.getFile().length; i++) {
                String filename = mail.getFile()[i];
                imgId = "imgId" + (i + 1);
                file = new File(filepath + filename);
                resource = new FileSystemResource(file);
                helper.addInline(imgId, resource);
            }

            mailSender.send(message);

            mail.setContent(text);
            OaEmailPojo oaEmailPojo = new OaEmailPojo(mail);
            oaEmailDao.insert(oaEmailPojo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result listMail(Email mail) {
        List<OaEmailPojo> list = oaEmailDao.findAll();
        return Result.ok(list);
    }

    @Override
    public List<OaEmailPojo> findByReceiveEmail(String receiveEmail) {
        return oaEmailDao.findByReceiveEmail(receiveEmail);
    }

    @Override
    public List<OaEmailPojo> findByReceiveEmailPage(String receiveEmail, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;

        Integer _start = (pageNo - 1) * pageSize;
        Integer _end = pageNo * pageSize;

        //PageHelper.startPage(pageNo, pageSize);

        Map map=new HashMap();
        map.put("receiveEmail",receiveEmail);
        map.put("_start",_start);
        map.put("_end",_end);
        //List<OaEmailPojo> list = oaEmailDao.findBySubject(receiveEmail, _start, _end);
        return oaEmailDao.findByReceiveEmailPage(map);

        //用PageInfo对结果进行包装
        //PageInfo<OaEmailPojo> page = new PageInfo<OaEmailPojo>(list);
        //return Result.ok(list);
    }

}