package com.training.modules.quartz.tasks;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.utils.CreateOrderPush;
import com.training.modules.quartz.utils.PushUtils;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.entity.ArrearageOfficeList;
import com.training.modules.train.service.EntryService;
import com.training.modules.train.service.RefundOrderService;
import com.training.modules.train.service.TrainLiveAuditService;

import net.sf.json.JSONObject;

/**  
* <p>Title: CreateRefundOrder.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/
@Component
@SuppressWarnings("all")
public class CreateRefundOrder extends CommonService{

	private Logger logger = Logger.getLogger(CreateRefundOrder.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static RefundOrderService refundOrderService;
	
	static{
		refundOrderService = (RefundOrderService) BeanUtil.getBean("refundOrderService");
	}
	
	/**
	 * 创建信用额度还款订单
	 * @author yangyang 
	 */
	public void createRefundOrder(){
	
		logger.info("[work0],start,创建信用额度还款订单，开始时间："+df.format(new Date()));
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("createRefundOrder");
		taskLog.setStartDate(startDate);
		
		try{
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM");
			 Calendar calendar = Calendar.getInstance();//日历对象  
//		     calendar.setTime(new Date());//设置当前日期  
//		     String time = sim.format(calendar.getTime());//输出本月的日期  
//		     calendar.add(Calendar.MONTH, -1);//月份减一  
//		     String times = sim.format(calendar.getTime());//输出上个月的日期  
			 
		     calendar.setTime(new Date());//设置当前日期  
		     String format = sim.format(calendar.getTime());//输出本月的日期  
		     calendar.add(Calendar.MONTH, -1);//月份减一  
		     String formats = sim.format(calendar.getTime());//输出上个月的日期  
		     
//		     SimpleDateFormat sims = new SimpleDateFormat("yyyy-MM");
//		     calendar.add(Calendar.MONTH, -1);//月份减一  
//		     String times = sims.format(calendar.getTime());//输出上个月的日期  
		     
		List<ArrearageOfficeList> arrearageOfficeList = refundOrderService.queryarrearageoffice(format,formats);
		int count = arrearageOfficeList.size() / 100;
		if((arrearageOfficeList.size() % 100) > 0)count ++;
		//List<ArrearageOfficeList> list = new ArrayList<ArrearageOfficeList>();
		String weburl = ParametersFactory.getTrainsParamValues("queryContractInfo");
		for(int i = 0; i < count ; i++){
			List<ArrearageOfficeList> subList = arrearageOfficeList.subList(i * 100, i == count -1 ? arrearageOfficeList.size() : (i+1) * 100);
			List<ArrearageOfficeList> list = new ArrayList<ArrearageOfficeList>(subList);
			int f = 0;
			for(int j = 0 ; j < subList.size() ; j++){
				list.get(j-f).setOrder_id(createRefundOrderid(j+""));
				list.get(j-f).setBillmonth(formats);
				double used_limit = subList.get(j).getUsed_limit();
				if(used_limit < 0 || used_limit == 0){
					list.remove(j);
					f++;
				}
			} 
			refundOrderService.addrefundOrder(list);
			
			try{
				CreateOrderPush createorderpush = new CreateOrderPush(list, weburl, formats);
				new Thread(createorderpush).start();
			}catch(Exception e){
				int k = 0;
				logger.info("#####【定时任务createRefundOrder】创建订单还款通知线程["+(k++)+"],出现异常，异常信息为："+e.getMessage());
				e.printStackTrace();
			}
		}
		
		}catch (Exception e) {
			logger.error("#####【定时任务createRefundOrder】创建信用额度还款订单,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,创建信用额度还款订单,结束时间："+df.format(new Date()));
		
	}
	
	/**
	 * 订单ID（26位）=临时订单标识（1）+yyyyMMDDHHMMSSsss(17)+8位随机大小写字母（11位）
	 * @param 0开头:正式订单号，1开头：临时订单号
	 * @return
	 */
	public static String createRefundOrderid(String num){
		if(num == null)num = "";
		StringBuffer sb = new StringBuffer();
		sb.append(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sb.append(sdf.format(new Date()));
		sb.append(num);
		Random r = new Random(); 
	    String code = ""; 
	    for (int i = 0; i < (8 - num.length()); ++i) { 
	        int temp = r.nextInt(52); 
	        char x = (char) (temp < 26 ? temp + 97 : (temp % 26) + 65); 
	        code += x; 
	    } 
		sb.append(code);
		return sb.toString();
	}
	
	
}
