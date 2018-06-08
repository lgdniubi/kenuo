package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.mapper.JsonMapper;
import com.training.common.utils.BeanUtil;
import com.training.modules.ec.dao.MtmyOaNotifyDao;
import com.training.modules.ec.entity.MtmyOaNotify;
import com.training.modules.ec.service.MtmyOaNotifyService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;

/**
 * 定时推送每天美耶电商通告
 * @author xiaoye  2018年6月7日
 *
 */
@Component
public class AutoPushMtmyOaNotify extends CommonService{

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	
	private static MtmyOaNotifyDao mtmyOaNotifyDao;
	private static MtmyOaNotifyService mtmyOaNotifyService;
	
	static{
		mtmyOaNotifyDao = (MtmyOaNotifyDao) BeanUtil.getBean("mtmyOaNotifyDao");
		mtmyOaNotifyService = (MtmyOaNotifyService)BeanUtil.getBean("mtmyOaNotifyService");
	}
	
	public void autoPushMtmyOaNotify(){
		
		logger.info("[work0],start,定时推送每天美耶电商通告,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("shareAdvancePrice");
		taskLog.setStartDate(startDate);
		
		try {
			List<MtmyOaNotify> oaNotifyIdList = mtmyOaNotifyDao.queryAutoPushMessage();
			if(oaNotifyIdList.size() > 0){
				for(MtmyOaNotify mtmyOaNotify:oaNotifyIdList){
					Map<String, Object> map = (Map) JsonMapper.fromJsonString(this.mtmyOaNotifyService.pushMsg(mtmyOaNotify.getId(), mtmyOaNotify.getPushType()), Map.class);
					System.out.println(map.get("result"));
					if("200".equals(map.get("result"))){
						mtmyOaNotifyService.updateStatus(mtmyOaNotify.getId(), 1);
					}
				}
			}
			
		}catch(Exception e){
			logger.error("#####【定时任务autoPushMtmyOaNotify】定时推送每天美耶电商通告,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,定时推送每天美耶电商通告,结束时间："+df.format(new Date()));
	}
}
