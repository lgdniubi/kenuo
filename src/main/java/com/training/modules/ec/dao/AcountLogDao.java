package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.AcountLog;


/**
 *操作日志dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface AcountLogDao extends TreeDao<AcountLog>{
	
	/**
	 * 根据订单号 查询操作此订单的管理员
	 * @param orderid
	 * @return
	 */
	public List<AcountLog> findByOrderid(String orderid);
	
	/**
	 * 插入操作数据
	 * @param acountLog
	 * @return
	 */
	public int insertLog(AcountLog acountLog);
	
	/**
	 * 查询两行日志信息
	 * @param orderid
	 * @return
	 */
	public List<AcountLog> findByOrderidtwo(String orderid);

}
