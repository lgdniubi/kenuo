package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;

/**
 * 清除天的临时数据
 * @author kele
 * @version 2016年9月29日
 */
@Component
public class ClearDayTemp extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 清除天的临时数据
	 */
	public void clearDayTemp(){
		logger.info("[work0],start,[任务]，->清除天的临时数据,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("clearDayTemp");
		taskLog.setStartDate(startDate);
		
		try {
			logger.info("[#日清理#]，清除妃子校学分及云币上限数据");
			Set<String> set = redisClientTemplate.smembers(RedisConfig.train_uplimit_set);
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()){
				redisClientTemplate.del(RedisConfig.train_nowruleuplimit_prefix+iter.next());
			}
			logger.info("[#日清理#]，清除每天美耶学分及云币上限数据");
			Set<String> s = redisClientTemplate.smembers(RedisConfig.mtmy_uplimit_set);
			Iterator<String> it = s.iterator();
			while(it.hasNext()){
				redisClientTemplate.del(RedisConfig.mtmy_nowruleuplimit_prefix+it.next());
			}
			
			//保存日志 
			taskLog.setJobDescription("[work],清除天的临时数据:颜值上限|金币上限|美容师当天学分累计|美容师当天金币累计 时间： "+df.format(new Date()));
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务clearDayTemp】清除天的临时数据,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,[任务]，->清除天的临时数据,结束时间："+df.format(new Date()));
	}
}
