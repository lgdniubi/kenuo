package com.training.modules.report.entity;

import com.training.common.persistence.DataEntity;

/**
 * 报表客户统计实体类
 * @author coffee
 *
 */
public class UserStatisticsReport extends DataEntity<UserStatisticsReport>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int userNum;		//注册用户
	private int orderUserNum;	//有订单用户数
	private double buyRate;		//购买率
	
	private double amount;		//会员购物总额
	private double orderNum;	//每会员订单数
	private double userBuyRate;	//每会员购物额
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public int getOrderUserNum() {
		return orderUserNum;
	}
	public void setOrderUserNum(int orderUserNum) {
		this.orderUserNum = orderUserNum;
	}
	public double getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(double buyRate) {
		this.buyRate = buyRate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(double orderNum) {
		this.orderNum = orderNum;
	}
	public double getUserBuyRate() {
		return userBuyRate;
	}
	public void setUserBuyRate(double userBuyRate) {
		this.userBuyRate = userBuyRate;
	}
}
