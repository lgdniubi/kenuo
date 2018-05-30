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
	private String order_id; //订单id
	private String office_id; //机构id
	private double used_limit; //额度
	private String from; //0:订单，1：取消订单，2：售后
	private int type; //0:收入1:支出
	private String create_date;
	

	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
	public double getUsed_limit() {
		return used_limit;
	}
	public void setUsed_limit(double used_limit) {
		this.used_limit = used_limit;
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
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
}
