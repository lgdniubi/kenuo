package com.training.modules.track.dao;

import java.util.Map;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.track.entity.TOffice;

/**
 * 类名称：  	ITOfficeDao
 * 类描述：	埋点-机构Dao曾
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午4:46:31
 */
@MyBatisDao
public interface ITOfficeDao {

	/**
	 * 方法说明：	查询机构信息
	 * 创建时间：	2018年7月14日 下午4:46:05
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午4:46:05
	 * @param map
	 * @return
	 */
	public TOffice queryOfficeDetail(Map<String, Object> map);
}
