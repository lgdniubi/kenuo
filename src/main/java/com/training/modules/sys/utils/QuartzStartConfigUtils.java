/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月8日 下午5:53:47
 * 修改人:	
 * 修改时间:	2017年6月8日 下午5:53:47
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.training.common.utils.SpringContextHolder;
import com.training.modules.sys.dao.QuartzStartConfigDao;
import com.training.modules.sys.entity.QuartzStartConfig;

/**
 * 类名称:	QuartzStartConfigUtils
 * 类描述:	获取定时器初始配置工具类
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月8日 下午5:53:47
 */
public class QuartzStartConfigUtils {

	private static QuartzStartConfigDao dao = SpringContextHolder.getBean(QuartzStartConfigDao.class);
	
	/**
	 * 方法说明:	根据key获取对应value值
	 * 创建时间:	2017年6月8日 下午5:30:06
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 下午5:30:06
	 * @param key
	 * @return
	 */
	public static Integer queryValue(String key){
		Integer value = dao.queryValue(key);
		return value == null ? 0 : value;
	}
	/**
	 * 方法说明:	添加定时器起始配置数据
	 * 创建时间:	2017年6月8日 下午6:21:24
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 下午6:21:24
	 * @return
	 */
	public static int addQuartzStartConfig(Map<String, Object> map){
		List<QuartzStartConfig> list = new ArrayList<QuartzStartConfig>();
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			String key = entry.getKey();
			Integer value = (Integer) entry.getValue();
			QuartzStartConfig quartzStartConfig = new QuartzStartConfig();
			quartzStartConfig.setKey(key);
			quartzStartConfig.setValue(value);
			list.add(quartzStartConfig);
		}
		int num = dao.addQuartzStartConfig(list);
		return num;
	}
}
