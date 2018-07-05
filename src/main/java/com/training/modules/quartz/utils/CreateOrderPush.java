/**
 * 
 */
package com.training.modules.quartz.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.tasks.CreateRefundOrder;
import com.training.modules.train.entity.ArrearageOfficeList;

import net.sf.json.JSONObject;

/**  
* <p>Title: CreateOrderPush.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年6月12日  
* @version 3.0.0 
*/

public class CreateOrderPush implements Runnable{
	
	private Logger logger = Logger.getLogger(CreateOrderPush.class);
	
	private List<ArrearageOfficeList> arrearageOfficeList;
	private String weburl;
	private String formats;
	
	public CreateOrderPush(List<ArrearageOfficeList> arrearageOfficeList,String weburl,String formats) {
		this.arrearageOfficeList = arrearageOfficeList;
		this.weburl = weburl;
		this.formats = formats;
	}
	
	@SuppressWarnings("all")
	public void run() {
		
		PushUtils pushUtils = new PushUtils();
		
		for (int i = 0; i < arrearageOfficeList.size(); i++) {
			try{
				//map.put("office_id", arrearageOfficeList.get(i).getOffice_id());
				String param = "{'ver_num':'1.0.0','office_id':'"+arrearageOfficeList.get(i).getOffice_id()+"'}";
				Map<String,Object> m = null;
				String user_id = null;
				Object object = JSONObject.fromObject(WebUtils.postCSObject(param, weburl)).get("data");
				if(!"null".equals(String.valueOf(object))){
					m = (Map<String, Object>) object;
					user_id = String.valueOf(m.get("proxy_userid"));
				}
				if(user_id != null && user_id != "")
					pushUtils.pushMsg(user_id, formats+"月账单已出，共计消费"+arrearageOfficeList.get(i).getUsed_limit()+"元", 15, "信用额度账单");
			}catch(Exception e){
				logger.error("#####【定时任务createRefundOrder】创建信用额度还款订单推送通知出现异常，异常信息为："+e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
