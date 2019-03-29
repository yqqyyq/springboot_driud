package com.xxx.controller;

import com.xxx.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;


	@RequestMapping(value = "/queryToken")
	public void firstdoget(HttpServletRequest req, HttpServletResponse resp){
		tokenService.firstdoget(req,resp);
	}
}
