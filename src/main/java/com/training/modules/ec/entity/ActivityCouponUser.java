package com.training.modules.ec.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;


/**
 * 用户领取优惠卷记录
 * @author yangyang
 *
 */

public class ActivityCouponUser extends TreeEntity<ActivityCouponUser>{
	
	
	private static final long serialVersionUID = 1L;
	private String mxId;			//明细id
	private String userId;			//用户id
	private String couponId;		//优惠卷id
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
	
	//导出功能字段
	private String mobile;			//手机号
	private String usedType;		//红包类型
	private String actionId;		//活动id
	private String actionName;		//活动名称
	private String excelStatus;		//状态
	private String orderMoney;		//订单金额
	private String couponAmount;	//红包金额
	
	

	@JsonIgnore
	@ExcelField(title="明细ID", align=2, sort=10)
	public String getMxId() {
		return mxId;
	}
	public void setMxId(String mxId) {
		this.mxId = mxId;
	}
	@JsonIgnore
	@ExcelField(title="用户ID", align=2, sort=15)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonIgnore
	@ExcelField(title="用户名称", align=2, sort=20)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnore
	@ExcelField(title="手机号", align=2, sort=25)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@JsonIgnore
	@ExcelField(title="红包ID", align=2, sort=30)
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	@JsonIgnore
	@ExcelField(title="红包名称", align=2, sort=35)
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	@JsonIgnore
	@ExcelField(title="红包金额", align=2, sort=40)
	public String getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}
	@JsonIgnore
	@ExcelField(title="红包类型", align=2, sort=45)
	public String getUsedType() {
		return usedType;
	}
	public void setUsedType(String usedType) {
		this.usedType = usedType;
	}
	@JsonIgnore
	@ExcelField(title="红包状态", align=2, sort=46)
	public String getExcelStatus() {
		return excelStatus;
	}
	public void setExcelStatus(String excelStatus) {
		this.excelStatus = excelStatus;
	}
	@JsonIgnore
	@ExcelField(title="领取日期", align=2, sort=50)
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@JsonIgnore
	@ExcelField(title="使用日期", align=2, sort=50)
	public Date getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	@JsonIgnore
	@ExcelField(title="订单号", align=2, sort=55)
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@JsonIgnore
	@ExcelField(title="订单金额", align=2, sort=60)
	public String getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	@JsonIgnore
	@ExcelField(title="活动ID", align=2, sort=65)
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	@JsonIgnore
	@ExcelField(title="活动名称", align=2, sort=65)
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getCouponMoney() {
		return couponMoney;
	}
	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}
	@Override
	public ActivityCouponUser getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(ActivityCouponUser parent) {
		// TODO Auto-generated method stub
		
	}

}
