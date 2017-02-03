package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;


/**
 * 积分明细
 * @author yangyang
 *
 */

public class IntegralLog extends TreeEntity<IntegralLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;					//用户id
	private int type;						//类型 0收入  1支出
	private Date addTime;					//添加时间
	private String remark;					//备注
	private String value;					//积分
	private String orderId;					//订单id
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
	public IntegralLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(IntegralLog parent) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	

	

}
