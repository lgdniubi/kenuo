package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.CrmOrders;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderInvoice;
import com.training.modules.ec.entity.OrderInvoiceRelevancy;
import com.training.modules.ec.entity.OrderRemarksLog;
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
	 * 查询所有数据
	 * @return
	 */
	public List<Orders> newFindAlllist(Orders orders);
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
	/**
	 * 查询用户等级
	 * @param mobile
	 * @return
	 */
	public String getUserLevel(String mobile);
	
	/**
	 * 查询用户信息
	 * @param mobile
	 * @return
	 */
	public Orders getUser(String mobile);
	/**
	 * 保存虚拟订单	 2017-01-03 大龙新增
	 * @param _orders
	 */
	public void saveVirtualOrder(Orders _orders);
	/**
	 * 保存用户账户信息
	 * @param _orders
	 */
	public void updateAccount(Orders _orders);
	/**
	 * 根据用户id 查询用户账户信息
	 * @param _orders
	 * @return
	 */
	public Orders getAccount(Orders _orders);
	/**
	 * 根据订单id查询基本信息
	 * @param orderid
	 * @return
	 */
	public Orders selectOrderById(String orderid);
	/**
	 * 修改虚拟订单
	 * @param orders
	 */
	public void updateVirtualOrder(Orders orders);
	/**
	 * 保存 实物订单
	 * @param _orders
	 */
	public void saveKindOrder(Orders _orders);
	/**
	 * 保存订单日备注
	 * @param orders
	 */
	public void saveOrderRemarksLog(Orders orders);
	/**
	 * 通过id查询备注信息
	 * @param orderid
	 * @return
	 */
	public List<OrderRemarksLog> getOrderRemarksLog(@Param("orderid")String orderid);
	/**
	 * 删除订单对应的备注信息
	 * @param orderRemarksId
	 */
	public void deleteOrderRemarksLog(@Param("orderRemarksId")Integer orderRemarksId);
	/**
	 * 保存订单发票中间表
	 * @param orderInvoiceRelevancy
	 */
	public void saveOrderInvoiceRelevancy(OrderInvoiceRelevancy orderInvoiceRelevancy);
	/**
	 * 查询订单发票关联表
	 * @param orderid
	 * @return
	 */
	public OrderInvoice getOrderInvoiceRelevancy(String orderid);

	/**
	 * 查找订单的发票个数
	 */
	public int selectInvoiceRelevancyNum(String orderId);


	/**
	 * 添加单条备注
	 * @param orders
	 */
	public void saveOrderRemarks(Orders orders);
	
	/**
	 * 
	 * @param orders
	 * @return
	 */
	public List<Orders> selectByOrderId(String userId);
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Orders selectBydoubl(String orderId);
	

	
	/**
	 * 根据退货期时间，订单为（1：待发货；2：待收货；）的订单修改状态为（4：已完成）
	 * @return
	 */
	public int updateOrderFinished(Map<String, Object> map);
	
	
	/**
	 * 查询订单
	 * @param orderid
	 * @return
	 */
	public Orders findselectByOrderId(String orderid);
	
	/**
	 * 强制取消
	 * @param orderid
	 * @return
	 */
	public int updateOrderStatut(String orderid);
	
	/**
	 * 取消订单
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	public void cancellationOrder(Orders orders);
	
	/**
	 * 计算是否欠款
	 * @param orderid
	 * @return
	 */
	public Orders selectByOrderIdSum(String orderid);

	/**
	 * @param 
	 * @return Orders
	 * 根据USERID及其其他条件查询订单列表
	 */
	public List<CrmOrders> findByUser(CrmOrders order);

	/**
	 * 分页查询订单，无权限的
	 * @param orders
	 * @return
	 */
	public List<Orders> newFindList(Orders orders);
	
	/**
	 * 修改收货地址
	 * @param orders
	 */
	public void updateAddress(Orders orders);
	
	/**
	 * 根据订单号查询其订单状态以及是否为虚拟订单 
	 */
	public Orders selectOrdersStatus(String orderId);
	
	/**
	 * 查看订单号是否存在
	 */
	public int selectOrdersId(String orderId);
	
	/**
	 * 根据订单里的user_id找到对应的cilent_id
	 * @param orders
	 * @return
	 */
	public List<String> selectCidByUserId(Orders orders);
	
	/**
	 * 根据订单id查找对应的信息，供订单发货后推送给用户 
	 * @param orders
	 * @return
	 */
	public List<OrderGoods> selectOrdersToUser(Orders orders);
}
