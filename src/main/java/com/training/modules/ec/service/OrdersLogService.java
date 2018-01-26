package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrdersLogDao;
import com.training.modules.ec.entity.OrdersLog;
/**
 * 操作日志
 * @author 小叶    2018年1月24日
 *
 */
@Service
@Transactional(readOnly = false)
public class OrdersLogService extends TreeService<OrdersLogDao,OrdersLog> {
	
	@Autowired
	private OrdersLogDao ordersLogDao;
	
	/**
	 * 查询操作此订单的所有操作人
	 * @param orderid
	 * @return
	 */
	public List<OrdersLog> findByOrderid(String orderid){
		return ordersLogDao.findByOrderid(orderid);
	}
	
	/**
	 * 根据订单id查询该订单的日志
	 * @param orderid
	 * @return
	 */
	public Page<OrdersLog> editLog(Page<OrdersLog> page, OrdersLog ordersLog){
		
		// 设置分页参数
		ordersLog.setPage(page);
		// 执行分页查询
		page.setList(ordersLogDao.editLog(ordersLog.getOrderid()));
		return page;
	}
	
}
