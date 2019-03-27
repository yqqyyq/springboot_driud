package com.xxx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转
 */
@Controller
@RequestMapping("/")
public class SystemController {

    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/system")
    public String system() {
        return "system-setting";
    }

    @RequestMapping(value = "/upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping("/chat")
    public ModelAndView getIndex() {
        ModelAndView mv = new ModelAndView("chat");
        return mv;
    }

    @RequestMapping(value = "/task")
    public String task() {
        return "task/job";
    }

    @RequestMapping(value = "/main")
    public String mainhtml() {
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

    @RequestMapping(value = "/newupload")
    public String newupload() {
        return "file/upload";
    }
}
