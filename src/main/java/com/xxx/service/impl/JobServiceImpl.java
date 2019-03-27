package com.xxx.service.impl;

import com.xxx.dao.QuartzDao;
import com.xxx.pojo.QuartzPojo;
import com.xxx.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jobService")
public class JobServiceImpl implements JobService {

	@Autowired
	private QuartzDao quartzDao;

	@Override
	public List<QuartzPojo> listQuartzPojo() {
		return quartzDao.listQuartzPojo();
	}

	@Override
	public Long listQuartzPojoCount() {
		return quartzDao.listQuartzPojoCount();
	}

	@Override
	public List<QuartzPojo> SelectByJobName(String jobName) {
		return quartzDao.SelectByJobName(jobName);
	}
}
