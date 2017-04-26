package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.SaleRebatesLog;
/**
 * 订单日志dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface SaleRebatesLogDao extends TreeDao<SaleRebatesLog> {
	
	
	/**
	 * 根据订单id  查询
	 * @param orderId
	 * @return
	 */
	public List<SaleRebatesLog> selectByOrderId(String orderId);
	
	/**
	 * 
	 * @param saleRebatesLog
	 * @return
	 */
	public int updateSale(SaleRebatesLog saleRebatesLog);

	/**
	 * 查询是否有退货记录del_flag=-1
	 * @param orderId
	 * @return
	 */
	public int selectNumByOrderId(String orderId);
}
