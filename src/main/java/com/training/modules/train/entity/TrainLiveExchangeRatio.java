package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 直播兑换比例配置实体类
 * @author xiaoye  2017年3月14日
 *
 */
public class TrainLiveExchangeRatio extends DataEntity<TrainLiveExchangeRatio>{

	private static final long serialVersionUID = 1L;

	private int exchangeRatioId;   //id
	private int activityId;        //对应的活动的id
	private String name;           //名称
	private String exchangeType;   //兑换类型（0：人民币换云币；1：佣金换云币）
	private double exchangePrice;  //兑换值（人民币or佣金）
	private int integrals;         //云币
	private int sort;              //排序
	private String isShow;         //是否显示（0：正常；1：隐藏）
	
	
	public int getExchangeRatioId() {
		return exchangeRatioId;
	}
	public void setExchangeRatioId(int exchangeRatioId) {
		this.exchangeRatioId = exchangeRatioId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public double getExchangePrice() {
		return exchangePrice;
	}
	public void setExchangePrice(double exchangePrice) {
		this.exchangePrice = exchangePrice;
	}
	public int getIntegrals() {
		return integrals;
	}
	public void setIntegrals(int integrals) {
		this.integrals = integrals;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	
	
}
