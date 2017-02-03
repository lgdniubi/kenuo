package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 操作日志实体
 * @author water
 *
 */
public class OrderInvoiceRelevancy extends TreeEntity<OrderInvoiceRelevancy>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int invoiceId;
	private String orderId;

	
	
	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
	public OrderInvoiceRelevancy getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrderInvoiceRelevancy parent) {
		// TODO Auto-generated method stub
		
	}

	

}
