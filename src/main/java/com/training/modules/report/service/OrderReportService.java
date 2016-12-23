package com.training.modules.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.report.dao.OrderReportDao;
import com.training.modules.report.entity.OrderReport;

/**
 * 订单报表
 * @author yangyang
 *
 */

@Service
@Transactional(readOnly = false)
public class OrderReportService extends CrudService<OrderReportDao,OrderReport>{
	
	@Autowired
	private OrderReportDao orderReportDao;
	
	
	/**
	 * 查询订单
	 * @param orderReport
	 * @return
	 */
	public List<OrderReport> orderlist(OrderReport orderReport){
		
		return orderReportDao.findOrderList(orderReport);
	}


}
