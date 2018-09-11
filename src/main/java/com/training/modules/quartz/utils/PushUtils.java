/**
 * 
 */
package com.training.modules.quartz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.train.dao.UserCheckDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**  
* <p>Title: PushUtils.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月31日  
* @version 3.0.0 
*/
@Component
public class PushUtils {

	@Autowired
	private static UserCheckDao userCheckDao;
	
	static{
		userCheckDao = (UserCheckDao) BeanUtil.getBean("userCheckDao");
	}
	
	/**
	 * 审核成功后推送消息
	 * @param userCheck
	 * @return
	 */
	public String pushMsg(String user_id,String text,int notify_type,String title) {
		try{
			JSONArray jsonArray = new JSONArray();
			String cid = userCheckDao.findCidByUserid(user_id);
	//		jsonArray.add("35be8ac9632c9475ac67f9be3c340665");
			jsonArray.add(cid);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("cid_list", jsonArray);
			jsonObj.put("push_type", 1);
			Map<String, Object> map = new HashMap<String, Object>();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			map.put("content", text);
			map.put("notify_id",UUID.randomUUID().toString());
			map.put("notify_type", notify_type);
			map.put("push_time", dateStr);
			map.put("title", title);
			jsonObj.put("content", map);
			this.push(jsonObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	/*
	 * 推送消息具体方法
	 */
	private String push(JSONObject jsonObj) throws Exception{
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		//  用于加密
		String sign = WebUtils.MD5("train"+StringEscapeUtils.unescapeJava(jsonObj.toString())+"train");
		String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+jsonObj+"'}";
		
		HttpEntity<String> entity = new HttpEntity<String>(paramter,headers);
		
		String push_url = ParametersFactory.getMtmyParamValues("push_url");
//		String push_url = "http://10.10.8.22:8801/appServer/pushMsg/pushGT.do";
		
		String json = restTemplate.postForObject(push_url, entity, String.class);
//		System.out.println("pushjsonStr == "+jsonObj);
		System.out.println(json);
		return json;
	}
}
