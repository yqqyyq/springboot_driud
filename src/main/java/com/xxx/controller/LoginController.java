package com.xxx.controller;


import com.xxx.pojo.User;
import com.xxx.service.UserService;
import com.xxx.utils.CommonDate;
import com.xxx.utils.WordDefined;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description 用户登录和注销
 */
@Controller
@RequestMapping("/")
public class LoginController {

    private static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 跳转登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String userid, String password, HttpSession session, RedirectAttributes attributes,HttpServletRequest request) {
        logger.info("userid|"+userid);
        User user = userService.getUserById(userid);
        logger.info("user|"+user);
        if(user == null){
            attributes.addFlashAttribute("error",WordDefined.LOGIN_USERID_ERROR);
            return "redirect:/login";
        }else {
            if(!user.getPassword().equals(password)){
                attributes.addFlashAttribute("error", WordDefined.LOGIN_PASSWORD_ERROR);
                return "redirect:/login";
            }else if(user.getStatus() != 1){
                attributes.addFlashAttribute("error", WordDefined.LOGIN_USERID_DISABLED);
                return "redirect:/login";
            } else{
                session.setAttribute("userid", userid);
                session.setAttribute("user", user);
                session.setAttribute("login_status",true);
                user.setLasttime(CommonDate.getTime24());
                userService.updateUser(user);
                attributes.addFlashAttribute("message", WordDefined.LOGIN_SUCCESS);
                return "redirect:/down";
            }
        }
    }

    /**
     * 用户退出
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes attributes){
        session.removeAttribute("userid");
        session.removeAttribute("user");
        session.removeAttribute("login_status");
        attributes.addFlashAttribute("message",WordDefined.LOGOUT_SUCCESS);
        return "redirect:/login";
    }
}
