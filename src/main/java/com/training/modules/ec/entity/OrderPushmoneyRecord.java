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
	private String pushmoneyUserId;	//提成人员ID
	private String pushmoneyUserName;	//提成人员name
	private String pushmoneyUserMobile;	//提成人员手机
	private double pushMoney;		//提成金额
	private String operatorName;	//操作人
	private String flag;            //标识
	
	private String officeId;		//操作用户当前机构ID   2017-10-12 土豆添加
	private String userOfficeId;	//业务员机构ID  	 2017-10-25 土豆添加
	private String userOfficeIds;	//业务员机构IDS  	 2017-10-25 土豆添加
	
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

	public String getPushmoneyUserId() {
		return pushmoneyUserId;
	}

	public void setPushmoneyUserId(String pushmoneyUserId) {
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getUserOfficeId() {
		return userOfficeId;
	}

	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}

	public String getUserOfficeIds() {
		return userOfficeIds;
	}

	public void setUserOfficeIds(String userOfficeIds) {
		this.userOfficeIds = userOfficeIds;
	}
	
}
