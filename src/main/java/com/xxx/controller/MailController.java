package com.xxx.controller;

import com.xxx.mail.Email;
import com.xxx.mail.SendMailService;
import com.xxx.quartz.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {
	
	@Autowired
	private SendMailService sendMailService;
	
	@PostMapping("send")
	public Result send(Email mail) {
		try {
			sendMailService.sendFreemarker(mail);
		} catch (Exception e) {
			e.printStackTrace();
			return  Result.error();
		}
		return  Result.ok();
	}
	
	@PostMapping("list")
	public Result list(Email mail) {
		return sendMailService.listMail(mail);
	}
}