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
 * 瀹氭椂浠诲姟-Service
 * @author kele
 * @version 2016骞�7鏈�28鏃�
 */
@Service
public class TaskService {

	//鏃ュ織璁板綍鍣�
	private static final Log logger = LogFactory.getLog(TaskService.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private ITaskDao iTaskDao;
	
	/*
	 * 瀹瑰櫒鍒濆鍖� bean 娉ㄨВ
	 * @PostConstruct
	*/
	
	@PostConstruct
	public void init() throws Exception {
		
		//娣诲姞鏃ュ織
		TaskLog taskLog = new TaskLog();
		Date startDate;	//寮�濮嬫椂闂�
		Date endDate;	//缁撴潫鏃堕棿
		long runTime;	//杩愯鏃堕棿
		
		startDate = new Date();
		taskLog.setJobName("init");
		taskLog.setStartDate(startDate);
		
//		try {
//			//瀹氭椂鍣ㄥ姞杞芥椂闂�-杩涜redisCaChe缂撳瓨
//			new RedisCaCheLoad().load();
//			
//			logger.info("######[鎵ц瀹氭椂浠诲姟]######");
//			// 杩欓噷鑾峰彇浠诲姟淇℃伅鏁版嵁
//			Task job = new Task();
//			job.setJobStatus("0");//寮�鍚姸鎬�
//			List<Task> jobList = iTaskDao.findAllTasks(job);
//			logger.info("#####[寮�鍚�-瀹氭椂浠诲姟鏁伴噺锛歖"+jobList.size());
//			if(jobList.size() > 0){
//				for (Task task : jobList) {
//					addJob(task);
//				}
//			}
//			
//			taskLog.setJobDescription("[work] 瀹氭椂鍣ㄥ姞杞芥椂闂�-杩涜redisCaChe缂撳瓨 | 寮�鍚�-瀹氭椂浠诲姟鏁伴噺锛�"+jobList.size());
//			taskLog.setStatus(0);//浠诲姟鐘舵��
//			
//		} catch (Exception e) {
//			logger.error("#####瀹氭椂浠诲姟init(),鍔犺浇寮傚父锛屽紓甯镐俊鎭负锛�"+e.getMessage());
//			
//			taskLog.setStatus(1);
//			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
//		}finally {
//			endDate = new Date();//缁撴潫鏃堕棿
//			runTime = (endDate.getTime() - startDate.getTime())/1000;//杩愯鏃堕棿
//			taskLog.setEndDate(new Date());	//缁撴潫鏃堕棿
//			taskLog.setRunTime(runTime);
//			taskLog.setRemarks("鍚庡彴閲嶅惎锛岄噸鏂板姞杞�");
//			iTaskDao.insertTaskLog(taskLog);
//		}
	}
	
	/**
	 * 鍒嗛〉瀹氭椂浠诲姟
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
	 * 鏍规嵁瀹氭椂浠诲姟id锛屾煡璇㈠叾瀹氭椂浠诲姟
	 * @param id
	 * @return
	 */
	public Task findTaskById(String id){
		return iTaskDao.findTaskById(id);
	}
	
	/**
	 * 娣诲姞瀹氭椂浠诲姟
	 * @param task
	 * @throws Exception 
	 */
	public void addTaskJob(Task task) throws Exception{
		//淇濆瓨鏁版嵁搴�
		iTaskDao.insertTask(task);
		task = iTaskDao.findTaskById(task.getJobId());
		if(null != task){
			//淇濆瓨鎴愬姛鍚庯紝娣诲姞瀹氭椂浠诲姟
			addJob(task);
		}
	}
	
	public void updateTask(Task task) throws Exception{
		int result = iTaskDao.updateTask(task);
		if(1 == result){
			//淇敼鎴愬姛鍚庯紝鍒犻櫎瀹氭椂鍣�
			task = iTaskDao.findTaskById(task.getJobId());
			deleteJob(task);
			task.setJobStatus(Task.STATUS_NOT_RUNNING);
		}
	}
	
	/**
	 * 淇敼瀹氭椂浠诲姟鐘舵��
	 * @return
	 */
	public int updateTaskStatus(Task task){
		return iTaskDao.updateTaskStatus(task);
	}
	
	/**
	 * 鏇存敼浠诲姟鐘舵��
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
	 * 鏇存敼浠诲姟 cron琛ㄨ揪寮�
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
	 * 娣诲姞浠诲姟
	 * @param task
	 * @throws SchedulerException
	 */
	public void addJob(Task task) throws SchedulerException {
		
		//娣诲姞鏃ュ織
		TaskLog taskLog = new TaskLog();
		Date startDate;	//寮�濮嬫椂闂�
		Date endDate;	//缁撴潫鏃堕棿
		long runTime;	//杩愯鏃堕棿
		
		startDate = new Date();
		taskLog.setJobName("addJob");
		taskLog.setStartDate(startDate);
		
		try {
			//鍒ゆ柇瀹氭椂浠诲姟涓嶄负null鏃讹紝骞朵笖锛岃繍琛岀姸鎬佷负[0-寮�鍚痌
			if (task == null || Task.STATUS_NOT_RUNNING.equals(task.getJobStatus())) {
				return;
			}
			logger.info("#####[鍚姩-瀹氭椂浠诲姟鍚嶇О锛歖"+task.getJobName());
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
			//鑾峰彇trigger锛屽嵆鍦╯pring閰嶇疆鏂囦欢涓畾涔夌殑 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			//涓嶅瓨鍦紝鍒涘缓涓�涓�
			if (null == trigger) {
				Class<? extends Job> clazz = Task.CONCURRENT_IS.equals(task.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
				JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(task.getJobName(), task.getJobGroup()).build();
				jobDetail.getJobDataMap().put("scheduleJob", task);
				//琛ㄨ揪寮忚皟搴︽瀯寤哄櫒
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				//鎸夋柊鐨刢ronExpression琛ㄨ揪寮忔瀯寤轰竴涓柊鐨則rigger
				trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup()).withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// Trigger宸插瓨鍦紝閭ｄ箞鏇存柊鐩稿簲鐨勫畾鏃惰缃�
				//琛ㄨ揪寮忚皟搴︽瀯寤哄櫒
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
				//鎸夋柊鐨刢ronExpression琛ㄨ揪寮忛噸鏂版瀯寤簍rigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				//鎸夋柊鐨則rigger閲嶆柊璁剧疆job鎵ц
				scheduler.rescheduleJob(triggerKey, trigger);
			}
			
			//淇濆瓨鏃ュ織 
			taskLog.setJobDescription("[鍚姩]瀹氭椂浠诲姟锛�"+task.getJobName()+" 鏄惁骞跺彂[0-鏄紱1-鍚:"+task.getIsConcurrent());
			taskLog.setStatus(0);//浠诲姟鐘舵��
			
		} catch (Exception e) {
			logger.error("#####銆恆ddJob銆戞坊鍔犲畾鏃朵换鍔�,鍑虹幇寮傚父锛屽紓甯镐俊鎭负锛�"+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			throw new SchedulerException();
		}finally {
			endDate = new Date();//缁撴潫鏃堕棿
			runTime = (endDate.getTime() - startDate.getTime());//杩愯鏃堕棿
			taskLog.setEndDate(new Date());	//缁撴潫鏃堕棿
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("娣诲姞瀹氭椂浠诲姟");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 鑾峰彇鎵�鏈夎鍒掍腑鐨勪换鍔″垪琛�
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
				job.setDescription("瑙﹀彂鍣�:" + trigger.getKey());
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
	 * 鎵�鏈夋鍦ㄨ繍琛岀殑job
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
			job.setDescription("瑙﹀彂鍣�:" + trigger.getKey());
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
	 * 鏆傚仠涓�涓猨ob
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseJob(Task task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 鎭㈠涓�涓猨ob
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resumeJob(Task task) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 鍒犻櫎瀹氭椂鍣ㄤ换鍔�-鏁版嵁搴�
	 * @param task
	 * @return
	 */
	public int deleteByLogic(Task task){
		return iTaskDao.deleteByLogic(task);
	}
	
	/**
	 * 鍒犻櫎涓�涓猨ob
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteJob(Task task) throws SchedulerException {
		//娣诲姞鏃ュ織
		TaskLog taskLog = new TaskLog();
		Date startDate;	//寮�濮嬫椂闂�
		Date endDate;	//缁撴潫鏃堕棿
		long runTime;	//杩愯鏃堕棿
		
		startDate = new Date();
		taskLog.setJobName("deleteJob");
		taskLog.setStartDate(startDate);
		
		try {
			logger.info("#####鍒犻櫎瀹氭椂浠诲姟鍚嶇О锛�"+task.getJobName());
			//鍒犻櫎浠诲姟-瀹氭椂鍣�
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
			scheduler.deleteJob(jobKey);
			
			//淇濆瓨鏃ュ織 
			taskLog.setJobDescription("[鍒犻櫎]瀹氭椂浠诲姟鍚嶇О锛�"+task.getJobName());
			taskLog.setStatus(0);//浠诲姟鐘舵��
			
		} catch (Exception e) {
			logger.error("#####銆恉eleteJob銆戝垹闄ゅ畾鏃朵换鍔�,鍑虹幇寮傚父锛屽紓甯镐俊鎭负锛�"+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			
			throw new SchedulerException();
		}finally {
			endDate = new Date();//缁撴潫鏃堕棿
			runTime = (endDate.getTime() - startDate.getTime());//杩愯鏃堕棿
			taskLog.setEndDate(new Date());	//缁撴潫鏃堕棿
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("鍒犻櫎瀹氭椂浠诲姟");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 绔嬪嵆鎵цjob
	 * @param task
	 * @throws SchedulerException
	 */
	public void runJobNow(Task task) throws SchedulerException {
		//娣诲姞鏃ュ織
		TaskLog taskLog = new TaskLog();
		Date startDate;	//寮�濮嬫椂闂�
		Date endDate;	//缁撴潫鏃堕棿
		long runTime;	//杩愯鏃堕棿
		
		startDate = new Date();
		taskLog.setJobName("runJobNow");
		taskLog.setStartDate(startDate);
		
		try {
			logger.info("#####绔嬪嵆鎵ц瀹氭椂浠诲姟鍚嶇О锛�"+task.getJobName());
			
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
			scheduler.triggerJob(jobKey);
			
			//淇濆瓨鏃ュ織 
			taskLog.setJobDescription("[绔嬪嵆鎵ц]瀹氭椂浠诲姟鍚嶇О锛�"+task.getJobName() +" 鏄惁骞跺彂[0-鏄紱1-鍚:"+task.getIsConcurrent());
			taskLog.setStatus(0);//浠诲姟鐘舵��
			
		} catch (Exception e) {
			logger.error("#####銆恟unJobNow銆戠珛鍗虫墽琛屽畾鏃朵换鍔�,鍑虹幇寮傚父锛屽紓甯镐俊鎭负锛�"+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
			
			throw new SchedulerException();
		}finally {
			endDate = new Date();//缁撴潫鏃堕棿
			runTime = (endDate.getTime() - startDate.getTime());//杩愯鏃堕棿
			taskLog.setEndDate(new Date());	//缁撴潫鏃堕棿
			taskLog.setRunTime(runTime);
			taskLog.setRemarks("绔嬪嵆鎵ц瀹氭椂浠诲姟");
			iTaskDao.insertTaskLog(taskLog);
		}
	}
	
	/**
	 * 鏇存柊job鏃堕棿琛ㄨ揪寮�
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
	 * 淇濆瓨瀹氭椂浠诲姟鏃ュ織
	 * @param taskLog
	 */
	public void saveTaskLog(TaskLog taskLog){
		iTaskDao.insertTaskLog(taskLog);
	}
	
	/**
	 * 鍒嗛〉瀹氭椂浠诲姟Log鏃ュ織鍒楄〃
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
	 * 鏍规嵁瀹氭椂浠诲姟id锛屾煡璇㈠叾瀹氭椂浠诲姟log
	 * @param id
	 * @return
	 */
	public TaskLog findLogById(String id){
		return iTaskDao.findLogById(id);
	}
	
	/**
	 * 鏇存柊琛ㄨ揪寮忥紝鍒ゆ柇琛ㄨ揪寮忔槸鍚︽纭彲鐢ㄤ竴涓嬩唬鐮�
	 * @param args
	 */
	public static void main(String[] args) {
		//鏇存柊琛ㄨ揪寮忥紝鍒ゆ柇琛ㄨ揪寮忔槸鍚︽纭彲鐢ㄤ竴涓嬩唬鐮�
		@SuppressWarnings("unused")
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 */20 * * * ?");
	}
}
