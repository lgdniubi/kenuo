package com.training.modules.quartz.tasks;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.ReportMapper;
/**
 * 统计会员及机构欠款
 * @author 
 *
 */
@Component
public class AccountArrearage extends CommonService{

    @Autowired
    private static ReportMapper reportMapper;
    
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	
    static{
    	reportMapper = (ReportMapper) BeanUtil.getBean("reportMapper");
	}
    
    public void accountArrearage(){
        logger.info("[work0],start,统计会员及机构欠款,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("accountArrearage");
		taskLog.setStartDate(startDate);
		
		try {
			 reportMapper.dropTableUserAppArrearge();
			 reportMapper.dropTableOfficeAppArrearge();
			 reportMapper.insertTableUserAppArrearge();
			 reportMapper.insertTableOfficeAppArrearge();
		} catch (Exception e) {
			logger.error("#####【定时任务accountArrearage】统计会员及机构欠款,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,统计会员及机构欠款,结束时间："+df.format(new Date()));
	}
    
}
