package com.xxx.service;

import com.xxx.pojo.FileLogPojo;
import com.xxx.quartz.Result;

import java.util.List;

public interface FileLogService {

    int deleteByPrimaryKey(String filename);

    int insert(FileLogPojo record);

    List<FileLogPojo> selectAll();

    Result selectAll1();

    List<FileLogPojo> selectInTime();

    Long selectByFileLog(FileLogPojo record);
}
