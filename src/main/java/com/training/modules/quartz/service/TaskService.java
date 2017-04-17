package com.training.modules.quartz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.training.common.persistence.Page;
import com.training.modules.quartz.dao.ITaskDao;
import com.training.modules.quartz.entity.Task;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.quartzfactory.QuartzJobFactory;
import com.training.modules.quartz.quartzfactory.QuartzJobFactoryDisallowConcurrentExecution;
import com.training.modules.quartz.tasks.rediscache.RedisCaCheLoad;

/**
 * 定时任务-Service
 * @author kele
 * @version 2016年7月28日
 */
@Service
public class TaskService {

	//日志记录器
	private static final Log logger = LogFactory.getLog(TaskService.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private ITaskDao iTaskDao;
	
	/*
	 * 容器初始化 bean 注解
	 * @PostConstruct
	*/
	
	@PostConstruct
	public void init() throws Exception {
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("init");
		taskLog.setStartDate(startDate);
		
		try {
			//定时器加载时间-进行redisCaChe缓存
			new RedisCaCheLoad().load();
			
			logger.info("######[执行定时任务]######");
			// 这里获取任务信息数据
			Task job = new Task();
			job.setJobStatus("0");//开启状态
			List<Task> jobList = iTaskDao.findAllTasks(job);
			logger.info("#####[开启-定时任务数量：]"+jobList.size());
			if(jobList.size() > 0){
				for (Task task : jobList) {
					addJob(task);
				}
			}
			
			taskLog.setJobDescription("[work] 定时器加载时间-进行redisCaChe缓存 | 开启-定时任务数量："+jobList.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####定时任务init(),加载异常，异常信息为："+e.getMessage());
			
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime())/1000;//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("后台重启，重新加载");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 分页定时任务
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<Task> findAllTasks(Page<Task> page, Task task) { 
		task.setPage(page);
		List<Task> taskList = iTaskDao.findAllTasks(task);
		page.setList(taskList);
		return page;
	}
	
	/**
	 * 根据定时任务id，查询其定时任务
	 * @param id
	 * @return
	 */
	public Task findTaskById(String id){
		return iTaskDao.findTaskById(id);
	}
	
	/**
	 * 添加定时任务
	 * @param task
	 * @throws Exception 
	 */
	public void addTaskJob(Task task) throws Exception{
		//保存数据库
		iTaskDao.insertTask(task);
		task = iTaskDao.findTaskById(task.getJobId());
		if(null != task){
			//保存成功后，添加定时任务
			addJob(task);
		}
	}
	
	public void updateTask(Task task) throws Exception{
		int result = iTaskDao.updateTask(task);
		if(1 == result){
			//修改成功后，删除定时器
			task = iTaskDao.findTaskById(task.getJobId());
			deleteJob(task);
			task.setJobStatus(Task.STATUS_NOT_RUNNING);
		}
	}
	
	/**
	 * 修改定时任务状态
	 * @return
	 */
	public int updateTaskStatus(Task task){
		return iTaskDao.updateTaskStatus(task);
	}
	
	/**
	 * 更改任务状态
	 * @throws SchedulerException
	 */
	public void changeStatus(Task task, String cmd) throws SchedulerException {
		if (Task.STATUS_NOT_RUNNING.equals(cmd)) {
			task.setJobStatus(Task.STATUS_NOT_RUNNING);
			deleteJob(task);
		} else if (Task.STATUS_RUNNING.equals(cmd)) {
			task.setJobStatus(Task.STATUS_RUNNING);
			addJob(task);
		}
	}
	
	/**
	 * 更改任务 cron表达式
	 * @throws SchedulerException
	 */
	public void updateCron(String id, String cron) throws SchedulerException {
		Task job = findTaskById(id);
		if (job == null) {
			return;
		}
		job.setCronExpression(cron);
		if (Task.STATUS_RUNNING.equals(job.getJobStatus())) {
			updateJobCron(job);
		}
		iTaskDao.updateCronExpression(job);

	}
	
	/**
	 * 添加任务
	 * @param task
	 * @throws SchedulerException
	 */
	public void addJob(Task task) throws SchedulerException {
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("addJob");
		taskLog.setStartDate(startDate);
		
		try {
			//判断定时任务不为null时，并且，运行状态为[0-开启]
			if (task == null || Task.STATUS_NOT_RUNNING.equals(task.getJobStatus())) {
				return;
			}
			logger.info("#####[启动-定时任务名称：]"+task.getJobName());
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
			//获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			//不存在，创建一个
			if (null == trigger) {
				Class<? extends Job> clazz = Task.CONCURRENT_IS.equals(task.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
				JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(task.getJobName(), task.getJobGroup()).build();
				jobDetail.getJobDataMap().put("scheduleJob", task);
				//表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				//按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup()).withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// Trigger已存在，那么更新相应的定时设置
				//表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				//按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				//按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
			
			//保存日志 
			taskLog.setJobDescription("[启动]定时任务："+task.getJobName()+" 是否并发[0-是；1-否]:"+task.getIsConcurrent());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【addJob】添加定时任务,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			throw new SchedulerException();
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("添加定时任务");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 获取所有计划中的任务列表
	 * @return
	 * @throws SchedulerException
	 */
	public List<Task> getAllTask() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<Task> jobList = new ArrayList<Task>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				Task job = new Task();
				job.setJobName(jobKey.getName());
				job.setJobGroup(jobKey.getGroup());
				job.setDescription("触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setJobStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}
	
	/**
	 * 所有正在运行的job
	 * @return
	 * @throws SchedulerException
	 */
	public List<Task> getRunningTask() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<Task> jobList = new ArrayList<Task>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			Task job = new Task();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			job.setJobName(jobKey.getName());
			job.setJobGroup(jobKey.getGroup());
			job.setDescription("触发器:" + trigger.getKey());
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			job.setJobStatus(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCronExpression(cronExpression);
			}
			jobList.add(job);
		}
		return jobList;
	}
	
	/**
	 * 暂停一个job
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseJob(Task task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复一个job
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(Task task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除定时器任务-数据库
	 * @param task
	 * @return
	 */
	public int deleteByLogic(Task task){
		return iTaskDao.deleteByLogic(task);
	}
	
	/**
	 * 删除一个job
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteJob(Task task) throws SchedulerException {
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("deleteJob");
		taskLog.setStartDate(startDate);
		
		try {
			logger.info("#####删除定时任务名称："+task.getJobName());
			//删除任务-定时器
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
			scheduler.deleteJob(jobKey);
			
			//保存日志 
			taskLog.setJobDescription("[删除]定时任务名称："+task.getJobName());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【deleteJob】删除定时任务,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			
			throw new SchedulerException();
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("删除定时任务");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 立即执行job
	 * @param task
	 * @throws SchedulerException
	 */
	public void runJobNow(Task task) throws SchedulerException {
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("runJobNow");
		taskLog.setStartDate(startDate);
		
		try {
			logger.info("#####立即执行定时任务名称："+task.getJobName());
			
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
			scheduler.triggerJob(jobKey);
			
			//保存日志 
			taskLog.setJobDescription("[立即执行]定时任务名称："+task.getJobName() +" 是否并发[0-是；1-否]:"+task.getIsConcurrent());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【runJobNow】立即执行定时任务,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			
			throw new SchedulerException();
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("立即执行定时任务");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 更新job时间表达式
	 * @param task
	 * @throws SchedulerException
	 */
	public void updateJobCron(Task task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
	}
	
	/**
	 * 保存定时任务日志
	 * @param taskLog
	 */
	public void saveTaskLog(TaskLog taskLog){
		iTaskDao.insertTaskLog(taskLog);
	}
	
	/**
	 * 分页定时任务Log日志列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<TaskLog> findAllTasksLogs(Page<TaskLog> page, TaskLog taskLog) { 
		taskLog.setPage(page);
		List<TaskLog> taskLogList = iTaskDao.findAllTasksLogs(taskLog);
		page.setList(taskLogList);
		return page;
	}
	
	/**
	 * 根据定时任务id，查询其定时任务log
	 * @param id
	 * @return
	 */
	public TaskLog findLogById(String id){
		return iTaskDao.findLogById(id);
	}
	
	/**
	 * 更新表达式，判断表达式是否正确可用一下代码
	 * @param args
	 */
	public static void main(String[] args) {
		//更新表达式，判断表达式是否正确可用一下代码
		@SuppressWarnings("unused")
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */20 * * * ?");
	}
}
