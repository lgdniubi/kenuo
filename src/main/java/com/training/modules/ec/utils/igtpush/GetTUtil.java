package com.training.modules.ec.utils.igtpush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.training.common.config.Global;
import com.training.common.utils.BeanUtil;
import com.training.modules.ec.service.MtmyOaNotifyService;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.ec.utils.igtpush.exception.SysConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 消息推送工具类
 * @author QJL
 *
 */
public class GetTUtil {
	
	private static final Log log = LogFactory.getLog(GetTUtil.class);
	
	public static final String APPID;
	
	public static final String APPKEY;
	
	public static final String MASTER;
	
	public static final String HOST;
	
	static{
		//获取个推系统配置
		APPID = Global.getConfig("appid");
		APPKEY = Global.getConfig("appkey");
		MASTER = Global.getConfig("master");
		HOST = Global.getConfig("host");
	}
	
	/**
	 * 个人消息推送  
	 * @throws ResponseError 
	 */
	public static String personal(JSONObject jsonObj) throws ResponseError{
		
		if(StringUtils.isBlank((String)jsonObj.get("client_id"))){
			throw new ResponseError(SysConstants.ERROR_CID_MESSAGE,SysConstants.ERROR_CID_RESULT);
		}
		
		IGtPush push = new IGtPush(HOST,APPKEY, MASTER);
		
		TransmissionTemplate template = getTransmissionTemplate(jsonObj);
		
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(2 * 1000 * 3600);
		message.setData(template);
		
		Target target = new Target();
		target.setAppId(APPID);
		target.setClientId((String)jsonObj.get("client_id"));
		
		IPushResult ret = push.pushMessageToSingle(message, target);
		
		log.info("个推返回结果:"+ret.getResponse().toString());
		
		if(!"ok".equalsIgnoreCase((String)ret.getResponse().get("result"))){
			throw new ResponseError((String)ret.getResponse().get("result"), SysConstants.ERROR_PUSH_RESULT);
		}else{
			persistentContentId(jsonObj,(String)ret.getResponse().get("taskId"));
			
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.SUCCESS_RESULT);
			json.put("message", SysConstants.SUCCESS_MESSAGE);
			return json.toString();
		}
	}
	
	/**
	 * 列表推送
	 * @throws ResponseError 
	 */
	public static String list(JSONObject jsonObj) throws ResponseError{
		JSONArray arr = jsonObj.getJSONArray("cid_list");
		if(arr == null || arr.size() == 0){
			throw new ResponseError(SysConstants.ERROR_CIDS_MESSAGE, SysConstants.ERROR_CIDS_RESULT);
		}
		
		IGtPush push = new IGtPush(HOST,APPKEY, MASTER);
		TransmissionTemplate template = getTransmissionTemplate(jsonObj);
		
		ListMessage message = new ListMessage();
		message.setData(template);
		
		message.setOffline(true);
		message.setOfflineExpireTime(1000 * 3600 * 24);
		
		List<Target> targets = new ArrayList<Target>();
		for(Object o : arr){
			Target target = new Target();
			target.setAppId(APPID);
			target.setClientId((String)o);
			targets.add(target);
		}
		
		String taskId = push.getContentId(message);
		IPushResult ret = push.pushMessageToList(taskId, targets);
		
		
		log.info("任务id："+taskId+"列表推送结果："+ret.getResponse().toString());
		
		if(!"ok".equals(ret.getResponse().get("result"))){
			throw new ResponseError((String)ret.getResponse().get("result"), SysConstants.ERROR_PUSH_RESULT);
		}else{
			persistentContentId(jsonObj,taskId);
			
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.SUCCESS_RESULT);
			json.put("message", SysConstants.SUCCESS_MESSAGE);
			return json.toString();
		}
	}
	
	/**
	 * 群推送
	 * @throws ResponseError 
	 */
	public static String crowd(JSONObject jsonObj) throws ResponseError{
		
		IGtPush push = new IGtPush(HOST,APPKEY, MASTER);
		
		TransmissionTemplate template = getTransmissionTemplate(jsonObj);
		AppMessage message = new AppMessage();
		message.setData(template);
		message.setOffline(true);
		message.setOfflineExpireTime(24 * 1000 * 3600);
		
		
		List<String> appIdList = new ArrayList<String>();
		List<String> phoneTypeList = new ArrayList<String>();
		List<String> provinceList = new ArrayList<String>();
		List<String> tagList = new ArrayList<String>();
		
		appIdList.add(APPID);
		//phoneTypeList.add("ANDROID");
		//provinceList.add("");
		//tagList.add("hello");
		
		message.setAppIdList(appIdList);
//		message.setPhoneTypeList(phoneTypeList);
//		message.setProvinceList(provinceList);
//		message.setTagList(tagList);
//		message.setPushNetWorkType(1);
//		message.setSpeed(1000);
		//设置省市平台tag的新方式
		AppConditions cdt = new AppConditions(); 		
		cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
		cdt.addCondition(AppConditions.REGION, provinceList);
		cdt.addCondition(AppConditions.TAG,tagList);
		message.setConditions(cdt);	
		
		IPushResult ret = push.pushMessageToApp(message);
		log.info("群推返回结果："+ret.getResponse().toString());
		if(!"ok".equals(ret.getResponse().get("result"))){
			throw new ResponseError((String)ret.getResponse().get("result"), SysConstants.ERROR_PUSH_RESULT);
		}else{
			persistentContentId(jsonObj,(String)ret.getResponse().get("contentId"));
			
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.SUCCESS_RESULT);
			json.put("message", SysConstants.SUCCESS_MESSAGE);
			return json.toString();
		}
		
	}
	
	//透析模板
	public static TransmissionTemplate getTransmissionTemplate(JSONObject jsonO) throws ResponseError{
		
		JSONObject json = (JSONObject) jsonO.get("content");
		if(json == null){
			throw new ResponseError(SysConstants.ERROR_CONTENT_MESSAGE,SysConstants.ERROR_CONTENT_RESULT);
		}
		
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(APPID);
		template.setAppkey(APPKEY);
		template.setTransmissionType(2);
		template.setTransmissionContent(json.toString());
		
		APNPayload apnpayload = new APNPayload();
		apnpayload.setContentAvailable(1);
		//apnpayload.setSound("test2.wav");
		apnpayload.setSound("kenuoPika.wav");	//推送声音配置
		//apnpayload.setCategory("ACTIONABLE");
		apnpayload.addCustomMsg("content", json.toString());
		apnpayload.setBadge(1);
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		
		alertMsg.setBody((String)json.get("title"));
		// IOS8.2及以上版本支持
		// IOS10以上，标题重复问题
		//alertMsg.setTitle((String)json.get("title"));
		
		apnpayload.setAlertMsg(alertMsg);
		template.setAPNInfo(apnpayload);
		return template;
	}
	
	//保存任务id以便后续获取推送结果
	public static void persistentContentId(JSONObject jsonO,String taskId){
		JSONObject json = (JSONObject) jsonO.get("content");
		
		MtmyOaNotifyService service = (MtmyOaNotifyService) BeanUtil.getBean("mtmyOaNotifyService");
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("content_id", taskId);
		map.put("notify_id", json.get("notify_id"));
		service.updateNotifyContentId(map);
	}
	
	/**
	 * 获得推送结果
	 */
	public static String getPushResult(JSONObject jsonObj) throws ResponseError{
		
		if(jsonObj == null || StringUtils.isBlank((String)jsonObj.get("content_id"))
				|| StringUtils.isBlank((String)jsonObj.get("notify_id"))){
			throw new ResponseError(SysConstants.ERROR_PARAM_MESSAGE, SysConstants.ERROR_PARAM_RESULT);
		}
	
		MtmyOaNotifyService service = (MtmyOaNotifyService) BeanUtil.getBean("mtmyOaNotifyService");
		
		IGtPush push = new IGtPush(GetTUtil.HOST,GetTUtil.APPKEY, GetTUtil.MASTER);
		IPushResult ret = push.getPushResult((String)jsonObj.get("content_id"));
		log.info("content_id:"+(String)jsonObj.get("content_id")+"获取推送结果："+ret.getResponse().toString());
		if(!"ok".equals(ret.getResponse().get("result"))){
			throw new ResponseError((String)ret.getResponse().get("result"), SysConstants.ERROR_PUSH_RESULT);
		}else{
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("notify_id", jsonObj.get("notify_id"));
			map.put("click_num",ret.getResponse().get("clickNum"));
			map.put("msg_process",ret.getResponse().get("msgProcess"));
			map.put("msg_total",ret.getResponse().get("msgTotal"));
			map.put("push_num",ret.getResponse().get("pushNum"));
			map.put("result",ret.getResponse().get("result"));
			map.put("status","2");//修改状态2：获取推送结果
			service.updatePushResult(map);
			
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.SUCCESS_RESULT);
			json.put("message", SysConstants.SUCCESS_MESSAGE);
			return json.toString();
		}
	}
	
	public static void main(String[] args) {
		/*IGtPush push = new IGtPush(HOST,APPKEY, MASTER);
		IPushResult pushResult = push.getPushResult("OSL-0326_MME52zUPfs7c3O6W1kNaM4");
        System.out.println(pushResult.getResponse().toString());*/
	}
}
