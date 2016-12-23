package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;


/**
 * 用户领取优惠卷记录
 * @author yangyang
 *
 */

public class CouponUser extends TreeEntity<CouponUser>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;			//用户id
	private String couponId;		//优惠卷id
	private	 String amountId;		//面值金额id
	private String status;			//状态  0 未使用  1 已使用 2 已过期
	private int type;				//类型
	private Date addTime;			//领取时间
	private Date usedTime;			//使用时间
	private String orderId;			//订单Id
	private String name;			//用户昵称
	private double couponMoney;	//红包金额
	private String couponName;		//优惠卷名称
	private double orderAmount;	//订单金额
	private Date begtime;			//查询条件  开始时间
	private Date endtime;			// 结束时间
	
	
	
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getAmountId() {
		return amountId;
	}
	public void setAmountId(String amountId) {
		this.amountId = amountId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	public double getCouponMoney() {
		return couponMoney;
	}
	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	@Override
	public CouponUser getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(CouponUser parent) {
		// TODO Auto-generated method stub
		
	}

}
