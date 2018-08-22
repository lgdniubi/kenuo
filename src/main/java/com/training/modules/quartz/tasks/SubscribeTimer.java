package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ThreadUtils;
import com.training.modules.track.core.TrackCore;
import com.training.modules.train.entity.Subscribe;
import com.training.modules.train.service.EntryService;

/**
 * 预约自动完成定时器
 *	
 */
@Component
@SuppressWarnings("all")
public class SubscribeTimer extends CommonService{

	private Logger logger = Logger.getLogger(SubscribeTimer.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static ReservationService reservationService;
	
	static{
		reservationService = (ReservationService) BeanUtil.getBean("reservationService");
	}
	
	/**
	 * regulation:	预约时间 (yyyy-MM-dd HH:mm) + 项目服务时长 (mm) + 一个小时 (mm) ---- 将未完成状态的预约改成已完成状态
	 * @author fengfeng 
	 */
	public void subscribeTimer(){
		logger.info("[work0],start,预约自动完成定时器开始执行，开始时间："+df.format(new Date()));
		HttpServletRequest request=null;
		List<Subscribe> list = new ArrayList<Subscribe>();
		int yueNum=0;
		int gouNum=0;
		int jiaNum=0;
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("SubscribeTimer");
		taskLog.setStartDate(startDate);
		
		try{
			//获取预约时间服务已过没有完成状态的预约
			list = reservationService.querySubscribelist();
			for (int i = 0; i < list.size(); i++) {
				//修改预约状态
				int num = reservationService.updateapptstatus(list.get(i).getAppt_id());
				if(num > 0){
					ThreadUtils.saveLog(request, "预约自动完成", 2, 2, list.get(i).getAppt_id());
					redisClientTemplate.hincrBy("BEAUTICIAN_SUBSCRIBE_NUM_KEY", list.get(i).getBeautician_id(), 1);	//美容师预约完成数量+1
					redisClientTemplate.hincrBy("SHOP_SUBSCRIBE_NUM_KEY", list.get(i).getShop_id(), 1);				//店铺预约完成数量+1
					
					/*##########[神策埋点-统计预约已完成的预约{submit_appoint_success}-Begin]##########*/
					TrackCore.submitAppointSuccess(list.get(i).getAppt_id());
					/*##########[神策埋点end]##########*/
				}
			}
			
		} catch (Exception e) {
			logger.error("#####【定时任务SubscribeTimer】预约自动完成定时器,出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "预约自动完成定时器", e);
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,预约自动完成定时器结束,结束时间："+df.format(new Date()));
	}
	
}
