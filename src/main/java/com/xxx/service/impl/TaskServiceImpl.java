/*
package com.xxx.service.impl;


import com.xxx.quartz.QuartzException;
import com.xxx.service.TaskService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private Scheduler scheduler;


    */
/**
     * 保存定时任务
     *//*

    public Boolean addTask(TaskPojo taskPojo) {
        String jobName = taskPojo.getJobName(),
                jobGroup = taskPojo.getJobGroup(),
                cronExpression = taskPojo.getCronExpression(),
                jobDescription = taskPojo.getJobDescription();
        try {
            if (checkExists(jobName, jobGroup)) {
                throw new QuartzException(String.format("Job已经存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(jobDescription).withSchedule(scheduleBuilder).build();

            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(taskPojo.getJobClass());
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException | ClassNotFoundException e) {
            throw new QuartzException("类名不存在或执行表达式错误");
        }
    }

    */
/**
     * 开始定时任务
     *//*

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resumeTask(TaskPojo taskPojo) {
        try {
            scheduler.resumeJob(JobKey.jobKey(taskPojo.getJobName(), taskPojo.getJobGroup()));
            return true;
        } catch (Exception e) {
            throw new QuartzException("FAILED");
        }
    }

    */
/**
     * 查询job
     *//*

    @Override
    @Transactional(readOnly = true)
    public List<TaskPojo> findTaskList() {
        return taskDao.findTaskList();
    }

    */
/**
     * 修改定时任务
     *//*

    public Boolean updateTask(TaskPojo taskPojo) {
        String jobName = taskPojo.getJobName(),
                jobGroup = taskPojo.getJobGroup(),
                cronExpression = taskPojo.getCronExpression(),
                jobDescription = taskPojo.getJobDescription(),
                createTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            if (!checkExists(jobName, jobGroup)) {
                throw new QuartzException(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(cronScheduleBuilder).build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(jobDescription);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            scheduler.scheduleJob(jobDetail, triggerSet, true);
            return true;
        } catch (SchedulerException e) {
            throw new QuartzException("类名不存在或执行表达式错误");
        }
    }

    */
/**
     * 停止任务
     *//*

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean pauseTask(TaskPojo taskPojo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(taskPojo.getJobName(), taskPojo.getJobGroup());
        try {
            if (checkExists(taskPojo.getJobName(), taskPojo.getJobGroup())) {
                scheduler.pauseTrigger(triggerKey); //停止触发器
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new QuartzException(e.getMessage());
        }
    }

    */
/**
     * 删除任务
     *//*

    public Boolean deleteTask(TaskPojo taskPojo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(taskPojo.getJobName(), taskPojo.getJobGroup());
        try {
            if (checkExists(taskPojo.getJobName(), taskPojo.getJobGroup())) {
                scheduler.pauseTrigger(triggerKey); //停止触发器
                scheduler.unscheduleJob(triggerKey); //移除触发器
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException(e.getMessage());
        }
        return false;
    }

    */
/**
     * 验证是否存在
     *//*

    private boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}
*/
