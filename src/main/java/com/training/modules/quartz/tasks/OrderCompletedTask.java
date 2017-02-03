package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.ParametersFactory;

/**
 * 订单已完成状态修改-定时器
 * @author kele
 * @version 2017-1-4 11:47:37
 */
@Component
public class OrderCompletedTask extends CommonService{
	
	public static final String ORDER_COMPLETED_TIME = "ORDER_COMPLETED_TIME";//订单已完成时间系统参数
	private Logger logger = Logger.getLogger(OrderCompletedTask.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static OrdersService ordersService;
	static{
		ordersService = (OrdersService) BeanUtil.getBean("ordersService");
	}
	
	/**
	 * 普通订单过期定时任务
	 */
	public void orderCompleted(){
		logger.info("[work0],start,确认收货订单,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("orderCompleted");
		taskLog.setStartDate(startDate);
		
		try {
			//每天美耶
			int completedDate = Integer.parseInt(ParametersFactory.getMtmyParamValues("ORDER_COMPLETED_TIME")); 
			logger.info("[确认收货订单]，确认收货时间为："+completedDate);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("completedDate", completedDate);
			int uResult = ordersService.updateOrderFinished(map);
			logger.info("[过期普通订单]，扫面过期订单开始，订单个数："+uResult);
			taskLog.setJobDescription("[确认收货订单]，确认收货订时间为："+completedDate+"   扫面过期订单开始，订单个数："+uResult);
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务orderCompleted】订单已完成状态修改出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,确认收货订单,结束时间："+df.format(new Date()));
	}
	
	public static void main(String[] args) {
		OrderCompletedTask task = new OrderCompletedTask();
		task.orderCompleted();
	}
}
