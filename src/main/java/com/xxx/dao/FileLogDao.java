package com.xxx.dao;

import com.xxx.pojo.FileLog;
import java.util.List;

public interface FileLogDao {

    int deleteByPrimaryKey(String filename);

    int insert(FileLog record);

    int insertSelective(FileLog record);

    FileLog selectByPrimaryKey(String filename);

    List<FileLog> selectAll();

    int updateByPrimaryKeySelective(FileLog record);

    int updateByPrimaryKey(FileLog record);
}