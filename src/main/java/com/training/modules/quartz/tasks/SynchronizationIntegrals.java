package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.TrainLiveAuditService;

/**
 * 同步云币
 * @author menglei
 *
 */
@Component
@SuppressWarnings("all")
public class SynchronizationIntegrals extends CommonService{

	public static final String MTMY_ID = "mtmy_id_";//每天美耶id
	private Logger logger = Logger.getLogger(SynchronizationIntegrals.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static EntryService entryService;
	private static RedisClientTemplate redisClientTemplate;
	
	static{
		entryService = (EntryService) BeanUtil.getBean("entryService");
		redisClientTemplate = (RedisClientTemplate)BeanUtil.getBean("redisClientTemplate");
	}
	
	public void SyncIntegrals(){
	
		logger.info("[work0],start,妃子校云币同步，开始时间："+df.format(new Date()));
		
		List<String> mtmy_id_list = new ArrayList<String>();
		//添加日志
				TaskLog taskLog = new TaskLog();
				Date startDate;	//开始时间
				Date endDate;	//结束时间
				long runTime;	//运行时间
				
				startDate = new Date();
				taskLog.setJobName("SynchronizationIntegrals");
				taskLog.setStartDate(startDate);
				
				try {
					Set<String> list = (Set<String>) redisClientTemplate.hkeys("MTMY_ID");
					logger.info("本次同步账号数量："+list.size()+"个");
					Iterator<String> it = list.iterator();  
					while (it.hasNext()) {  
					  String mtmy_id = it.next();  
					  String integral = redisClientTemplate.get(MTMY_ID+""+mtmy_id);
					  entryService.SyncIntegrals(mtmy_id,integral);
					} 
				}catch (Exception e) {
					logger.error("#####【定时任务trains.SyncIntegrals】妃子校云币同步,出现异常，异常信息为："+e.getMessage());
					taskLog.setStatus(1);
					taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
				}finally {
					endDate = new Date();//结束时间
					runTime = (endDate.getTime() - startDate.getTime());//运行时间
					taskLog.setEndDate(new Date());	//结束时间
					taskLog.setRunTime(runTime);
					taskService.saveTaskLog(taskLog);
				}
				
				logger.info("[work0],end,妃子校云币同步,结束时间："+df.format(new Date()));
				}
		
	
}
