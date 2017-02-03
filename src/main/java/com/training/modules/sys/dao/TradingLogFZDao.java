package com.training.modules.sys.dao;

import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.TradingFZLog;


/**
 * 账户交易记录
 * @author yangyang
 *
 */

@MyBatisDao
public interface TradingLogFZDao extends TreeDao<TradingFZLog> {
	
	/**
	 * 根据订单id查询日志
	 * @param orderId
	 * @return
	 */
	public TradingFZLog findByOrderId(String orderId);
	
	
	public int insertTradingLog(Map<String, Object> map);

}
