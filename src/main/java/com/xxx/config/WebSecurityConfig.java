package com.xxx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'
        http.headers().frameOptions().disable();
        //403
        http.csrf().disable();
        //because its MIME type ('text/css') is not executable, and strict MIME type checking is enabled.
        http.authorizeRequests()
                .antMatchers("/**").permitAll();
        /*http.csrf().disable();
        //允许所有用户访问"/"和"/home"
        http.authorizeRequests()
                .antMatchers("/", "/home").permitAll();*/

        //其他地址的访问均需验证权限
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                //指定登录页是"/login"
//                .loginPage("/login")
//                .defaultSuccessUrl("/hello")//登录成功后默认跳转到"/hello"
//                .permitAll()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/home")//退出登录后的默认url是"/home"
//                .permitAll();

    }
}
