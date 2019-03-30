package com.xxx.controller;

import com.xxx.ide.ResultResponse;
import com.xxx.ide.enums.ResultTypeEnum;
import com.xxx.service.JavaComplieService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@CrossOrigin
@Controller
@RequestMapping("/ide/java")
public class JavaComplierController {

    @Resource
    private JavaComplieService javaComplieService;

    /**
     * 执行编译
     */
    @ResponseBody
    @RequestMapping(value = "/complie")
    public ResultResponse complie(String javaSource,
                                  @RequestParam(value = "excuteTimeLimit", required = false) Long excuteTimeLimit,
                                  @RequestParam(value = "excuteArgs", required = false) String excuteArgs) {
        try {
            if (StringUtils.isEmpty(javaSource)) {
                return ResultResponse.Build(ResultTypeEnum.error, "代码不能为空！");
            }
            Class clazz = javaComplieService.complie(javaSource);
            String[] args = getInputArgs(excuteArgs);
            if (null == excuteTimeLimit && null == args) {
                //无参数 无时限
                return javaComplieService.excuteMainMethod(clazz);
            } else if (null == excuteTimeLimit) {
                //有参数 无时限
                return javaComplieService.excuteMainMethod(clazz, args);
            } else if (null == args) {
                //无参数 有时限
                if (excuteTimeLimit <= 0) {
                    return ResultResponse.Build(ResultTypeEnum.error, "限制时间不能小于1毫秒！");
                }
                return javaComplieService.excuteMainMethod(clazz, excuteTimeLimit);
            } else {
                //有参数 有时限
                if (excuteTimeLimit <= 0) {
                    return ResultResponse.Build(ResultTypeEnum.error, "限制时间不能小于1毫秒！");
                }
                return javaComplieService.excuteMainMethod(clazz, excuteTimeLimit, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.Build(ResultTypeEnum.error, "编译出错了！ 错误信息:" + e.getMessage());
        }
    }

    /**
     * 获取运行时程序需要的参数
     */
    private String[] getInputArgs(String excuteArgsStr) {
        if (StringUtils.isEmpty(excuteArgsStr)) {
            return null;
        } else {
            return excuteArgsStr.split(" ");
        }
    }
}
