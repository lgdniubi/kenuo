package com.training.modules.quartz.quartzfactory;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.training.modules.quartz.entity.Task;
import com.training.modules.quartz.utils.TaskUtils;


/**
 * 定时任务运行工厂类
 * @author kele
 * @version 2016年7月28日
 */
public class QuartzJobFactory implements Job{

	private Logger logger = Logger.getLogger(QuartzJobFactory.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("#####QuartzJobFactory[任务成功开始运行]#########");
        Task scheduleJob = (Task)context.getMergedJobDataMap().get("scheduleJob");
        logger.debug("#####QuartzJobFactory执行的任务名称为："+scheduleJob.getJobName()+"  方法："+scheduleJob.getMethodName());
        TaskUtils.invokMethod(scheduleJob);
	}
}
