/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月8日 下午5:04:51
 * 修改人:	
 * 修改时间:	2017年6月8日 下午5:04:51
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.QuartzStartConfig;

/**
 * 类名称:	QuartzStartConfigDao
 * 类描述:	操作定时器任务执行初始配置  接口
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月8日 下午5:04:51
 */
@MyBatisDao
public interface QuartzStartConfigDao extends TreeDao<QuartzStartConfig> {

	/**
	 * 方法说明:	根据key获取对应value值
	 * 创建时间:	2017年6月8日 下午5:06:23
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 下午5:06:23
	 * @param key
	 * @return
	 */
	public Integer queryValue(String key);
	/**
	 * 方法说明:	添加定时器起始配置数据
	 * 创建时间:	2017年6月8日 下午5:16:16
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 下午5:16:16
	 * @return
	 */
	public int addQuartzStartConfig(List<QuartzStartConfig> list);
	
	/**
	 * 根据key获取每天美耶对应value值
	 * @param key
	 * @return
	 */
	public String queryMtmyValue(String key);
	
	/**
	 * 添加每天美耶定时器起始配置数据
	 * @param list
	 * @return
	 */
	public Integer addMtmyQuartzStartConfig(List<QuartzStartConfig> list);
}
