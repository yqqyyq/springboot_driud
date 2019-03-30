package com.xxx.quartz.job;

import com.xxx.mail.ReceiveMailCompment;
import com.xxx.pojo.IaEmailPojo;
import com.xxx.service.ReceiveMailService;
import com.xxx.utils.CommonDate;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@DisallowConcurrentExecution
public class ReceiveMailJob implements Job, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveMailJob.class);
    private static final long serialVersionUID = 1L;

    @Autowired
    private ReceiveMailCompment receiveMailCompment;

    @Autowired
    private ReceiveMailService receiveMailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //取得job详情
        JobDetail jobDetail = context.getJobDetail();
        //取得job的类
        logger.info("Job Start Class: " + jobDetail.getJobClass());
        //取得job开始时间
        logger.info("start :" + CommonDate.transferDate(context.getFireTime()));

        List<IaEmailPojo> list=receiveMailCompment.resceive();

        if(list!=null){
            //logger.info(String.valueOf(list.size()));
            int size=receiveMailService.insertBatch(list);
            if(size==list.size()){
                logger.info("succ|"+size);
            }
        }
        logger.info("Job End Class: " + jobDetail.getJobClass());
        logger.info("end :" + CommonDate.getTime24());
        //测试效果 保证上一个任务执行完后，再去执行下一个任务
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
