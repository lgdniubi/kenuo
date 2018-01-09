package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷表
 * @author water
 *
 */
public class Activity extends TreeEntity<Activity> {
	
	
	private static final long serialVersionUID = 1L;
	private String name;						//活动名称
	private int franchiseeId;					//所属机构
	private int actionType;					//活动类型：1 红包活动 2 抢购活动 9 投票
	private Date showTime;						//抢购活动展示的时间
	private Date closeTime;						//抢购活动关闭时间
	private Date startTime;						//红包领取时间
	private Date endTime;						//红包结束时间
	private Date expirationDate;				//红包有效期
	private int status;						//状态 1 开启，2关闭，3 结束
	private Date createDate;					//创建时间
	//private String createBy;					//创建人
	private String franchiseeName;				//所属机构名称
	private String cateId;						//分类id集合
	private String cateName;					//分类名称集合
	private String goodsId;						//商品id
	private String goodsName;					//商品name
	
	private String couponId;					//红包id
	private String couponName;					//红包名称		
	private String userId;						//用户id
	private String mobileNum;					//多用户手机号
	private int sendType;						//推送方式
	
	private String moreType;                    //多用户推送方式（0：手机号；1：用户ID）
	
	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFranchiseeId() {
		return franchiseeId;
	}

	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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

	public String getFranchiseeName() {
		return franchiseeName;
	}

	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}

	@Override
	public Activity getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Activity parent) {
		// TODO Auto-generated method stub
		
	}

	public String getMoreType() {
		return moreType;
	}

	public void setMoreType(String moreType) {
		this.moreType = moreType;
	}
	

}
