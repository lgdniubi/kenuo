package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 登云账户实体类
 * @author xiaoye  2017年3月29日
 *
 */
public class OfficeAccount extends DataEntity<OfficeAccount>{

	private static final long serialVersionUID = 1L;

	private String officeId;   //组织ID
	private double amount;     //金额
	private String payPwd;     //支付密码
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	
	
}
