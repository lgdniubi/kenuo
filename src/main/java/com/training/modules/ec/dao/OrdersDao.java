package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Orders;
/**
 * 订单Dao
 * @author water
 *
 */
@MyBatisDao

public interface OrdersDao extends TreeDao<Orders>{
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<Orders> findAlllist(Orders orders);
	/**
	 * 根据id查询数据
	 * @param orderid
	 * @return
	 */
	public List<Orders> findByOrderId(String orderid);

	/**
	 * 保存修改数据
	 * @param orders
	 * @return
	 */
	public int UpdateOrders(Orders orders);
	/**
	 * 更新订单状态
	 * @param orders
	 * @return
	 */
	public int UpdateOrderstatus(Orders orders);
	
	/**
	 * 更新订单状态
	 * @param orders
	 * @return
	 */
	public int  UpdateOrderReturnStatus(Orders orders);
	/**
	 * 更新退货订数据
	 * @param orders
	 * @return
	 */
	public int UpdateOrderReturn(Orders orders);
	
	/**
	 * 修改物流
	 * @param orders
	 * @return
	 */
	public int UpdateShipping(Orders orders);
	
	
	/**
	 * 根据订单orderid查询 订单表 parent_id 有没有生成 退货订单 
	 * @param orderid
	 * @return
	 */
	public List<Orders> orderlist(String orderid);
	/**
	 * 插入退货订单
	 * @param orders
	 * @return
	 */
	public int insertReturn(Orders orders);
	/**
	 * 保存新增订单数据
	 * @param orders
	 * @return
	 */
	public int saveOrder(Orders orders);
	/**
	 * 查询用下面有多少个订单
	 * @param userId
	 * @return
	 */
	public int findOrderByUserId(String userId);
	/**
	 * 查询所有没有退货的订单
	 * @param minute
	 * @return
	 */
	public List<Orders> queryNotPayOrder(Map<String, Object> map);
	/**
	 * 修改订单状态
	 * @param map
	 * @return
	 */
	public int modifyOrderStatus(Map<String, Object> map);
	
	/**
	 * 更新对账日志
	 * @param orderId
	 * @return
	 */
	public int updateSale(String orderId);
	
	/**
	 * 查询分销 是否有订单
	 * @param orderId
	 * @return
	 */
	public int selectSaleByOrderid(String orderId);
}
