package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.training.common.utils.BeanUtil;
import com.training.common.utils.IdGen;
import com.training.modules.ec.entity.MtmyCheckAccount;
import com.training.modules.ec.service.CheckAccountService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;

/**
 * 微信对账
 *
 */
@Component
public class WeixinCheckAccount extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String downloadAccount = "https://api.mch.weixin.qq.com/pay/downloadbill"; // 对账接口
	public static final String appid = "wxa3d2b87d794df255"; // APP应用ID
	public static final String mch_id = "1364131902"; // 商户号
	public static final String bill_type = "SUCCESS"; // 账单类型
	public static final String key = "Dengyun1364131902Xiaomomo1988071"; // 账单类型
	
	private static CheckAccountService checkAccountService;
	static{
		checkAccountService = (CheckAccountService) BeanUtil.getBean("checkAccountService");
	}
	/**
	 * 对账
	 */
	public void weixinCheckAccount(){
		logger.info("[work0],start,微信对账，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("weixinCheckAccount");
		taskLog.setStartDate(startDate);
		
		try {
			List<MtmyCheckAccount> list = new ArrayList<MtmyCheckAccount>();
			
			String string = getXmlInfo(); 
			String result = webUtil(string,downloadAccount);
			
			String str = result;// 获取对账报文
	        String newStr = str.replaceAll(",", " "); // 去空格
	        String[] tempStr = newStr.split("`"); // 数据分组
	        String[] t = tempStr[0].split(" ");// 分组标题
	        int k = 1; // 纪录数组下标
	        int j = tempStr.length / t.length; // 计算循环次数
	        for (int i = 0; i < j; i++) {
	        	MtmyCheckAccount mtmyCheckAccount = new MtmyCheckAccount();
	            for (int l = 0; l < t.length; l++) {
	            	//如果是最后列且是最后一行数据时，去除数据里的汉字
	            	if((i == j - 1) && (l == t.length -1)){
	                	String reg = "[\u4e00-\u9fa5]";//汉字的正则表达式
	                	Pattern pat = Pattern.compile(reg);  
	                	Matcher mat=pat.matcher(tempStr[l + k]); 
	                	String repickStr = mat.replaceAll("");
	            		System.out.println(t[l] + ":" + repickStr);
	            	}else{
	            		if("微信订单号".equals(t[l])){
	        				mtmyCheckAccount.setPingId(tempStr[l + k]);
	        			}
	        			if("商户订单号".equals(t[l])){
	        				mtmyCheckAccount.setOrderNo(tempStr[l + k]);
	        			}
	        			if(t[l].indexOf("交易时间") != -1){
	        				mtmyCheckAccount.setPayDate(df.parse(tempStr[l + k]));
	        			}
	        			if("总金额".equals(t[l])){
	        				mtmyCheckAccount.setPayAmount(tempStr[l + k]);
	        			}
	        			if("商品名称".equals(t[l])){
	        				mtmyCheckAccount.setPayRemark(tempStr[l + k]);
	        			}
	            	}
	            }
	            mtmyCheckAccount.setPayChannel("微信公众号支付(定时任务)");
	            if(!"妃子校".equals(mtmyCheckAccount.getPayRemark())){
		            if(checkAccountService.findByOrderNo(mtmyCheckAccount) == 0){
		            	list.add(mtmyCheckAccount);
	    			};
	            }
	            k = k + t.length;
	        }
	        int num = 0;
	        if(list.size() > 0){
				num = checkAccountService.insterAccount(list);
			}
			taskLog.setJobDescription("[work],微信对账,成功插入数据"+num+"条");
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务weixinCheckAccount】微信对账,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,微信对账,结束时间："+df.format(new Date()));
	}
	/**
	 * 调用接口
	 * @param parpm
	 * @param url
	 * @return
	 */
	public static String webUtil(String parpm,String url){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		HttpEntity<String> formEntity = new HttpEntity<String>(parpm.toString(), headers);
		String result = restTemplate.postForObject(url, formEntity, String.class);
		
		return result;
	}
	/**
	 * 请求参数
	 * @return
	 */
	public static String getXmlInfo(){
//		String bill_date = formatDate(); 	// 对账单的日期，格式：20140603
		String bill_date = "20170526"; 	// 对账单的日期，格式：20140603
		String nonce_str = IdGen.uuid();	// 随机字符串
		String sign = createSign(bill_date,nonce_str);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>"+appid+"</appid>");
		sb.append("<bill_date>"+bill_date+"</bill_date>");
		sb.append("<bill_type>"+bill_type+"</bill_type>");
		sb.append("<mch_id>"+mch_id+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		return sb.toString();
	}
	public static String formatDate(){
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(new Date());				//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -2);  	//设置为前两天
		dBefore = calendar.getTime();   			//得到前两天的时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); //设置时间格式
		String defaultStartDate = sdf.format(dBefore);    		//格式化前两天
		return defaultStartDate;
	}
	public static String createSign(String bill_date,String nonce_str){
		String stringA="appid="+appid+"&bill_date="+bill_date+"&bill_type="+bill_type+"&mch_id="+mch_id+"&nonce_str="+nonce_str+"&key="+key; 
		return WebUtils.MD5(stringA);
	}
	public static void main(String[] args) {
		String string = getXmlInfo(); 
		String result = webUtil(string,downloadAccount);
		System.out.println(result);
	}
}
