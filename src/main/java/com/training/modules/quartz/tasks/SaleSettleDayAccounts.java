package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.MtmySaleRelieveService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.utils.RedisLock;

/**
 * 总结算
 *
 */
@Component
public class SaleSettleDayAccounts extends CommonService{

	public static final String MTMY_ID = "mtmy_id_";//用户云币缓存前缀
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static MtmySaleRelieveService mtmySaleRelieveService;
	
	static{
		mtmySaleRelieveService = (MtmySaleRelieveService) BeanUtil.getBean("mtmySaleRelieveService");
	}
	
	/**
	 * 总结算
	 */
	public void saleSettleDayAccounts(){
		logger.info("[work0],start,总结算，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("saleSettleDayAccounts");
		taskLog.setStartDate(startDate);
		
		try {
			// 结算的所有用户及云币
			List<Users> list = mtmySaleRelieveService.changeSaleAccounts();
			mtmySaleRelieveService.SaleSettleDayAccounts();
			for (int i = 0; i < list.size(); i++) {
				boolean str = redisClientTemplate.exists(MTMY_ID+list.get(i).getUserid());
				if(str){
					RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+list.get(i).getUserid());
					redisLock.lock();
					redisClientTemplate.incrBy(MTMY_ID+list.get(i).getUserid(),list.get(i).getPaypoints());
					redisLock.unlock();
				}
			}
			taskLog.setJobDescription("[work],总结算");
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务saleSettleDayAccounts】总结算,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,总结算,结束时间："+df.format(new Date()));
	}
}
