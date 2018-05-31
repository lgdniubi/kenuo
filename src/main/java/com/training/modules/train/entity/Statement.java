package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
/**
 * 对账单
 * @author QJL
 *
 */
public class Statement extends DataEntity<Statement> {
	private static final long serialVersionUID = 1L;
	
	private int stateId;
	private String orderId; //订单id
	private String officeId; //机构id
	private double usedLimit; //额度
	private String from; //0:订单，1：取消订单，2：售后
	private int type; //0:收入1:支出
	private String createTime;
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public double getUsedLimit() {
		return usedLimit;
	}
	public void setUsedLimit(double usedLimit) {
		this.usedLimit = usedLimit;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
