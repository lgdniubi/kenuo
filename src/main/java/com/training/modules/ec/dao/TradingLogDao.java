package com.training.modules.ec.dao;

import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.TradingLog;

/**
 * 账户交易记录
 * @author yangyang
 *
 */

@MyBatisDao
public interface TradingLogDao extends TreeDao<TradingLog> {
	
	/**
	 * 根据订单id查询日志
	 * @param orderId
	 * @return
	 */
	public TradingLog findByOrderId(String orderId);
	
	
	public int insertTradingLog(Map<String, Object> map);

}
