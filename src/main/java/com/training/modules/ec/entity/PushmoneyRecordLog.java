package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 订单提成业务员日志表
 * @author xiaoye 2017年9月11日
 *
 */
public class PushmoneyRecordLog extends DataEntity<PushmoneyRecordLog> {

	private static final long serialVersionUID = 1L;
	
	private int pushmoneyRecordLogId;    //订单提成日志ID自动增长
	private String orderId;              //订单ID
	private int pushmoneyRecordId;         //业务员提成表ID
	private String content;              //更新内容
	
	public int getPushmoneyRecordLogId() {
		return pushmoneyRecordLogId;
	}
	public void setPushmoneyRecordLogId(int pushmoneyRecordLogId) {
		this.pushmoneyRecordLogId = pushmoneyRecordLogId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getPushmoneyRecordId() {
		return pushmoneyRecordId;
	}
	public void setPushmoneyRecordId(int pushmoneyRecordId) {
		this.pushmoneyRecordId = pushmoneyRecordId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
}
