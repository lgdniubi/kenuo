package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.sys.entity.UserOfficeCode;
import com.training.modules.sys.service.SystemService;

/**
 * 用户-office_id-映射表
 * 定时任务WeekScorePersistent 统计使用
 * @author kele
 *
 */
@Component
public class UserOfficeCodeMapping extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SystemService systemService;
	
	static{
		systemService = (SystemService) BeanUtil.getBean("systemService");
	}
	
	/**
	 * user_id - office_id
	 */
	public void user_officecode(){
		logger.info("[work0],start,userid-officecode对应任务,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("user_officecode");
		taskLog.setStartDate(startDate);
		
		try {
			/**
			 * 查询的是office-Id
			 */
			List<UserOfficeCode> uocs =  systemService.queryUserOfficeCodes();
			for(UserOfficeCode uoc : uocs){
				//log.info("user_id = "+uoc.getUserid()+", office_code = "+uoc.getOfficecode());
				redisClientTemplate.hset(RedisConfig.OFFICE_IDS_KEY, uoc.getUserid(), uoc.getOfficeid());
			}
			
			taskLog.setJobDescription("[work],userid-officecode对应任务,个数："+uocs.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务user_officecode】userid-officecode对应任务,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,userid-officecode对应任务,结束时间："+df.format(new Date()));
	}
}
