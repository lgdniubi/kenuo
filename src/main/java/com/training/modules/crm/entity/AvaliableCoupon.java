package com.training.modules.crm.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * kenuo
 * 寄存记录的实体类
 * @author：sharp @date：2017年3月15日
 */
public class AvaliableCoupon extends DataEntity<AvaliableCoupon> {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String couponName;//红包名字
	private String 	baseAmount;//满减金额
	private String 	couponMoney;//红包名字
	private String 	couponType;//'红包类型：1：商品详情页；2：活动页 ,3 新注册 4.通用红包',
	private String  usedType;//'使用类型（1：全部商品 2：商品分类；3：指定商品)',
	private Date 	expirationDate;//过期时间
		
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(String baseAmount) {
		this.baseAmount = baseAmount;
	}
	public String getCouponMoney() {
		return couponMoney;
	}
	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getUsedType() {
		return usedType;
	}
	public void setUsedType(String usedType) {
		this.usedType = usedType;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}