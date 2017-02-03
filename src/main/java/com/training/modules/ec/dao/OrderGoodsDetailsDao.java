package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsDetailSum;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;
/**
 * 订单商品详情记录表
 * @author dalong
 *
 */

@MyBatisDao
public interface OrderGoodsDetailsDao extends TreeDao<OrderGoodsDetails> {

	/**
	 * 保存订单商品详情记录
	 * @param details
	 */
	void saveOrderGoodsDetails(OrderGoodsDetails details);

	/**
	 * 通过主订单id查询订单的计费信息
	 * @param orderid
	 * @return
	 */
	Orders getOrderGoodsDetailListByOid(@Param("orderid")String orderid);

	/**
	 * 通过子订单id查询计费信息
	 * @param i
	 * @return
	 */
	OrderGoods getOrderGoodsDetailListByMid(@Param("mid")int i);
	/**
	 * 查看商品订单充值记录
	 * @param orderId 
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	List<OrderGoodsDetails> getMappinfOrderView(@Param("recid")Integer recid);
	
	/**
	 * 计算欠款
	 * @param recId
	 * @return
	 */
	public  GoodsDetailSum selectDetaiSum(String recId);
}
