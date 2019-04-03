package com.xxx.controller;

import com.yqqyyq.stat.HelloServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 页面跳转
 */
@Controller
@RequestMapping("/")
public class SystemController {

    @Autowired
    private HelloServiceConfiguration helloService ;


    @RequestMapping(value = "/")
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/main")
    public String main() {
        return "main";
    }

    @RequestMapping("{url}.shtml")
    public String page(@PathVariable("url") String url) {
        return url;
    }

    @RequestMapping("{module}/{url}.shtml")
    public String page(@PathVariable("module") String module, @PathVariable("url") String url) {
        return module + "/" + url;
    }

    @RequestMapping(value = "/com/yqqyyq/name")
    @ResponseBody
    public String yqname() {
        return helloService.getName();
    }

    @RequestMapping(value = "/com/yqqyyq/hobby")
    @ResponseBody
    public String yqhobby() {
        return helloService.getHobby();
    }

}
