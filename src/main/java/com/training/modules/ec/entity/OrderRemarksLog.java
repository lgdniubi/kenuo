package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 订单备注日志表
 * 
 * @author dalong
 * @version
 */
public class OrderRemarksLog extends DataEntity<OrderRemarksLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006757284338719813L;
	
	private int orderRemarksId;
	private String orderId;
	
	
	public int getOrderRemarksId() {
		return orderRemarksId;
	}
	public void setOrderRemarksId(int orderRemarksId) {
		this.orderRemarksId = orderRemarksId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
