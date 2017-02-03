package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderRechargeLogDao;
import com.training.modules.ec.entity.OrderRechargeLog;

/**
 * 订单充值日志service
 * 
 * @author dalong
 *
 */

@Service
public class OrderRechargeLogService extends TreeService<OrderRechargeLogDao, OrderRechargeLog> {

	/**
	 * 保存订单充值日志
	 * @param oLog
	 */
	public void saveOrderRechargeLog(OrderRechargeLog oLog) {
		dao.saveOrderRechargeLog(oLog);
	}

	/**
	 * 查看主订单充值记录
	 * @param orderid
	 * @return
	 */
	public List<OrderRechargeLog> getOrderRechargeView(String orderid) {
		return dao.getOrderRechargeView(orderid);
	}
}
