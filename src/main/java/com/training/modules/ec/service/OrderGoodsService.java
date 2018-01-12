package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.ReturnedGoods;

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
		List<OrderGoods> list = orderGoodsDao.findListByOrderid(orderid);
		for (OrderGoods orderGoods : list) {
			if(orderGoods.getServicetimes() == 999){//计算时限卡的截止之间
				orderGoods.setExpiringdate(getDate(orderGoods.getRealityAddTime(),orderGoods.getExpiringDate()));
			}
		}
		return list;
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
	
	/**
	 * 根据给出的月份,判断时限卡的截止日期
	 */
	public String getDate(Date date1, int month){
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		c.setTime(date1);// 设置日历时间
		c.add(Calendar.MONTH, month);// 在日历的月份上增加6个月
		return sdf.format(c.getTime());	// 返回截止时间
	}

	/**
	 * 获取实物子项的可售后数量(根据组ID(mapping_id)获取卡项中的实物集合)
	 * @param orderGoods
	 * @return
	 */
	public List<OrderGoods> getCardRealNum(OrderGoods orderGoods) {
		return orderGoodsDao.getCardRealNum(orderGoods);
	}

	/**
	 * 根据recid和订单ID查询商品和子项信息
	 * @param og
	 * @return
	 */
	public List<OrderGoods> cardOrderid(OrderGoods og) {
		return orderGoodsDao.cardOrderid(og);
	}

	/**
	 * 获取商品的实付金额和剩余服务次数
	 * @param returnedGoods
	 * @return
	 */
	public OrderGoods getTotalAmountAndTimes(ReturnedGoods returnedGoods) {
		return orderGoodsDao.getTotalAmountAndTimes(returnedGoods);
	}

	/**
	 * 实物:获取mapping表中的总购买数量
	 * @param returnedGoods
	 * @return
	 */
	public int getGoodsNum(ReturnedGoods returnedGoods) {
		return orderGoodsDao.getGoodsNum(returnedGoods);
	}
	
	/**
	 * 根据mapping关联details查询订单的详情
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> findOrderDetails(String orderId){
		return orderGoodsDao.findOrderDetails(orderId);
	}
}
