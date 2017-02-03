package com.training.modules.report.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.report.entity.OrderReport;

/**
 * 订单报表
 * @author yangyang
 *
 */

@MyBatisDao
public interface OrderReportDao extends TreeDao<OrderReport> {
	
	//根据日期查询
	public	List<OrderReport> findOrderList(OrderReport orderReport);
	

}
