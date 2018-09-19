package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 账户
 * @author QJL
 *
 */
public class OfficeAcount extends DataEntity<OfficeAcount> {

	private static final long serialVersionUID = 100009L;
	private String officeId; //机构id
	private String officeName; //机构名称
	private String companyName; //商家名称
	private double amount = 0;	//金额
	private int integralEarnings = 0; //贡献登云平台总云币
	private double creditLimit = 0;	//信用额度
	private double oldCreditLimit = 0;	//修改前信用额度
	private double usedLimit = 0;	//可使用额度
	private double useLimit = 0;	//
	private double payPwd;	//支付密码
	private String lastRefundTime;		//最近还款时间
	
	
	
	public double getUseLimit() {
		return useLimit;
	}
	public void setUseLimit(double useLimit) {
		this.useLimit = useLimit;
	}
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
	public int getIntegralEarnings() {
		return integralEarnings;
	}
	public void setIntegralEarnings(int integralEarnings) {
		this.integralEarnings = integralEarnings;
	}
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	public double getUsedLimit() {
		return usedLimit;
	}
	public void setUsedLimit(double usedLimit) {
		this.usedLimit = usedLimit;
	}
	public double getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(double payPwd) {
		this.payPwd = payPwd;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLastRefundTime() {
		return lastRefundTime;
	}
	public void setLastRefundTime(String lastRefundTime) {
		this.lastRefundTime = lastRefundTime;
	}
	public double getOldCreditLimit() {
		return oldCreditLimit;
	}
	public void setOldCreditLimit(double oldCreditLimit) {
		this.oldCreditLimit = oldCreditLimit;
	}
	
	
	
}
