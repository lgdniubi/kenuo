package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.service.TrainLiveAuditService;

/**
 * 妃子校直播审核
 *
 */
@Component
public class LiveAudit extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static TrainLiveAuditService trainLiveAuditService;
	
	static{
		trainLiveAuditService = (TrainLiveAuditService) BeanUtil.getBean("trainLiveAuditService");
	}
	
	/**
	 * 妃子校直播审核
	 */
	public void liveAudit(){
		logger.info("[work0],start,妃子校审核直播，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("trainLiveAudit");
		taskLog.setStartDate(startDate);
		
		try {
			int num =  trainLiveAuditService.updateLiveLiveAudit();
			taskLog.setJobDescription("[work],妃子校直播审核,个数："+num);
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务trainLiveAudit】妃子校审核直播,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,妃子校审核直播,结束时间："+df.format(new Date()));
	}
}
