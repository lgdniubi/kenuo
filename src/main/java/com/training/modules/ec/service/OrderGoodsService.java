package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.entity.OrderGoods;

/**
 * 订单商品信息service
 * @author yangyang
 *
 */

@Service
@Transactional(readOnly = false)
public class OrderGoodsService extends TreeService<OrderGoodsDao,OrderGoods>{
	
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	
	
	
	/**
	 * 根据订单号查询订单下所有商品
	 * @param id
	 * @return
	 */
	public List<OrderGoods> orderlist(String orderid){
		return orderGoodsDao.findListByOrderid(orderid);
	}
	
	/**
	 * 根据订单号查询订单下所有商品
	 * @param id
	 * @return
	 */
	public List<OrderGoods> orderlistTow(String orderid){
		return orderGoodsDao.orderlistTow(orderid);
	}
	
	/**
	 * 通过订单号查询订单下的虚拟商品及用户信息
	 * @param orders
	 * @return
	 */
	public List<OrderGoods> findOrderGoods(OrderGoods orderGoods){
		return dao.findOrderGoods(orderGoods);
	}
	
	public int numByGoodsId(String goodsId){
		return dao.numByGoodsId(goodsId);
	}
	/**
	 * 
	 * @param orderGoods
	 * @return
	 */
	public List<OrderGoods> selectByOrderId(String orderId,String userId){
		OrderGoods orderGoods=new OrderGoods();
		orderGoods.setOrderid(orderId);
		orderGoods.setUserid(Integer.parseInt(userId));
		return dao.selectByOrderId(orderGoods);
	}

	/**
	 * 通过订单id查询订单商品信息
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> getGoodMapping(String orderId) {
		return dao.getGoodMapping(orderId);
	}
	
	
	
	
	
}
