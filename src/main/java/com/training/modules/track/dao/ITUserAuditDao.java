package com.training.modules.track.dao;

import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.track.entity.TUserAudit;

/**
 * 类名称：  	ITUserAudit
 * 类描述：	用户审核Dao层
 * 创建人：  	kele
 * 创建时间：	2018年6月14日 下午3:57:42
 */
@MyBatisDao
public interface ITUserAuditDao extends CrudDao<TUserAudit>{

	/**
	 * 方法说明：	查询用户审核详情
	 * 创建时间：	2018年6月14日 下午3:59:30
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月14日 下午3:59:30
	 * @param map
	 * @return
	 */
	public TUserAudit queryUserAuditDetails(Map<String, Object> map);
	
}
