package com.training.modules.personnelfile.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 用户-入职情况及联系方式
 * 
 * @version 2013-05-15
 */
public class UserEmployed extends DataEntity<UserEmployed> {

	private static final long serialVersionUID = 6510590215925352897L;
	

	private String userId;//用户ID
	private String phoneNumber;//手机号码
	private String officeTel;//办公室电话
	private String familyTel;//家庭电话
	private String source;//何处了解公司
	private String introducer;//入职介绍人
	private String introducerTel;//介绍人电话	
	private Date probationStartDate;//试用开始日期
	private Date memberDate;//转正日期
	private Date contractEndDate;//合同截止日期
	private String probationWages;//试用期工资
	private String baseWages;//基本工资
	private String secondWork;//第二工作职业
	private String bankName;//持卡人姓名
	private String bankBeneficiary;//工资卡开户行
	private String bankCode;//账号
	private Date createDate;//创建时间
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getOfficeTel() {
		return officeTel;
	}
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	public String getFamilyTel() {
		return familyTel;
	}
	public void setFamilyTel(String familyTel) {
		this.familyTel = familyTel;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIntroducer() {
		return introducer;
	}
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}
	public String getIntroducerTel() {
		return introducerTel;
	}
	public void setIntroducerTel(String introducerTel) {
		this.introducerTel = introducerTel;
	}
	
	public Date getProbationStartDate() {
		return probationStartDate;
	}
	public void setProbationStartDate(Date probationStartDate) {
		this.probationStartDate = probationStartDate;
	}
	public Date getMemberDate() {
		return memberDate;
	}
	public void setMemberDate(Date memberDate) {
		this.memberDate = memberDate;
	}
	public Date getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public String getProbationWages() {
		return probationWages;
	}
	public void setProbationWages(String probationWages) {
		this.probationWages = probationWages;
	}
	public String getBaseWages() {
		return baseWages;
	}
	public void setBaseWages(String baseWages) {
		this.baseWages = baseWages;
	}
	public String getSecondWork() {
		return secondWork;
	}
	public void setSecondWork(String secondWork) {
		this.secondWork = secondWork;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBeneficiary() {
		return bankBeneficiary;
	}
	public void setBankBeneficiary(String bankBeneficiary) {
		this.bankBeneficiary = bankBeneficiary;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}