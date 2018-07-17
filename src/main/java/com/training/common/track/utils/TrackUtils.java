package com.training.common.track.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.training.common.utils.BeanUtil;
import com.training.modules.track.entity.TOffice;
import com.training.modules.track.service.IOfficeDaoService;

/**
 * 类名称：	TrackUtils
 * 类描述：	埋点公共方法抽取
 * 创建人： 	kele
 * 创建时间：    	2018年4月28日 上午11:00:42
 */
public class TrackUtils {
	
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
	
	
	
	/**
	 * 方法说明：	转换String类型
	 * 创建时间：	2018年7月12日 下午4:30:33
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月12日 下午4:30:33
	 * @param obj
	 * @return
	 */
	public static String convertStr(Object obj) {
		if(null != obj) {
			return String.valueOf(obj);
		}
		return "";
	}
	
	/**
	 * 方法说明：	转换double类型
	 * 创建时间：	2018年7月14日 下午3:38:51
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午3:38:51
	 * @param obj
	 * @return
	 */
	public static double convertDouble(Object obj) {
		if(null != obj) {
			return (double) obj;
		}
		return 0.0;
	}
	
	/**
	 * 方法说明：	转换Integer类型
	 * 创建时间：	2018年7月14日 下午3:39:37
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午3:39:37
	 * @param obj
	 * @return
	 */
	public static Integer convertInteger(Object obj) {
		if(null != obj) {
			return (Integer) obj;
		}
		return 0;
	}
	
	/**
	 * 方法说明：	根据officeId查询机构详情
	 * 创建时间：	2018年7月14日 下午4:54:49
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午4:54:49
	 * @param officeId
	 * @return
	 */
	public static TOffice getOffcieDetail(String officeId) {
		IOfficeDaoService iOfficeDaoService = (IOfficeDaoService) BeanUtil.getBean("IOfficeDaoService");
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("officeId", officeId);
	    return iOfficeDaoService.queryOfficeDetail(map);
	}
}
