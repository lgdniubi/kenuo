package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrderInvoiceDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderInvoice;
import com.training.modules.ec.entity.OrderInvoiceExport;
import com.training.modules.ec.entity.OrderInvoiceRelevancy;
import com.training.modules.ec.entity.Orders;

/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class OrderInvoiceService extends TreeService<OrderInvoiceDao,OrderInvoice> {

	@Autowired
	private OrderInvoiceDao orderInvoiceDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	
	
	/**
	 * 保存发票信息
	 * @param orders
	 */
	public void saveOrderInvoice(Orders orders) {
		dao.saveOrderInvoice(orders);
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<OrderInvoice> findInvoice(Page<OrderInvoice> page, OrderInvoice orderInvoice) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		orderInvoice.setPage(page);
		// 执行分页查询
		page.setList(orderInvoiceDao.findList(orderInvoice));
		return page;
	}
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderGoods selectSumByorderid(String orderId){
		return orderGoodsDao.selectSumByorderid(orderId);
	}
	/**
	 * 保存
	 * @param orderInvoice
	 */
	public void saveOrderInvoice(OrderInvoice orderInvoice){
		
		orderInvoiceDao.insert(orderInvoice);

	}
	
	/**
	 * 修改
	 * @param orderInvoice
	 * @return
	 */
	public int updateSave(OrderInvoice orderInvoice){
		return orderInvoiceDao.updateSave(orderInvoice);
	}
	
	/**
	 * 删除数据 
	 * @param orderInvoice
	 * @return
	 */
	public int delInvoic(OrderInvoice orderInvoice){
		return orderInvoiceDao.delInvoic(orderInvoice);
	}
	
	
	/**
	 * 插入中建表
	 * @param relevancy
	 * @return
	 */
	public int insertMaping(OrderInvoiceRelevancy relevancy){
		return orderInvoiceDao.insertMaping(relevancy);
	}
	/**
	 * 修改已经开发票的订单
	 * @return
	 */
	public int updateOrderIsinv(String orderId){
		return orderInvoiceDao.updateOrderIsinv(orderId);
	}
	/**
	 * 导出数据
	 * @return
	 */
	public List<OrderInvoiceExport> findExportList(){
		return orderInvoiceDao.findExportList();
	}
	
	/**
	 * 查询发票下的订单
	 * @param invoiceId
	 * @return
	 */
	public List<Orders> findInvoiceRelevancy(String invoiceId){
		return orderInvoiceDao.findInvoiceRelevancy(invoiceId);
	} 
	
// 2017年3月21日14:20:37 发票优化
	/**
	 * 查询所有已完成无欠款的订单 
	 * @param userId
	 * @return
	 */
	public List<Orders> findOrderList(int userId){
		return orderInvoiceDao.findOrderList(userId);
	}
	/**
	 * 查询所有已开发票的订单
	 * @param userId
	 * @return
	 */
	public List<Orders> findOpenOrderList(int userId){
		return orderInvoiceDao.findOpenOrderList(userId);
	}
	/**
	 * 查询已开订单详情
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> findOpenOrderListDetails(String orderId){
		return dao.findOpenOrderListDetails(orderId);
	}
	/**
	 * 查询所有已完成无欠款的订单详情
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> findOrderListDetails(String orderId){
		return dao.findOrderListDetails(orderId);
	}
}
