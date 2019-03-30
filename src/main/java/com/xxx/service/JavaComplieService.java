package com.xxx.service;

import com.xxx.ide.ResultResponse;

/**
 * JAVA编译器service接口
 */
public interface JavaComplieService {

    /**
     * 编译，并获取编译后的class类
     */
    Class complie(String javaSource) throws Exception;

    /**
     * 执行MAIN方法
     */
    ResultResponse excuteMainMethod(Class clazz) throws Exception;

    /**
     * 执行MAIN方法
     */
    ResultResponse excuteMainMethod(Class clazz, String[] args) throws Exception;

    /**
     * 执行MAIN方法
     */
    ResultResponse excuteMainMethod(Class clazz, Long timeLimit) throws Exception;

    /**
     * 执行MAIN方法
     */
    ResultResponse excuteMainMethod(Class clazz, Long timeLimit, String[] args) throws Exception;
}
