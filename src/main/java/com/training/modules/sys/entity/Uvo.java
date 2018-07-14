package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

public class Uvo{
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userName;
	private String loginName;
	private String mobile;
	private Integer mtmyId;
	private Integer franchiseeId;
	private String officeId;
	private String officeName;
	//报货人
	/*private String cargoUserId;
	private String cargoUserName;
	private String cargoLoginName;
	private String cargoMobile;
	private Integer cargoMtmyId;
	private Integer cargoFranchiseeId;
	private String cargoOfficeId;
	private String cargoOfficeName;*/
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getMtmyId() {
		return mtmyId;
	}
	public void setMtmyId(Integer mtmyId) {
		this.mtmyId = mtmyId;
	}
	public Integer getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(Integer franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	//审核人
/*	private String auditUserId;
	private String auditUserName;
	private String auditLoginName;
	private String auditMobile;
	private Integer auditMtmyId;
	private Integer auditFranchiseeId;
	private String auditOfficeId;
	private String auditOfficeName;*/
	
	//代付人
/*	private String proxyUserId;
	private String proxyUserName;
	private String proxyLoginName;
	private String proxyMobile;
	private Integer proxyMtmyId;
	private Integer proxyFranchiseeId;
	private String proxyOfficeId;
	private String proxyOfficeName;*/
	
}
