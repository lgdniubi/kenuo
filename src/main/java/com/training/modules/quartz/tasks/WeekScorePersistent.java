package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.common.utils.DateUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.train.entity.BeautyWeekScore;
import com.training.modules.train.service.BeautyWeekScoreService;
import com.training.modules.train.service.ShopWeekScoreService;

import redis.clients.jedis.Tuple;

/**
 * 周学分统计
 * @author kele
 *
 */
@Component
public class WeekScorePersistent extends CommonService{

	private Logger logger = Logger.getLogger(WeekScorePersistent.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static BeautyWeekScoreService beautyWeekScoreService;
	private static ShopWeekScoreService shopWeekScoreService;
	
	static{
		beautyWeekScoreService = (BeautyWeekScoreService) BeanUtil.getBean("beautyWeekScoreService");
		shopWeekScoreService = (ShopWeekScoreService) BeanUtil.getBean("shopWeekScoreService");
	}
	
	/**
	 * 备份当天学分排行榜
	 */
	public void weekScorePersistent(){
		logger.info("[work0],start,[积分排行榜任务]，备份本周数据,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("weekScorePersistent");
		taskLog.setStartDate(startDate);
		
		try {
			DateUtils.YearAndWeek yaw = DateUtils.getLastWeekOfYear(-1);
			DateUtils.YearAndWeek week = DateUtils.getLastWeekOfYear(-2);
			//获取全部的区域code
			Set<String> set = redisClientTemplate.smembers(RedisConfig.office_area_ids_key);
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()){
				String aredId = iter.next();
				//获取商家下所有人的排行列表
				Set<Tuple> st = redisClientTemplate.zrangeWithScores(RedisConfig.AREA_WEEK_BEAUTIFUL_KEY+yaw+"_"+aredId, 0, -1);
				if(st.size()>0){
					int rank = st.size();
					Iterator<Tuple> it = st.iterator();
					
					while(it.hasNext()){
						Tuple t = it.next();
						//long area_rank = redisClientTemplate.zrank(RedisConfig.AREA_WEEK_BEAUTIFUL_KEY+code, t.getElement());
						//同步美容师的周数据
						BeautyWeekScore bws = new BeautyWeekScore();
						bws.setUserid(t.getElement());
						bws.setWeekscore(t.getScore());
						bws.setArearank(rank);
						
						bws.setYear(yaw.getYear());
						bws.setWeek(yaw.getWeek());
						bws.setOfficeid(redisClientTemplate.hget(RedisConfig.OFFICE_IDS_KEY, t.getElement()));
						bws.setAreaid(Integer.parseInt(aredId));
						beautyWeekScoreService.insertWeekScore(bws);
						
						redisClientTemplate.zadd(RedisConfig.AREA_WEEK_BEAUTIFUL_KEY+yaw+"_"+aredId, 0, t.getElement());
						rank--;
						//officeWeekScoreService.statisOfficeWeekScoreByOfficeCode(BeanUtil.getOfficeCode( t.getElement()), t.getScore());
					}
					//统计店铺周数据
					logger.info("year="+DateUtils.getYears()+",week="+DateUtils.getWeekOfYear()+",area_id="+aredId);
					Map<String,Object> mapWeek = new HashMap<String,Object>();

					mapWeek.put("year", yaw.getYear());
					mapWeek.put("week", yaw.getWeek());
					mapWeek.put("area_id", aredId);
					List<BeautyWeekScore> list = beautyWeekScoreService.queryWeekScoreByGroupByOfficeCode(mapWeek);
					for(int i=1;i<=list.size();i++){
						Map<String,Object> officeMap = new HashMap<String,Object>();
						officeMap.put("year", yaw.getYear());
						officeMap.put("week", yaw.getWeek());
						officeMap.put("week_avg_score", list.get(i-1).getWeekscore());
						officeMap.put("area_id", list.get(i-1).getAreaid());
						officeMap.put("area_rank",i);
						officeMap.put("office_id", list.get(i-1).getOfficeid());
						shopWeekScoreService.insertOfficeWeekScore(officeMap);
					}
					//清除美容师 缓存数据
					//this.redisClientTemplate.del(AREA_WEEK_BEAUTIFUL_KEY+code);
				}
				//删除上上周的数据
				redisClientTemplate.del(RedisConfig.AREA_WEEK_BEAUTIFUL_KEY+week+"_"+aredId);
			}
			
			
			taskLog.setJobDescription("[work],[积分排行榜任务]，备份本周数据,个数："+set.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务weekScorePersistent】积分排行榜任务,备份本周数据,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,[积分排行榜任务]，备份本周数据,结束时间："+df.format(new Date()));
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtils.getYears());
		System.out.println(DateUtils.getWeekOfYear());
	}
}
