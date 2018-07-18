package com.training.common.track.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.training.common.config.Global;

/**
 * 类名称：	TrackConfig
 * 类描述：	埋点主要配置类
 * 创建人： 	kele
 * 创建时间：    	2018年4月25日16:53:13
 */
public class TrackConfig {

	// 从 Sensors Analytics 获取的数据接收的 URL
	public static final String SA_SERVER_MTMY_URL = Global.getConfig("SA_SERVER_MTMY_URL");
	// 从 Sensors Analytics 获取的数据接收的 URL
	public static final String SA_SERVER_FZX_URL = Global.getConfig("SA_SERVER_FZX_URL");
	// 使用 Debug 模式，并且导入 Debug 模式下所发送的数据
	public static final boolean SA_WRITE_DATA = true;
	// 模式 DEBUG ONLINE
	public static final String SA_LEVEL_DATA = Global.getConfig("SA_LEVEL_DATA");
		
	// 埋点地址列表
	public static Map<String, String> trackConfigList = new HashMap<String, String>();
	static {
		/*每天美耶*/
		// 注册-同步每天美耶账号
		trackConfigList.put("sign_up", "com.training.common.track.modules.TrackLoginPub");
		// 消耗业绩统计
		trackConfigList.put("deplete_achievement", "com.training.common.track.modules.TrackApptPub");
		// 充值信息
		trackConfigList.put("order_recharge", "com.training.common.track.modules.TrackOrderPub");
		// 支付订单
		trackConfigList.put("pay_order", "com.training.common.track.modules.TrackOrderPub");
		
		/*妃子校*/
		// 用户审核 -通过
		trackConfigList.put("user_authent_win", "com.training.common.track.modules.TrackUserAuditPub");
		// 用户审核-失败
		trackConfigList.put("user_authent_Loser", "com.training.common.track.modules.TrackUserAuditPub");
		// 注册-妃子校账号
		trackConfigList.put("sign_up_fzx", "com.training.common.track.modules.TrackLoginPub");
	}

	/**
	 * 方法说明：	根据某个对象的名称和方法去执行该方法
	 * 创建时间：	2018年4月25日16:53:13
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年1月15日 下午7:51:44
	 * @param 	object
	 * @param 	methodName
	 * @param 	paramMap
	 */
	public static void execute(String methodName, Map<String, Object> paramMap) {
		
		//重要参数
		paramMap.put("SA_SERVER_MTMY_URL", SA_SERVER_MTMY_URL);
		paramMap.put("SA_SERVER_FZX_URL", SA_SERVER_FZX_URL);
		paramMap.put("SA_WRITE_DATA", SA_WRITE_DATA);
		
		try {
			// 根据给定的类名初始化类
			Class<?> catClass= Class.forName(trackConfigList.get(methodName));
			// 实例化这个类
			Object obj = catClass.newInstance();
			// 获得这个类的所有方法
			Method[] methods = catClass.getMethods();
			// 循环查找想要的方法
			for (Method method : methods) {
				if ("setParamMap".equals(method.getName())) {
					// 调用这个方法，invoke第一个参数是类名，后面是方法需要的参数
					method.invoke(obj, paramMap);
				}
			}
			// 调用指定方法
			catClass.getMethod(methodName).invoke(obj, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方法说明：	埋点-MTMY-模式切换
	 * 创建时间：	2018年4月25日16:53:13
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年4月25日16:53:13
	 * @return
	 * @throws IOException 
	 */
	public static SensorsAnalytics getMtmySensorsAnalytics(Map<String, Object> paramMap) throws IOException {
		SensorsAnalytics sa = null ;
		if(TrackConfig.SA_LEVEL_DATA.equals("DEBUG")) {
			//debug模式
	    	sa = new SensorsAnalytics(new SensorsAnalytics.DebugConsumer(SA_SERVER_MTMY_URL, SA_WRITE_DATA));
	    }else if(TrackConfig.SA_LEVEL_DATA.equals("ONLINE")) {
	    	//生产模式
	    	sa = new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer("/data/shence/kenuo_logs/access.log"));
	    }
		
		Map<String, Object> properties = new HashMap<String, Object>();
		// 平台类型 (iOS/Android/WAP/INTERFACE)
		properties.put("platformType", "mg后台");
		// 是否是登录状态,默认登录
		properties.put("is_login", true);
		// 每天美耶用户ID
		properties.put("com_mtmy_user_id", String.valueOf(paramMap.get("DISTINCT_ID")));
		// 设置公共属性
		sa.registerSuperProperties(properties);
		
		return sa;
	}
	
	/**
	 * 方法说明：	埋点-MTMY-模式切换
	 * 创建时间：	2018年4月25日16:53:13
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年4月25日16:53:13
	 * @return
	 * @throws IOException 
	 */
	public static SensorsAnalytics getFzxSensorsAnalytics(Map<String, Object> paramMap) throws IOException {
		SensorsAnalytics sa = null ;
		if(TrackConfig.SA_LEVEL_DATA.equals("DEBUG")) {
			//debug模式
	    	sa = new SensorsAnalytics(new SensorsAnalytics.DebugConsumer(SA_SERVER_FZX_URL, SA_WRITE_DATA));
	    }else if(TrackConfig.SA_LEVEL_DATA.equals("ONLINE")) {
	    	//生产模式
	    	sa = new SensorsAnalytics(new SensorsAnalytics.ConcurrentLoggingConsumer("/data/shence/kenuo_fzx_logs/access.log"));
	    }
		
		Map<String, Object> properties = new HashMap<String, Object>();
		// 平台类型
		properties.put("platformType", "mg后台");
		// 是否是登录状态
		String distinctId = String.valueOf(paramMap.get("DISTINCT_ID")); 
		properties.put("is_login", true);
		properties.put("com_fzx_user_id", distinctId);
		// 设置公共属性
		sa.registerSuperProperties(properties);
		
		return sa;
	}
}
