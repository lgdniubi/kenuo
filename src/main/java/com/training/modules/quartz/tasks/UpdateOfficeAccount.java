package com.training.modules.quartz.tasks;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ArrearageOfficeList;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.RefundOrderService;
import com.training.modules.train.service.TrainLiveAuditService;

/**  
* <p>Title: CreateRefundOrder.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/
@Component
@SuppressWarnings("all")
public class UpdateOfficeAccount extends CommonService{

	private Logger logger = Logger.getLogger(UpdateOfficeAccount.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static RefundOrderService refundOrderService;
	
	static{
		refundOrderService = (RefundOrderService) BeanUtil.getBean("refundOrderService");
	}
	
	/**
	 * 冻结信用额度
	 * @author yangyang 
	 */
	public void updateOfficeAccount(){
	
		logger.info("[work0],start,冻结信用额度，开始时间："+df.format(new Date()));
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("updateOfficeAccount");
		taskLog.setStartDate(startDate);
		
		try{
			//冻结额度账户
			refundOrderService.updateOfficeAccount();
			//将订单改为逾期状态
			refundOrderService.updateOrderOverdueStatus();
		
		}catch (Exception e) {
			logger.error("#####【定时任务updateOfficeAccount】冻结信用额度,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,冻结信用额度,结束时间："+df.format(new Date()));
		
	}

	
}
