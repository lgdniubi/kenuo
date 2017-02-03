package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PaymentDao;
import com.training.modules.ec.entity.Payment;

/**
 * 支付方式service
 * @author yangyang
 *
 */

@Service
@Transactional(readOnly = false)
public class PaymentService extends TreeService<PaymentDao,Payment> {
	
	@Autowired
	private PaymentDao paymentDao;
	/**
	 * 查询所有支付方式
	 * @return
	 */
	public List<Payment> paylist(){
		return paymentDao.findAlllist();
	}
	

}
