package com.training.modules.quartz.quartzfactory;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.training.modules.quartz.entity.Task;
import com.training.modules.quartz.utils.TaskUtils;


/**
 * @Description: 若一个方法一次执行不完下次轮转时则等待该方法执行完后才执行下一次操作
 * @author kele
 * @date 2016年7月30日17:58:17
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job{

	private Logger logger = Logger.getLogger(QuartzJobFactoryDisallowConcurrentExecution.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("#####QuartzJobFactoryDisallowConcurrentExecution[任务成功开始运行]#########");
		Task scheduleJob = (Task) context.getMergedJobDataMap().get("scheduleJob");
		TaskUtils.invokMethod(scheduleJob);
		logger.debug("#####QuartzJobFactoryDisallowConcurrentExecution等待执行的任务名称为："+scheduleJob.getJobName());
	}
}
