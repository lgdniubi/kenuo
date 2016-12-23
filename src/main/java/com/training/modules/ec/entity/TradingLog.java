package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;


/**
 * 邀请红包明细
 * @author yangyang
 *
 */

public class TradingLog extends TreeEntity<TradingLog>{

	private static final long serialVersionUID = 1L;
	private int userId;					//用户id
	private String userName;				//用户姓名
	private double money;					//交易金额
	private int status;					//状态 1 收入 2支出
	private String type;					//类型 1 是邀请提成 2 购买支出
	private String orderId;					//订单id
	private Date tradingTime;				//交易日期
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getTradingTime() {
		return tradingTime;
	}
	public void setTradingTime(Date tradingTime) {
		this.tradingTime = tradingTime;
	}
	@Override
	public TradingLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(TradingLog parent) {
		// TODO Auto-generated method stub
		
	}
	

}
