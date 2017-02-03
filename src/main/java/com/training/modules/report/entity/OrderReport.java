package com.training.modules.report.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 订单报表
 * 
 * @author yangyang
 *
 */
public class OrderReport extends TreeEntity<OrderReport> {

	private static final long serialVersionUID = 1L;
	private int orderNum; 				// 订单数量
	private int amount; 				// 订单价格
	private int userNum; 				// 用户数量
	private int goodNum; 				// 商品数量
	private int totalMoney; 			// 商品总价
	private String addTime; 				// 订单时间
	private String begtime;				//查询开始时间
	private String endtime;				//查询结束时间
	private int avAmount;				//平均订单价格
	private int avTotalMoney;			//商品平均价格
	
	
	
	public int getAvAmount() {
		return avAmount;
	}

	public void setAvAmount(int avAmount) {
		this.avAmount = avAmount;
	}

	public int getAvTotalMoney() {
		return avTotalMoney;
	}

	public void setAvTotalMoney(int avTotalMoney) {
		this.avTotalMoney = avTotalMoney;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getGoodNum() {
		return goodNum;
	}

	public void setGoodNum(int goodNum) {
		this.goodNum = goodNum;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	

	public String getBegtime() {
		return begtime;
	}

	public void setBegtime(String begtime) {
		this.begtime = begtime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Override
	public OrderReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(OrderReport parent) {
		// TODO Auto-generated method stub

	}

}
