package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.common.utils.DateUtils;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.entity.CourierResultXML;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.utils.CourierUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 修改订单签收日期
 * @author coffee
 * @date 2018年5月21日
 */
@Component
public class OrdersCourier extends CommonService{
	
	private Logger logger = Logger.getLogger(OrdersCourier.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static OrdersDao ordersDao;
	static{
		ordersDao = (OrdersDao) BeanUtil.getBean("ordersDao");
	}
	
	/**
	 * 订单签收日期
	 */
	public void ordersCourier(){
		logger.info("[ordersCourier],start,修改订单签收日期,开始时间："+df.format(new Date()));
		HttpServletRequest request=null;
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("ordersCourier");
		taskLog.setStartDate(startDate);
		
		int num = 0;	// 已签收订单个数
		int num1 = 0;	// 实际执行操作订单个数
		try {
			List<Orders> ordersList = ordersDao.queryCourierOrder();
			int ordersListNum = ordersList.size();	// 扫描订单个数
			for (int i = 0; i < ordersList.size(); i++) {
				String xmlString = CourierUtils.findCourierPost(ordersList.get(i).getShippingcode());	// 请求圆通快递接口
				if(xmlString.contains("已签收")){	// 存在用户签收状态时
					List<CourierResultXML> xmllist=CourierUtils.readStringXmlOut(xmlString);
					for (int j = 0; j < xmllist.size(); j++) {
						if(xmllist.get(j).getProcessInfo().contains("已签收")){		// 用户已签收
							num = num + 1;
							Orders orders = new Orders();
							orders.setShippingReceivedDate(DateUtils.parseDate(xmllist.get(j).getUploadTime()));
							orders.setOrderid(ordersList.get(i).getOrderid());
							int updateStatus = ordersDao.updateCourierOrder(orders);
							if(updateStatus > 0){num1 = num1 + 1;};
							break;
						}
					}
				}
			}
			System.out.println("修改订单签收日期开始，查询未完成订单个数：["+ordersListNum+"]"+",已签收订单个数：["+num+"],实际执行个数：["+num1+"]");
			taskLog.setJobDescription("修改订单签收日期开始，查询未完成订单个数：["+ordersListNum+"]"+",已签收订单个数：["+num+"],实际执行个数：["+num1+"]");
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务ordersCourier】修改订单签收日期,出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "修改订单签收日期", e);
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[ordersCourier],end,修改订单签收日期,结束时间："+df.format(new Date()));
	}
}
