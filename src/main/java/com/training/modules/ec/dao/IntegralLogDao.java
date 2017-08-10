package com.training.modules.ec.dao;

import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.IntegralLog;
import com.training.modules.ec.entity.IntegralsLog;

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
	
	/**
	 * 商品售后,扣减云币
	 * @param integralsLog
	 */
	public void insertIntegrals(IntegralsLog integralsLog);

}
