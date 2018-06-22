package com.training.common.track.modules;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.training.common.track.config.TrackConfig;

/**
 * 类名称:  TrackLoginPub
 * 类描述:  登录-埋点主体方法
 * 创建人:  kele
 * 创建时间: 2018年4月25日16:53:13
 */
public class TrackLoginPub {

	private static Log log = LogFactory.getLog(TrackLoginPub.class);
	
	private Map<String, Object> paramMap;
	
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	/**
	 * 方法说明:	注册
	 * 创建时间:	2018年4月25日16:53:13
	 * 创建人:	kele
	 * 修改记录:	修改人	修改记录	2018年1月17日 下午3:35:35
	 */
	public void sign_up() {
	    try {
	    	// 使用 DebugConsumer 初始化 SensorsAnalytics
		    final SensorsAnalytics sa = TrackConfig.getMtmySensorsAnalytics(this.paramMap);
		    
		    // 用户的 Distinct Id	(登录用用户ID，非登录用户用设备ID)
		    String distinctId = String.valueOf(this.paramMap.get("DISTINCT_ID"));
	    	
		    String sourceType = "";
		    if(null != this.paramMap.get("SOURCE_TYPE")) {
		    	sourceType = (String) this.paramMap.get("SOURCE_TYPE");
		    	if("".equals(sourceType) || "null".equals(sourceType)) {
		    		sourceType = "";
		    	}
		    }
		    String actionSource = "";
		    if(null != this.paramMap.get("ACTION_SOURCE")) {
		    	actionSource = (String) this.paramMap.get("ACTION_SOURCE");
		    	if("".equals(actionSource) || "null".equals(actionSource)) {
		    		actionSource = "";
		    	}
		    }
		    
		    Map<String, Object> properties = new HashMap<String, Object>();
		    // 用户ID
		    properties.put("idy_user_id", distinctId);
		    // 注册是否成功，默认为true
		    properties.put("is_success", true);
		    // 来源类型
		    properties.put("source_type", String.valueOf(sourceType));
		    // 来源名称
		    properties.put("action_source",  String.valueOf(actionSource));
		    
		    String anonymousId = String.valueOf(this.paramMap.get("ANONYMOUS_ID"));
		    if(null != anonymousId && !"null".equals(anonymousId) && !"".equals(anonymousId)) {
		    	// 用户注册，将用户ID与匿名ID关联
		    	sa.trackSignUp(distinctId, anonymousId);
		    }
		    
		    // 记录用户注册事件
		    sa.track(distinctId, true, "sign_up", properties);

			// 程序结束前，停止 Sensors Analytics SDK 所有服务
		    sa.shutdown();
		    
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-注册[sign_up],出现异常，信息为："+e.getMessage());
		}
	}
}
