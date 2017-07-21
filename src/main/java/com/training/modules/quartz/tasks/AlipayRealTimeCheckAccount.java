package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.MtmyCheckAccount;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.CheckAccountService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.pay.config.alipay.AlipayConfig;
import com.training.modules.quartz.tasks.utils.CommonService;
/**
 * 支付宝实时对账
 * @author coffee
 * 2017上午11:50:36
 */
@Component
public class AlipayRealTimeCheckAccount extends CommonService{
	
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	
	private static CheckAccountService checkAccountService;
	static{
		checkAccountService = (CheckAccountService) BeanUtil.getBean("checkAccountService");
	}
	
	public void alipayRealTimeCheckAccount(){
		logger.info("[work0],start,支付宝实时对账，开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("alipayRealTimeCheckAccount");
		taskLog.setStartDate(startDate);
		try {
			List<MtmyCheckAccount> list = new ArrayList<MtmyCheckAccount>();	
			List<Orders>  OrdersList = checkAccountService.findOrder("支付宝");		// 查询两小时之内支付的订单
			for (int i = 0; i < OrdersList.size(); i++) {
				AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.downloadAccount,AlipayConfig.appid,AlipayConfig.your_private_key,"json",AlipayConfig.input_charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
				AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
				request.setBizContent("{" +
									  "\"out_trade_no\":\""+OrdersList.get(i).getOrderid()+"\"," +
									  "\"trade_no\":\"\"" +
									  "  }");
				AlipayTradeQueryResponse response = alipayClient.execute(request);
				if(response.isSuccess()){
					// 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
					if("TRADE_SUCCESS".equals(response.getTradeStatus())){
						MtmyCheckAccount mtmyCheckAccount = new MtmyCheckAccount();
						mtmyCheckAccount.setOrderNo(response.getOutTradeNo());
						mtmyCheckAccount.setPingId(response.getTradeNo());
						mtmyCheckAccount.setPayDate(response.getSendPayDate());
						mtmyCheckAccount.setPayAmount(response.getTotalAmount());
						mtmyCheckAccount.setPayRemark("每天美耶");
						mtmyCheckAccount.setPayChannel("支付宝移动支付(实时定时任务)");
						if(checkAccountService.findByOrderNo(mtmyCheckAccount) == 0){	// 验证是否已插入数据
							list.add(mtmyCheckAccount);
						};
					}
				} else {
					System.out.println("支付宝移动支付(实时定时任务),调用失败,失败订单号为："+OrdersList.get(i).getOrderid());
				}
			}
			int num = 0;
	        if(list.size() > 0){
				num = checkAccountService.insterAccount(list);
			}
			taskLog.setJobDescription("[work],支付宝实时对账,成功插入数据"+num+"条");
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务alipayRealTimeCheckAccount】支付宝实时对账,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[work0],end,支付宝实时对账,结束时间："+df.format(new Date()));
	}

	public static void main(String[] args) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.downloadAccount,AlipayConfig.appid,AlipayConfig.your_private_key,"json",AlipayConfig.input_charset,AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			request.setBizContent("{" +
								  "\"out_trade_no\":\"00201706181505344291004374\"," +
								  "\"trade_no\":\"\"" +
								  "  }");
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if(response.isSuccess()){
				System.out.println("调用成功");
				System.out.println("支付状态："+response.getTradeStatus());
				// 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
				if("TRADE_SUCCESS".equals(response.getTradeStatus())){
					System.out.println("商户订单号:"+response.getOutTradeNo()+",支付宝支付订单号:"+response.getTradeNo()+",支付完成时间:"+response.getSendPayDate()+",总金额:"+response.getTotalAmount());
				}
			} else {
				System.out.println("调用失败");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
