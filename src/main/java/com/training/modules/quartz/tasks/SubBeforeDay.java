package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mtmy.common.cachemanager.vo.OneDayTime;
import com.mtmy.common.cachemanager.vo.SaveModel;
import com.mtmy.common.cachemanager.vo.Time;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.TaskUtils;

/**
 * 美容师预约
 * @author kele
 *
 */
@Component
public class SubBeforeDay extends CommonService{
	
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void delSubBeforeDay(){
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("delSubBeforeDay");
		taskLog.setStartDate(startDate);
		
		logger.info("[work],开始更新美容师预约时间,开始时间："+df.format(new Date()));
		try {
			
			List<String> beauty_ids = redisClientTemplate.lrange(RedisConfig.BEAUTY_ID_HASH, 0, -1);
			
			logger.info("[work],更新预约时间0,需要更新时间美容师个数："+beauty_ids.size());
			
			for(String bid : beauty_ids){
				logger.info("[work],更新预约时间1,美容师id："+bid);
				SaveModel sm = (SaveModel) TaskUtils.deserialize(redisClientTemplate.get(TaskUtils.serialize(RedisConfig.SUBSCRIBETIME_PREFIX+bid)));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E",Locale.CHINA);
				
				Map<String, Integer> indexS = sm.getIndex();
				LinkedList<OneDayTime> odts = sm.getOdts();
				
				OneDayTime temp = odts.getFirst(); //第一天的数据
				//String k = temp.getKey();
				indexS.remove(temp.getKey()); //删除SaveModel过期的key
				odts.poll();//删除SaveModel过期的OneDayTime
				
				//更新indexS索引
				Set<String> s = indexS.keySet();
				Iterator<String> it = s.iterator();
				while(it.hasNext()){
					String mk = it.next();
					indexS.put(mk, indexS.get(mk)-1);
				}
				
				//生成新的一天的数据
				SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
				OneDayTime odt = new OneDayTime();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, 2);
				String kk = sdf.format(calendar.getTime());
				String kkk = sdf0.format(new Date());
				odt.setKey(kk);
				//新一天索引
				indexS.put(kk, 2);
				
				List<Time> times = new ArrayList<Time>(); //数据
				Map<String, Integer> indexT = new HashMap<String, Integer>();//索引
				String timepoint[] = (String[]) TaskUtils.deserialize(redisClientTemplate.hget(TaskUtils.serialize(RedisConfig.SUBSCRIBETIMETP_KEY), TaskUtils.serialize(bid))); 
				logger.info("[work],更新预约时间2,生成时间点列表："+timepoint.length);
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				for (int j = 0; j < timepoint.length; j++) {
					Time t = new Time();
					t.setName( timepoint[j]);
					//logger.info("[work],更新预约时间3,生成时间点："+timepoint[j]+"，生成时间："+kkk+" "+timepoint[j]);
					t.setTime(sd.parse(kkk+" "+timepoint[j]));
					if(j == timepoint.length-1 || j == timepoint.length -2){
						t.setIs_use(1);
					}
					times.add(t);
					indexT.put( timepoint[j], j);//生成索引
				}
				odt.setIndex(indexT);
				odt.setTimes(times);
				
				odts.addLast(odt);
				sm.setIndex(indexS);
				sm.setOdts(odts);
				
				redisClientTemplate.set(TaskUtils.serialize(RedisConfig.SUBSCRIBETIME_PREFIX+bid), TaskUtils.serialize(sm));
			}
			
			taskLog.setJobDescription("[work],更新预约时间0,需要更新时间美容师个数："+beauty_ids.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务delSubBeforeDay】更新美容师预约时间,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work],结束更新美容师预约时间,结束时间："+df.format(new Date()));
	}
}
