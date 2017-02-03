package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.AcountLogDao;
import com.training.modules.ec.entity.AcountLog;
/**
 * 操作日志
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class AcountLogService extends TreeService<AcountLogDao,AcountLog> {
	
	@Autowired
	private AcountLogDao acountLogDao;
	
	/**
	 * 查询操作此订单的所有操作人
	 * @param orderid
	 * @return
	 */
	public List<AcountLog> findByOrderid(String orderid){
		return acountLogDao.findByOrderid(orderid);
	}
	/**
	 * 插入操作日志
	 * @param acountLog
	 * @return
	 */
	
	public int insertLog(AcountLog acountLog){
		
		return acountLogDao.insertLog(acountLog);
	}
	/**
	 * 查询两行日志信息
	 * @param orderid
	 * @return
	 */
	public List<AcountLog> findByOrderidtwo(String orderid){
		return acountLogDao.findByOrderidtwo(orderid);
	}
}
