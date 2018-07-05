package com.training.common.track.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.training.common.config.Global;
import com.training.common.track.thread.TrackThread;
import com.training.modules.sys.entity.User;

/**
 * 类名称：	TrackUtils
 * 类描述：	埋点公共方法抽取
 * 创建人： 	kele
 * 创建时间：    	2018年4月28日 上午11:00:42
 */
public class TrackUtils {

	/**
	 * 方法说明：	同步每天美耶用户埋点方法
	 * 创建时间：	2018年4月28日 上午11:01:18
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年4月28日 上午11:01:18
	 * @param user
	 */
	public static void trackSyncMtmyUser(User user) {
		/*##########[神策埋点{sign_up}-Begin]##########*/
		if(null != user && 0 != user.getMtmyUserId()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 方法名称-注册
			paramMap.put("METHOD_NAME", "sign_up");
			// 用户ID
			paramMap.put("DISTINCT_ID", user.getMtmyUserId());
			// 匿名ID
			paramMap.put("ANONYMOUS_ID", "");
			// 来源类型
			paramMap.put("SOURCE_TYPE", user.getSourceType());
			// 来源类型名称
			paramMap.put("ACTION_SOURCE", user.getActionSource());
			
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(paramMap));
		}
		/*##########[神策埋点end]##########*/
	}
	
	/**
	 * 方法说明：	时间处理（秒）
	 * 创建时间：	2018年6月14日 下午4:24:13
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月14日 下午4:24:13
	 * @param disposeDate
	 */
	public static String getTimeDispose(Date disposeDate) {
		long time1 = new Date().getTime();
		long time2 = disposeDate.getTime();
		// 秒级别  
	    long days = (time1 - time2) / (1000);
	    return String.valueOf(days);
	}
	
	/**
	 * 方法说明：	时间转换方法（str 转 date）
	 * 创建时间：	2018年6月19日 下午2:40:55
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月19日 下午2:40:55
	 * @param dateStr
	 * @return
	 */
	public static Date getStrConvertDate(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
}
