package com.xxx.quartz.job;

import com.xxx.pojo.FileLogPojo;
import com.xxx.service.FileLogService;
import com.xxx.upload.ConstantByProperties;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@DisallowConcurrentExecution
public class FileLogJob implements Job, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FileLogJob.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private FileLogService fileLogService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //取得job详情
        JobDetail jobDetail = context.getJobDetail();
        //取得job的类
        logger.info("Job Class: " + jobDetail.getJobClass());
        //取得job开始时间
        logger.info("fired at " + context.getFireTime());

        List<FileLogPojo> listsPojo=fileLogService.selectInTime();
        for(FileLogPojo fileLogPojo:listsPojo){
            String filepath=fileLogPojo.getFilepath();
            String fileuser=fileLogPojo.getFileuser();
            String path=ConstantByProperties.basePath+fileuser+"/"+filepath;
            String filename=fileLogPojo.getFilename();
            File file=new File(path);
            if(!file.exists()){
                fileLogService.deleteByPrimaryKey(filename);
                logger.info("[fileLog]" + fileuser + "|" + filename);
            }
        }

        //测试效果 保证上一个任务执行完后，再去执行下一个任务
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
