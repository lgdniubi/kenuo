package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.MtmySaleRelieveService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;

/**
 * 日结算
 *
 */
@Component
public class SaleDayAccounts extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static MtmySaleRelieveService mtmySaleRelieveService;
	
	static{
		mtmySaleRelieveService = (MtmySaleRelieveService) BeanUtil.getBean("mtmySaleRelieveService");
	}
	
	/**
	 * 日结算
	 */
	public void saleDayAccounts(){
		logger.info("[work0],start,日结算，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("saleDayAccounts");
		taskLog.setStartDate(startDate);
		
		try {
			mtmySaleRelieveService.SaleDayAccounts();
			taskLog.setJobDescription("[work],日结算");
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务saleDayAccounts】日结算,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,日结算,结束时间："+df.format(new Date()));
	}
}
