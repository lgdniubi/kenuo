package com.training.modules.ec.dao;

import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.IntegralLog;

/**
 * 积分明细
 * @author yangyang
 *
 */

@MyBatisDao
public interface IntegralLogDao extends CrudDao<IntegralLog>{
	

	/**
	 * 根据订单id 查询
	 * @param orderId
	 * @return
	 */
	public IntegralLog findByOrderId(String orderId);
	/**
	 * 查询增加的积分
	 * @param orderId
	 * @return
	 */
	
	public IntegralLog findByOrderIdAdd(String orderId);
	
	
	public int insertIntegralLog(Map<String, Object> map);

}
