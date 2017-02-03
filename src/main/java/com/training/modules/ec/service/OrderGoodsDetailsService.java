package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;

/**
 * 订单商品详情记录表service
 * 
 * @author dalong
 *
 */

@Service
public class OrderGoodsDetailsService extends TreeService<OrderGoodsDetailsDao, OrderGoodsDetails> {

	/**
	 * 保存订单商品详情记录
	 * @param details
	 */
	public void saveOrderGoodsDetails(OrderGoodsDetails details) {
		dao.saveOrderGoodsDetails(details);
	}
	/**
	 * 通过主订单id查询订单的计费信息
	 * @param orderid
	 * @return
	 */
	public Orders getOrderGoodsDetailListByOid(String orderid) {
		return dao.getOrderGoodsDetailListByOid(orderid);
	}
	
	/**
	 * 通过子订单id查询计费信息
	 * @param i
	 * @return
	 */
	public OrderGoods getOrderGoodsDetailListByMid(int i) {
		return dao.getOrderGoodsDetailListByMid(i);
	}

	/**
	 * 查看商品订单充值记录
	 * @param orderId 
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	public List<OrderGoodsDetails> getMappinfOrderView(Integer recid) {
		return dao.getMappinfOrderView(recid);
	}
}
