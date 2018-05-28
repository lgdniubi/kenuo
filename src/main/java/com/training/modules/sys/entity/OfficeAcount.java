package com.training.modules.sys.entity;
/**
 * 账户
 * @author QJL
 *
 */
public class OfficeAcount {

	private String officeId; //机构id
	private double amount = 0;	//金额
	private int integralEarnings = 0; //贡献登云平台总云币
	private double creditLimit = 0;	//信用额度
	private double usedLimit = 0;	//已用额度
	private double payPwd;	//支付密码
	
	
	
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
	
	
}
