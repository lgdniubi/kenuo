package com.training.modules.train.entity;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 直播会员entity
 * @author yangyang
 *
 */
public class LiveUser  {

	private String auditId;			//直播间id
	private String name;			//用户姓名
	private String mobile;			//手机号
	private String money;			//购买金额
	private String payment;			//支付方式
	private String remak;			//备注说明
	private String validityDate;	//有效期

	

	@JsonIgnore
	@NotNull(message="直播ID不能为空")
	@ExcelField(title="直播ID", align=2, sort=10)
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	
	@JsonIgnore
	@ExcelField(title="会员姓名", align=2, sort=15)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@NotNull(message="手机号不能为空")
	@ExcelField(title="手机号", align=2, sort=20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonIgnore
	@ExcelField(title="购买金额", align=2, sort=25)
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	@JsonIgnore
	@ExcelField(title="支付方式", align=2, sort=30)
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	@JsonIgnore
	@ExcelField(title="备注", align=2, sort=35)
	public String getRemak() {
		return remak;
	}

	public void setRemak(String remak) {
		this.remak = remak;
	}

	@JsonIgnore
	@ExcelField(title="有效期", align=2, sort=40)
	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

	


}
