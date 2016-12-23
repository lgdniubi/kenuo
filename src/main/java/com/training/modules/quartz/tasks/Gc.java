package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.modules.ec.entity.CouponAcmountMapping;
import com.training.modules.ec.service.CouponService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
/**
 * 垃圾数据回收
 * @author QJL
 *
 */
@Component
public class Gc extends CommonService {
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private CouponService couponService;
	/**
	 * 定时任务清理
	 */
	public void gc(){
		logger.info("[work0],start,[任务]，->清除垃圾数据,开始时间："+df.format(new Date()));
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("gc data");
		taskLog.setStartDate(startDate);
		
		try {
			
			List<CouponAcmountMapping> cams = this.couponService.timeOutAmountList();
			logger.info("#####【定时任务gc data】,清理缓存过期红包面值冗余数据数量："+cams.size());
			Set<Integer> couponids = new HashSet<Integer>();
			for(CouponAcmountMapping cam : cams){
				redisClientTemplate.del(RedisConfig.coupon_store_prefix+cam.getAmountId());
				redisClientTemplate.hdel(RedisConfig.coupon_amountids_hash, cam.getAmountId()+"");
				couponids.add(cam.getCouponId());
			}
			
			Iterator<Integer> iter = couponids.iterator();
			while(iter.hasNext()){
				couponService.updateTimeOStatus(iter.next());
			}
			
			//保存日志 
			taskLog.setJobDescription("[work],清理缓存过期红包面值冗余数据数量："+cams.size()+",累计 时间： "+df.format(new Date()));
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务gc data】垃圾数据回收,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,[任务]，->垃圾数据回收,结束时间："+df.format(new Date()));
		
	}
	/**
	 * 手动清理
	 * @param amount_id
	 */
	public void gc(int amount_id){
		redisClientTemplate.del(RedisConfig.coupon_store_prefix+amount_id);
		redisClientTemplate.hdel(RedisConfig.coupon_amountids_hash, amount_id +"");
	}
}
