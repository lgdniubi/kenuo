package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 店务报表Entity
 * @author coffee
 *
 */
public class ShopReport extends DataEntity<ShopReport>{
	private static final long serialVersionUID = 1L;
	private int userId;					//用户ID
	private String shopId;				//店铺ID
	private String parentIds;			//父类IDS
	private int year;					//年
	private int quarter;				//季度
	private int month;					//月
	private int week;					//当年所在的周数
	private int day;					//日
	private double consumeSum;			//用户总消费额
	private double arrearsSum;			//用户总欠款
	private int apptTimes;				//预约次数
	
	private int isExist;				//是否存在(1 存在、0不存在)   消耗是否超上限(0 否、1 是) 
	private Date date;					//
	
	// 业绩
	private int mappingId;				//mappingId
	private double basisFee;			//总消耗业绩
	private double totalAmount;			//实付金额
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public double getConsumeSum() {
		return consumeSum;
	}
	public void setConsumeSum(double consumeSum) {
		this.consumeSum = consumeSum;
	}
	public double getArrearsSum() {
		return arrearsSum;
	}
	public void setArrearsSum(double arrearsSum) {
		this.arrearsSum = arrearsSum;
	}
	public int getApptTimes() {
		return apptTimes;
	}
	public void setApptTimes(int apptTimes) {
		this.apptTimes = apptTimes;
	}
	public int getIsExist() {
		return isExist;
	}
	public void setIsExist(int isExist) {
		this.isExist = isExist;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getMappingId() {
		return mappingId;
	}
	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}
	public double getBasisFee() {
		return basisFee;
	}
	public void setBasisFee(double basisFee) {
		this.basisFee = basisFee;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
