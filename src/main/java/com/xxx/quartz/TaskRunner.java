package com.xxx.quartz;

import com.xxx.pojo.QuartzPojo;
import com.xxx.service.JobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskRunner implements ApplicationRunner{
    
	private final static Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);
	
	@Autowired
    private JobService jobService;
	@Autowired
    private Scheduler scheduler;
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run(ApplicationArguments var) throws Exception{
    	Long count = jobService.listQuartzPojoCount();
    	if(count==0){
    		LOGGER.info("初始化测试任务");
    		QuartzPojo quartz = new QuartzPojo();
    		quartz.setJobName("test01");
    		quartz.setJobGroup("test");
    		quartz.setDescription("测试任务");
    		quartz.setJobClassName("com.xxx.quartz.job.ChickenJob");
    		quartz.setCronExpression("0/20 * * * * ?");
   	        Class cls = Class.forName(quartz.getJobClassName()) ;
   	        cls.newInstance();
   	        //构建job信息
   	        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),
   	        		quartz.getJobGroup())
   	        		.withDescription(quartz.getDescription()).build();
   	        //添加JobDataMap数据
   	        job.getJobDataMap().put("itstyle", "团险常规启动");
   	        job.getJobDataMap().put("blog", "https://github.com/yqqyyq");
		   	job.getJobDataMap().put("data", new String[]{"yqqyyq","xxx"});
   	        // 触发时间点
   	        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
   	        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
   	                .startNow().withSchedule(cronScheduleBuilder).build();	
   	        //交由Scheduler安排触发
   	        scheduler.scheduleJob(job, trigger);
    	}
    }

}