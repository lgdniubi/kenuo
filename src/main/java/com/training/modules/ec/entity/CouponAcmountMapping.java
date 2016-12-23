package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷金额中间表
 * @author water
 *
 */
public class CouponAcmountMapping extends TreeEntity<CouponAcmountMapping> {
	
	
	private static final long serialVersionUID = 1L;
	private int amountId;						//金额id 
	private int couponId;						//红包id
	private String amountName;					//面值名称
	private double baseAmount;					//满减金额
	private double couponMoney;				//红包金额
	private int totalNumber;					//红包总数量
	private int couponNumber;					//剩余数量
	private int status;						//使用数量
	private Date createDate;					//创建时间
//	private String createBy;					//创建者
	private Date updateDate;					//更新时间
//	private String updateBy;					//更新人
	private int dateCompare;					//日期比较
	
	
	
	public int getDateCompare() {
		return dateCompare;
	}

	public void setDateCompare(int dateCompare) {
		this.dateCompare = dateCompare;
	}

	public int getAmountId() {
		return amountId;
	}

	public String getAmountName() {
		return amountName;
	}

	public void setAmountName(String amountName) {
		this.amountName = amountName;
	}

	public void setAmountId(int amountId) {
		this.amountId = amountId;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
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

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getCouponNumber() {
		return couponNumber;
	}

	public void setCouponNumber(int couponNumber) {
		this.couponNumber = couponNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Override
	public CouponAcmountMapping getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CouponAcmountMapping parent) {
		// TODO Auto-generated method stub
		
	}
	

}
