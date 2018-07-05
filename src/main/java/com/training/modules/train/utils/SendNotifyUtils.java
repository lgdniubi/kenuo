package com.training.modules.train.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 
 * @Title:SendNotifyUtils.java
 * @Description: 发送通知
 * @author 兵子
 * @Date 2018年4月11日 
 *
 */
public class SendNotifyUtils {
	
	
	public static Logger logger = LoggerFactory.getLogger(SendNotifyUtils.class);

	
	/**
	 * 
	 * @Title:SendNotifyUtils.java
	 * @Description: 发送短信
	 * @author 兵子
	 * @Date 2018年4月23日 
	 *
	 */
	public static void sendSMS(String mobile,String name,String password) {
		SendSmsResponse sendSmsResponse = null;
		try {
			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");

			//初始化acsClient,暂不支持region化
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIGz2lAHp3rpKi", "pUjRNAp9IaKqrwXsGNhcSuVS9R7vIO");
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
			IAcsClient acsClient = new DefaultAcsClient(profile);

			SendSmsRequest request = new SendSmsRequest();	//组装请求对象-具体描述见控制台-文档部分内容
			request.setPhoneNumbers(mobile);				//必填:待发送手机号
			request.setSignName("妃子校");				//必填:短信签名-可在短信控制台中找到
			request.setTemplateCode("SMS_135032100");		//必填:短信模板-可在短信控制台中找到

			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"a\":\"" + name + "\",\"b\":\"" + password + "\"}");
			sendSmsResponse = acsClient.getAcsResponse(request);

			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {	//请求成功
				logger.info("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请成功，返回结果->" + sendSmsResponse.getMessage());
			}else{
				logger.error("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，返回结果->" + sendSmsResponse.getMessage());
			}
		} catch (ServerException e) {
			logger.error("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，原因为 ==" + e.getErrMsg());
		} catch (ClientException e) {
			logger.info("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，原因为 ==" + e.getErrMsg());
		}
	}
	
	/**
	 * 发送验证码
	 * @param mobile
	 * @param name
	 */
	public static void sendCodeSMS(String mobile,String name,String code) {
		SendSmsResponse sendSmsResponse = null;
		try {
			//可自助调整超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");

			//初始化acsClient,暂不支持region化
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIGz2lAHp3rpKi", "pUjRNAp9IaKqrwXsGNhcSuVS9R7vIO");
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
			IAcsClient acsClient = new DefaultAcsClient(profile);

			SendSmsRequest request = new SendSmsRequest();	//组装请求对象-具体描述见控制台-文档部分内容
			request.setPhoneNumbers(mobile);				//必填:待发送手机号
			request.setSignName("妃子校");				//必填:短信签名-可在短信控制台中找到
			request.setTemplateCode("SMS_70320230");		//必填:短信模板-可在短信控制台中找到

			//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			request.setTemplateParam("{\"a\":\"" + code + "\",\"b\":\"" + 1 + "\"}");
			sendSmsResponse = acsClient.getAcsResponse(request);

			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {	//请求成功
				logger.info("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请成功，返回结果->" + sendSmsResponse.getMessage());
			}else{
				logger.error("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，返回结果->" + sendSmsResponse.getMessage());
			}
		} catch (ServerException e) {
			logger.error("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，原因为 ==" + e.getErrMsg());
		} catch (ClientException e) {
			logger.info("向手机号为[" + mobile + "]姓名为[" + name + "]发送邀请失败，原因为 ==" + e.getErrMsg());
		}
	}
	
	
}
