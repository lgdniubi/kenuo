package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;
/**
 * 订单提成记录表
 * @author dalong
 *
 */
public class OrderPushmoneyRecord extends TreeEntity<OrderPushmoneyRecord> {
	
	private static final long serialVersionUID = 1L;
	private int pushmoneyRecordId;	//主键自动增长
	private String orderId;			//订单ID
	private int pushmoneyUserId;	//提成人员ID
	private String pushmoneyUserName;	//提成人员name
	private String pushmoneyUserMobile;	//提成人员手机
	private double pushMoney;		//提成金额
	private String operatorName;	//操作人
	
	
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPushmoneyUserName() {
		return pushmoneyUserName;
	}

	public void setPushmoneyUserName(String pushmoneyUserName) {
		this.pushmoneyUserName = pushmoneyUserName;
	}

	public String getPushmoneyUserMobile() {
		return pushmoneyUserMobile;
	}

	public void setPushmoneyUserMobile(String pushmoneyUserMobile) {
		this.pushmoneyUserMobile = pushmoneyUserMobile;
	}

	public int getPushmoneyUserId() {
		return pushmoneyUserId;
	}

	public void setPushmoneyUserId(int pushmoneyUserId) {
		this.pushmoneyUserId = pushmoneyUserId;
	}

	public int getPushmoneyRecordId() {
		return pushmoneyRecordId;
	}

	public void setPushmoneyRecordId(int pushmoneyRecordId) {
		this.pushmoneyRecordId = pushmoneyRecordId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getPushMoney() {
		return pushMoney;
	}

	public void setPushMoney(double pushMoney) {
		this.pushMoney = pushMoney;
	}

	@Override
	public OrderPushmoneyRecord getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(OrderPushmoneyRecord parent) {
		// TODO Auto-generated method stub
		
	}
}
