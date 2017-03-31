package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderInvoice;
import com.training.modules.ec.entity.OrderInvoiceExport;
import com.training.modules.ec.entity.OrderInvoiceRelevancy;
import com.training.modules.ec.entity.Orders;


/**
 *订单发票dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface OrderInvoiceDao extends TreeDao<OrderInvoice>{

	/**
	 * 保存商品订单
	 * @param orders
	 */
	void saveOrderInvoice(Orders orders);
	
	
	/**
	 * 保存
	 * @param relevancy
	 * @return
	 */
	public int insertMaping(OrderInvoiceRelevancy relevancy);
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public int updateOrderIsinv(String orderId);
	
	/**
	 * 修改
	 * @param orderInvoice
	 * @return
	 */
	public int updateSave(OrderInvoice orderInvoice);
	
	/**
	 * 删除数据
	 * @param orderInvoice
	 * @return
	 */
	public int delInvoic(OrderInvoice orderInvoice);
	
	/**
	 * 导出数据
	 * @return
	 */
	public List<OrderInvoiceExport> findExportList();
	
	/**
	 * 查询
	 * @return
	 */
	public List<Orders> findInvoiceRelevancy(String invoiceId);
	
// 2017年3月21日14:20:09 发票优化
	/**
	 * 查询所有已完成无欠款的订单 
	 * @param userId
	 * @return
	 */
	public List<Orders> findOrderList(int userId);
	/**
	 * 查询所有已开发票的订单
	 * @param userId
	 * @return
	 */
	public List<Orders> findOpenOrderList(int userId);
	/**
	 * 查询已开订单详情
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> findOpenOrderListDetails(String orderId);
	/**
	 * 查询所有已完成无欠款的订单详情
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> findOrderListDetails(String orderId);
}
