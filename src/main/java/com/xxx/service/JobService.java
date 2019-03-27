package com.xxx.service;

import com.xxx.pojo.QuartzPojo;

import java.util.List;

public interface JobService {

    List<QuartzPojo> listQuartzPojo();
    
    Long listQuartzPojoCount();

    List<QuartzPojo> SelectByJobName(String jobName);
}
