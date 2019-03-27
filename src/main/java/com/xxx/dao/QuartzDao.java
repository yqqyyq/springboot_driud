package com.xxx.dao;

import com.xxx.pojo.QuartzPojo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartzDao {
    List<QuartzPojo> listQuartzPojo();

    Long listQuartzPojoCount();

    List<QuartzPojo> SelectByJobName(String jobName);
}
