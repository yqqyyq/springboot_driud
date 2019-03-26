package com.xxx.dao;

import com.xxx.pojo.FileLogPojo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileLogDao {

    int deleteByPrimaryKey(String filename);

    int insert(FileLogPojo record);

    int insertSelective(FileLogPojo record);

    FileLogPojo selectByPrimaryKey(String filename);

    List<FileLogPojo> selectAll();

    int updateByPrimaryKeySelective(FileLogPojo record);

    int updateByPrimaryKey(FileLogPojo record);
}