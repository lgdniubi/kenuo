package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrdersLog;


/**
 *操作日志dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface OrdersLogDao extends TreeDao<OrdersLog>{
	
	/**
	 * 根据订单号 查询操作此订单的管理员
	 * @param orderid
	 * @return
	 */
	public List<OrdersLog> findByOrderid(String orderid);
	


}
