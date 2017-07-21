package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.training.common.utils.BeanUtil;
import com.training.common.utils.IdGen;
import com.training.modules.ec.entity.MtmyCheckAccount;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.CheckAccountService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.pay.config.weixin.WeixinConfig;
import com.training.modules.quartz.tasks.pay.config.weixin.util.XMLUtil;
import com.training.modules.quartz.tasks.utils.CommonService;

/**
 * 微信实时对账
 * @author coffee
 * 2017上午9:50:00
 */
@Component
public class WeixinRealTimeCheckAccount extends CommonService{

	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	private static DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");	
	
	private static CheckAccountService checkAccountService;
	static{
		checkAccountService = (CheckAccountService) BeanUtil.getBean("checkAccountService");
	}
	
	public void weixinRealTimeCheckAccount(){
		logger.info("[work0],start,微信实时对账，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("WeixinRealTimeCheckAccount");
		taskLog.setStartDate(startDate);
		try {
			List<MtmyCheckAccount> list = new ArrayList<MtmyCheckAccount>();	
			List<Orders>  OrdersList = checkAccountService.findOrder("微信");		// 查询两小时之内支付的订单
			for (int i = 0; i < OrdersList.size(); i++) {
				String string = getXmlInfo(OrdersList.get(i).getOrderid()); // 请求参数
				String result = webUtil(string,WeixinConfig.orderquery);	// 请求接口
				Map<String, String> map = XMLUtil.doXMLParse(result);		// 解析xml(接口返回的xml)
				// return_code SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断    
				// result_code SUCCESS/FAIL 业务结果 
				// trade_state 交易状态  SUCCESS—支付成功    REFUND—转入退款 	NOTPAY—未支付 	CLOSED—已关闭 	REVOKED—已撤销（刷卡支付） 	USERPAYING--用户支付中	PAYERROR--支付失败(其他原因，如银行返回失败)
				if(map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("trade_state").equals("SUCCESS")){
					MtmyCheckAccount mtmyCheckAccount = new MtmyCheckAccount();
					mtmyCheckAccount.setOrderNo(map.get("out_trade_no"));
					mtmyCheckAccount.setPingId(map.get("transaction_id"));
					mtmyCheckAccount.setPayDate(df1.parse(map.get("time_end")));
					mtmyCheckAccount.setPayAmount(String.valueOf(Double.valueOf(map.get("total_fee")) / 100));
					mtmyCheckAccount.setPayRemark("每天美耶");
					mtmyCheckAccount.setPayChannel("微信公众号支付(实时定时任务)");
					if(checkAccountService.findByOrderNo(mtmyCheckAccount) == 0){	// 验证是否已插入数据
						list.add(mtmyCheckAccount);
					};
				}
			}
			int num = 0;
	        if(list.size() > 0){
				num = checkAccountService.insterAccount(list);
			}
			taskLog.setJobDescription("[work],微信实时对账,成功插入数据"+num+"条");
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务WeixinRealTimeCheckAccount】微信实时对账,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,微信实时对账,结束时间："+df.format(new Date()));
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
	public static String getXmlInfo(String orderId){
		String nonce_str = IdGen.uuid();	// 随机字符串
		String sign = createSign(orderId,nonce_str);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>"+WeixinConfig.appid+"</appid>");
		sb.append("<mch_id>"+WeixinConfig.mch_id+"</mch_id>");
		sb.append("<nonce_str>"+nonce_str+"</nonce_str>");
		sb.append("<out_trade_no>"+orderId+"</out_trade_no>");
		sb.append("<sign>"+sign+"</sign>");
		sb.append("</xml>");
		return sb.toString();
	}
	public static String createSign(String orderId,String nonce_str){
		String stringA="appid="+WeixinConfig.appid+"&mch_id="+WeixinConfig.mch_id+"&nonce_str="+nonce_str+"&out_trade_no="+orderId+"&key="+WeixinConfig.key; 
		return WebUtils.MD5(stringA);
	}
	// 测试方法
	public static void main(String[] args) {
		try {
			String string = getXmlInfo("00201706180726114731174351"); 
			String result = webUtil(string,WeixinConfig.orderquery);
			System.out.println(result);
			Map<String, String> map = XMLUtil.doXMLParse(result);
			if(map.get("return_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS") && map.get("result_code").equals("SUCCESS")){	// SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断 
				System.out.println("商户订单号:"+map.get("out_trade_no")+",微信支付订单号:"+map.get("transaction_id")+",支付完成时间:"+df1.parse(map.get("time_end"))+",总金额:"+String.valueOf(Double.valueOf(map.get("total_fee")) / 100));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
