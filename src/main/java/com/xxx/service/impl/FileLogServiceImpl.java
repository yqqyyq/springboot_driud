package com.xxx.service.impl;

import com.xxx.dao.FileLogDao;
import com.xxx.pojo.FileLogPojo;
import com.xxx.service.FileLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileLogServiceImpl implements FileLogService {

    @Autowired
    private FileLogDao fileLogDao;


    @Override
    public int deleteByPrimaryKey(String filename){
        return fileLogDao.deleteByPrimaryKey(filename);
    }

    @Override
    public int insert(FileLogPojo record) {
        return fileLogDao.insert(record);
    }

    @Override
    public List<FileLogPojo> selectAll() {
        return fileLogDao.selectAll();
    }


    public List<FileLogPojo> selectInTime() {
        return fileLogDao.selectInTime();
    }

    public Long selectByFileLog(FileLogPojo record){
        return fileLogDao.selectByFileLog(record);
    }
}
