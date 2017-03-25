package com.training.modules.ec.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;

/**
 * 创建订单dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface OrderGoodsDao extends TreeDao<OrderGoods> {
	/**
	 * 根据订单id查询订单下商品
	 * @param id
	 * @return
	 */
	public List<OrderGoods> findListByOrderid(String orderid);
	/**
	 * 根据订单id查询订单下商品
	 * @param id
	 * @return
	 */
	public List<OrderGoods> orderlistTow(String orderid);
	
	/**
	 * 保存新建订单数据
	 * @param orderGoods
	 * @return
	 */
	public int saveOrderGoods(OrderGoods orderGoods);
	
	/**
	 * 通过订单号查询订单下的虚拟商品及用户信息
	 * @param orders
	 * @return
	 */
	public List<OrderGoods> findOrderGoods(OrderGoods orderGoods);
	/**
	 * 根据商品id 查询数量
	 * @param goodsId
	 * @return
	 */
	public int numByGoodsId(String goodsId);
	/**
	 * 根据订单id查询订单下商品以及商品条形码
	 * @param orderid
	 * @return
	 */
	public List<OrderGoods> findBarCodeByOederId(String orderid);

	/**
	 * 通过主订单查询商品订单信息
	 * @param orders
	 * @return
	 */
	public List<OrderGoods> getOrderGoodsMapping(Orders orders);
	
	/**
	 * 查询
	 * @param orderGoods
	 * @return
	 */
	public List<OrderGoods> selectByOrderId(OrderGoods orderGoods);
	
	/**
	 * 查询
	 * @param orderId
	 * @return
	 */
	public OrderGoods selectSumByorderid(String orderId);
	
	/**
	 * 更新是否售后状态
	 * @param recId
	 * @return
	 */
	public int updateIsAfterSales(OrderGoods orderGoods);
	/**
	 * 更新是否售后状态
	 * @param recId
	 * @return
	 */
	public int updateIsAfterSalesTow(String recId);
	/**
	 * 根据订单id查询所有商品订单
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> getGoodMapping(@Param("orderId")String orderId);
	
	
	 /**
	  * 根据recid查找相应的商品信息
	  * @param recId
	  * @return
	  */
	public OrderGoods selectOrderGoodsByRecid(int recId);
}
