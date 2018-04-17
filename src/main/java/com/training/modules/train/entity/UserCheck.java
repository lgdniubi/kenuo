package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户审核
 * @author 
 * @version 2018年3月26日
 */
public class UserCheck extends DataEntity<UserCheck>{

	private static final long serialVersionUID = 1L;
	private String userid;		//用户id
	private String applyType;		//申请类型
	private String auditType;	//认证类型
	private String status;		//审核状态（0：待审核，1：未通过，2：已通过）
	private String name;		//用户的姓名
	private String type;		//用户类型
	private String mobile;		//用户的手机号
	private String nickname;		//用户昵称
	
	//---------------企业审核信息-------------
	private String companyName;		//企业名称
	private String shortName;		//企业简称
	private String charterCard;		//执照编号
	private String address;			//详情地址
	private String legalPerson;		//法人
	private String legalMobile;		//法人手机号
	private String legalCard;		//法人身份证号
	private String icardone;		//身份证（正）
	private String icardtwo;		//身份证（反）
	private String intro;			//企业介绍
	
	public UserCheck() {
		super();
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCharterCard() {
		return charterCard;
	}
	public void setCharterCard(String charterCard) {
		this.charterCard = charterCard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getLegalMobile() {
		return legalMobile;
	}
	public void setLegalMobile(String legalMobile) {
		this.legalMobile = legalMobile;
	}
	public String getLegalCard() {
		return legalCard;
	}
	public void setLegalCard(String legalCard) {
		this.legalCard = legalCard;
	}
	public String getIcardone() {
		return icardone;
	}
	public void setIcardone(String icardone) {
		this.icardone = icardone;
	}
	public String getIcardtwo() {
		return icardtwo;
	}
	public void setIcardtwo(String icardtwo) {
		this.icardtwo = icardtwo;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
}
