package com.xxx;

import com.xxx.mail.Email;
import com.xxx.service.SendMailService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.xxx.dao")
//@PropertySource(value = "classpath:application-dev.yml",encoding = "utf-8")
@ServletComponentScan       //描到自己编写的servlet和filter druid监控
public class ChatApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

	@Autowired
	private SendMailService mailService;

	@Override
	public void run(String... args) throws Exception {
		Email mail = new Email();
		mail.setEmail(new String[]{"yqqyyq@163.com"});
								//,"1280239780@qq.com"});
		mail.setSubject("for-mail");
		mail.setContent("<br>多学习!<br>多实践!<br>多反思!<br>");
		mail.setTemplate("yqqyyq.flt");
		mail.setFile(new String[]{"icon_email_prompt.png","yqqyyq.jpeg"});
		mailService.sendInlinResourceMail(mail);
	}

}
