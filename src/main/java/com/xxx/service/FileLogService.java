package com.xxx.service;

import com.xxx.pojo.FileLog;
import java.util.List;

public interface FileLogService {

    int deleteByPrimaryKey(String filename);
    int insert(FileLog record);
    List<FileLog> selectAll();
}
