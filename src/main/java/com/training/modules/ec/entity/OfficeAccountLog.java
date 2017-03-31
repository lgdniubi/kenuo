package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 登云账户log日志实体类
 * @author xiaoye  2017年3月29日
 *
 */
public class OfficeAccountLog extends DataEntity<OfficeAccountLog>{

	private static final long serialVersionUID = 1L;
	
	private int officeAccountLogId;  // 登云账户log日志id
	private String orderId;         //订单id
	private String officeId;        //机构id 
	private String type;            //类型（0：收入，1支出）
	private String officeFrom;      //来源机构id
	private double amount;         //金额
	private String channelFlag;     //渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	
	public int getOfficeAccountLogId() {
		return officeAccountLogId;
	}
	public void setOfficeAccountLogId(int officeAccountLogId) {
		this.officeAccountLogId = officeAccountLogId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOfficeFrom() {
		return officeFrom;
	}
	public void setOfficeFrom(String officeFrom) {
		this.officeFrom = officeFrom;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	
	
}
