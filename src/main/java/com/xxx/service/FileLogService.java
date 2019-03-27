package com.xxx.service;

import com.xxx.pojo.FileLogPojo;

import java.util.List;

public interface FileLogService {

    int deleteByPrimaryKey(String filename);

    int insert(FileLogPojo record);

    List<FileLogPojo> selectAll();

    List<FileLogPojo> selectInTime();

    Long selectByFileLog(FileLogPojo record);
}
