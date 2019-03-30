package com.xxx;

import com.xxx.service.SendMailService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.security.Permission;

@SpringBootApplication
@MapperScan("com.xxx.dao")
//@PropertySource(value = "classpath:application-dev.yml",encoding = "utf-8")
//描到自己编写的servlet和filter druid监控
@ServletComponentScan
public class ChatApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Autowired
    private SendMailService mailService;

    @Override
    public void run(String... args) throws Exception {
        /*Email mail = new Email();
        mail.setEmail(new String[]{"yqqyyq@163.com"
                , "1280239780@qq.com"
                , "dongowned@163.com"
                , "hsdyym123@163.com"
                , "longshun19941229@163.com"});
        mail.setSubject("for-mail");
        mail.setContent("<br>多学习!<br>多实践!<br>多反思!<br>");
        mail.setTemplate("yqqyyq.ftl");
        mail.setFile(new String[]{"icon.png", "yqqyyq.jpeg"});
        mailService.sendInlinResourceMail(mail);*/
        //initPermission();
    }

    /**
     * 配置权限，禁止执行CMD 命令
     */
    private static void initPermission() {
        SecurityManager originalSecurityManager = System.getSecurityManager();
        if (originalSecurityManager == null) {
            // 创建自己的SecurityManager
            SecurityManager sm = new SecurityManager() {
                private void check(Permission perm) {
                    // 禁止exec
                    if (perm instanceof java.io.FilePermission) {
                        String actions = perm.getActions();
                        if (actions != null && actions.contains("execute")) {
                            throw new SecurityException("execute denied!");
                        }
                    }
                    // 禁止设置新的SecurityManager，保护自己
                    if (perm instanceof java.lang.RuntimePermission) {
                        String name = perm.getName();
                        if (name != null && name.contains("setSecurityManager")) {
                            throw new SecurityException("System.setSecurityManager denied!");
                        }
                    }
                }

                @Override
                public void checkPermission(Permission perm) {
                    check(perm);
                }

                @Override
                public void checkPermission(Permission perm, Object context) {
                    check(perm);
                }
            };
            System.setSecurityManager(sm);
        }
    }

}
