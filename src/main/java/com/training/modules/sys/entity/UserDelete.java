package com.training.modules.sys.entity;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.utils.excel.annotation.ExcelField;

public class UserDelete {
	
	private String loginName;
	private String name;
	private String idCard;
	private String on;
	private String mobile;
	
	
	@JsonIgnore
	@NotNull(message="登录名称不能为空")
	@ExcelField(title="登录名", align=2, sort=10)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@JsonIgnore
	@NotNull(message="姓名不能为空")
	@ExcelField(title="姓名", align=2, sort=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@NotNull(message="身份证号不能为空")
	@ExcelField(title="身份证号", align=2, sort=30)
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@JsonIgnore
	@NotNull(message="工号不能为空")
	@ExcelField(title="工号", align=2, sort=40)
	public String getOn() {
		return on;
	}
	public void setOn(String on) {
		this.on = on;
	}
	
	@JsonIgnore
	@NotNull(message="手机号不能为空")
	@ExcelField(title="手机号", align=2, sort=50)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

}
