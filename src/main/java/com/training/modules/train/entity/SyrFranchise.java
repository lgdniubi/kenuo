package com.training.modules.train.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 手艺人商家
 * @author 
 * @version 2018年3月26日
 */
public class SyrFranchise extends DataEntity<SyrFranchise>{

	private static final long serialVersionUID = 1L;
	private String userid;		//用户id
	private Integer applyId;		//用户id
	private String applyType;		//申请类型
	private String auditType;	//认证类型
	private String status;		//审核状态（0：待审核，1：未通过，2：已通过）
	private String name;		//用户的姓名
	private String type;		//用户类型
	private String mobile;		//用户的手机号
	private String nickname;		//用户昵称
	private Integer modId;		//版本id
	private Date authEndDate;		//到期日期
	
	
	//----------------手艺人审核信息--train_audituser_info
	private String speciality;			//特长
	private String startDate;			//工作年限
	private String income;			//月收入
	private String city;			//工作城市
	private List<PayAccount> payAccount;			//支付宝微信账户
	private List<BankAccount> bankAccount;			//银行账户
	private CheckAddr addr;				//地址详细
	
	
	public SyrFranchise() {
		super();
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getApplyId() {
		return applyId;
	}
	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public List<PayAccount> getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(List<PayAccount> payAccount) {
		this.payAccount = payAccount;
	}
	public List<BankAccount> getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(List<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}
	public CheckAddr getAddr() {
		return addr;
	}
	public void setAddr(CheckAddr addr) {
		this.addr = addr;
	}
	public Integer getModId() {
		return modId;
	}
	public void setModId(Integer modId) {
		this.modId = modId;
	}
	public Date getAuthEndDate() {
		return authEndDate;
	}
	public void setAuthEndDate(Date authEndDate) {
		this.authEndDate = authEndDate;
	}
	
}
