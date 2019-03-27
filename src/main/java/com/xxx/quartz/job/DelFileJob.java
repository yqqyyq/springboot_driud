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

@DisallowConcurrentExecution
public class DelFileJob implements Job, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(DelFileJob.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private FileLogService fileLogService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("DelFileJob is executing.");
        //取得job详情
        JobDetail jobDetail = context.getJobDetail();
        // 取得job名称
        String jobName = jobDetail.getClass().getName();
        logger.info("Name: " + jobDetail.getClass().getSimpleName());
        //取得job的类
        logger.info("Job Class: " + jobDetail.getJobClass());
        //取得job开始时间
        logger.info(jobName + " fired at " + context.getFireTime());
        //取得job下次触发时间
        logger.info("FileLogJob next time." + context.getNextFireTime());

        FileLogPojo fileLogPojo=null;

        File dir =new File(ConstantByProperties.basePath);
        File[] files = dir.listFiles();
        if(files !=null) {
            for (File file : files) {
                String fileuser = file.getName();
                if (file.isDirectory()) { // 判断是文件还是文件夹
                    File dirUser = new File(file.getAbsolutePath());
                    File[] filesUser = dirUser.listFiles();
                    for (File fileUser : filesUser) {
                        String filepath = fileUser.getName();
                        fileLogPojo = new FileLogPojo();
                        fileLogPojo.setFilepath(filepath);
                        fileLogPojo.setFileuser(fileuser);
                        Long count = fileLogService.selectByFileLog(fileLogPojo);
                        if (count == 0) {
                            fileUser.delete();
                            logger.info("[del]" + fileuser + "/" + fileUser.getName());
                        }
                    }
                } else {
                    file.delete();
                    logger.info("[del]" + file.getName());

                }
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
