package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

/**
 * 分销  用户订单关系  实体类
 * @author coffee
 *
 */
public class MtmySaleRelieveLog extends DataEntity<MtmySaleRelieveLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Users users;			//mtmy用户
	private User user;				//妃子笑用户
	private Orders orders;			//订单实体类
	
	private int rebateUser;			//返利人（每天美耶用户）
	private int receiveUser;		//收利人（妃子校用户、每天美耶用户）
	private String depth;			//返利关系类型（AB、AC、BC、CD）
	private double balancePercent;	//返利余额比例
	private double balanceAmount;	//返利余额金额
	private double integralPercent;	//返利云币比例
	private int integralAmount;		//返利云币枚数
	private int rebateFlag;			//返利标识（0：未结算；1：已结算）
	private String rabateDate;		//返利时间（当天返利昨天订单）YYYYMMDD
	
	private double newBalanceAmount;	//未结算返利余额金额
	private int newIntegralAmount;		//未结算返利云币枚数
	
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Orders getOrders() {
		return orders;
	}
	public void setOrders(Orders orders) {
		this.orders = orders;
	}
	public int getRebateUser() {
		return rebateUser;
	}
	public void setRebateUser(int rebateUser) {
		this.rebateUser = rebateUser;
	}
	public int getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(int receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public double getBalancePercent() {
		return balancePercent;
	}
	public void setBalancePercent(double balancePercent) {
		this.balancePercent = balancePercent;
	}
	public double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public double getIntegralPercent() {
		return integralPercent;
	}
	public void setIntegralPercent(double integralPercent) {
		this.integralPercent = integralPercent;
	}
	public int getIntegralAmount() {
		return integralAmount;
	}
	public void setIntegralAmount(int integralAmount) {
		this.integralAmount = integralAmount;
	}
	public int getRebateFlag() {
		return rebateFlag;
	}
	public void setRebateFlag(int rebateFlag) {
		this.rebateFlag = rebateFlag;
	}
	public String getRabateDate() {
		return rabateDate;
	}
	public void setRabateDate(String rabateDate) {
		this.rabateDate = rabateDate;
	}
	public double getNewBalanceAmount() {
		return newBalanceAmount;
	}
	public void setNewBalanceAmount(double newBalanceAmount) {
		this.newBalanceAmount = newBalanceAmount;
	}
	public int getNewIntegralAmount() {
		return newIntegralAmount;
	}
	public void setNewIntegralAmount(int newIntegralAmount) {
		this.newIntegralAmount = newIntegralAmount;
	}
}
    