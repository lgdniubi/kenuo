package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Payment;

/**
 * 支付方式dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface PaymentDao extends TreeDao<Payment> {
	
	/**
	 * 查询所有支付方式
	 * @return
	 */
	public List<Payment> findAlllist();
	
	/**
	 * 根据code查询支付方式
	 * @param code
	 * @return
	 */
	public Payment getByCode(String code);

}
