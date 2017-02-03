package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderRechargeLog;
/**
 * 订单充值日志表Dao
 * @author dalong
 *
 */

@MyBatisDao
public interface OrderRechargeLogDao extends TreeDao<OrderRechargeLog> {

	/**
	 * 保存订单充值日志
	 * @param oLog
	 */
	void saveOrderRechargeLog(OrderRechargeLog oLog);
	
	/**
	 * 查看主订单充值记录
	 * @param orderid
	 * @return
	 */
	List<OrderRechargeLog> getOrderRechargeView(@Param("orderid")String orderid);
}
