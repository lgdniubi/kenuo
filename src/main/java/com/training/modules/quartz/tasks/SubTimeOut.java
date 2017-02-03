package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mtmy.common.cachemanager.vo.OneDayTime;
import com.mtmy.common.cachemanager.vo.SaveModel;
import com.mtmy.common.cachemanager.vo.Time;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.quartz.utils.TaskUtils;


/**
 * 每个半个小时执行一次，将当天过期的时间置为过期
 * @author kele
 *
 */
@Component
public class SubTimeOut extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 每个半个小时执行一次，将当天过期的时间置为过期
	 */
	public void subTimeOut(){
		logger.info("[work0],start,扫描美容师预约过期时间,开始时间："+df.format(new Date()));
		
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("subTimeOut");
		taskLog.setStartDate(startDate);

		try {
			
			List<String> beauty_ids = redisClientTemplate.lrange(RedisConfig.BEAUTY_ID_HASH, 0, -1);
			
			for(String bid : beauty_ids){
				logger.info("[work0],扫描美容师["+bid+"]的过期时间");
				RedisLock l = new RedisLock(redisClientTemplate, bid);
				l.lock();
				try {
					
					SaveModel sm = (SaveModel) TaskUtils.deserialize(redisClientTemplate.get(TaskUtils.serialize(RedisConfig.SUBSCRIBETIME_PREFIX+bid)));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E",Locale.CHINA);
					Date date = new Date();
					String odt_key = sdf.format(date);
					logger.info("odt_key = "+odt_key);
					OneDayTime odt = sm.getOdts().get(sm.getIndex().get(odt_key));
					
					List<Time> times = odt.getTimes();
					for(int i=0;i<times.size();i++){
						//logger.info("[work0],第"+i+"个元素");
						//logger.info("[work0],key = "+times.get(i).getName());
						//logger.info("[work0],times  = ["+times.get(i).getTime().getTime()+"] , now() = ["+date.getTime()+"]");
						if((times.get(i).getTime().getTime() <= date.getTime()) && times.get(i).getIs_use() == 0 ){
							logger.info("结果：置为已用");
							times.get(i).setIs_use(1);
						}
					}
					redisClientTemplate.set(TaskUtils.serialize(RedisConfig.SUBSCRIBETIME_PREFIX+bid), TaskUtils.serialize(sm));
					
				} catch (Exception e) {
					logger.info("[work0],exception扫描美容师预约过期时间");
					e.printStackTrace();
				}finally {
					l.unlock();
				}
			}
			
			taskLog.setJobDescription("[work],扫描美容师个数："+beauty_ids.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务subTimeOut】更新美容师预约时间,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end扫描美容师预约过期时间,结束时间："+df.format(new Date()));
	}
}
