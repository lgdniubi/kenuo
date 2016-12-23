package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

public class OrderGoodsCoupon extends DataEntity<OrderGoodsCoupon> {

	private static final long serialVersionUID = 1L;
	private int actionId;					//活动id
	private String actionName;				//活动名称
	private int counponId;					//红包id
	private String counponName;				//红包名称
	private int type;						//红包类型
	private int usedType;					//红包使用类型
	private double baseAmount;		 		//满减金额
	private double couponMoney;	 		//金额
	
	
	
	
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public int getCounponId() {
		return counponId;
	}
	public void setCounponId(int counponId) {
		this.counponId = counponId;
	}
	public String getCounponName() {
		return counponName;
	}
	public void setCounponName(String counponName) {
		this.counponName = counponName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUsedType() {
		return usedType;
	}
	public void setUsedType(int usedType) {
		this.usedType = usedType;
	}
	public double getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(double baseAmount) {
		this.baseAmount = baseAmount;
	}
	public double getCouponMoney() {
		return couponMoney;
	}
	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}
	
	
	
}
