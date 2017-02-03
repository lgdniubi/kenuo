package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 电商用户账户信息实体类
 * @author coffee
 *
 */
public class UsersAccounts extends DataEntity<UsersAccounts> {
	
	private static final long serialVersionUID = 1L;

	private int userId;							//每天美耶用户ID
	private double userBalance;					//可使用余额
	private double frozenBalance;				//冻结余额
	private double totalBmount;					//已消费金额总计
	private int userIntegral;					//可使用云币
	private double accountBalance;				//账户欠款
	private double accountArrearage;			//账户欠款
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public double getUserBalance() {
		return userBalance;
	}
	public void setUserBalance(double userBalance) {
		this.userBalance = userBalance;
	}
	public double getFrozenBalance() {
		return frozenBalance;
	}
	public void setFrozenBalance(double frozenBalance) {
		this.frozenBalance = frozenBalance;
	}
	public double getTotalBmount() {
		return totalBmount;
	}
	public void setTotalBmount(double totalBmount) {
		this.totalBmount = totalBmount;
	}
	public int getUserIntegral() {
		return userIntegral;
	}
	public void setUserIntegral(int userIntegral) {
		this.userIntegral = userIntegral;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public double getAccountArrearage() {
		return accountArrearage;
	}
	public void setAccountArrearage(double accountArrearage) {
		this.accountArrearage = accountArrearage;
	}
}
