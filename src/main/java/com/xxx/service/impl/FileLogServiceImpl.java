package com.xxx.service.impl;

import com.xxx.dao.FileLogDao;
import com.xxx.pojo.FileLog;
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
    public int insert(FileLog record) {
        return fileLogDao.insert(record);
    }

    @Override
    public List<FileLog> selectAll() {
        return fileLogDao.selectAll();
    }
}
