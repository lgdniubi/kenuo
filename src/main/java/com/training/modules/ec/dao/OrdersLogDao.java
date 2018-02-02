package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrdersLog;


/**
 *操作日志dao
 * @author 小叶   2018年1月24日
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
	
	/**
	 * 插入订单日志
	 * @param ordersLog
	 */
	public void saveOrdersLog(OrdersLog ordersLog);
	
	/**
	 * 根据订单id查询该订单的日志
	 * @param orderid
	 * @return
	 */
	public List<OrdersLog> editLog(OrdersLog ordersLog);
}
