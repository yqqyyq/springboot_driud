package com.xxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.xxx.dao")
//@PropertySource(value = "classpath:application-dev.yml",encoding = "utf-8")
@ServletComponentScan       //描到自己编写的servlet和filter druid监控
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
