package com.training.modules.track.entity;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：	TApptOrder
 * 类描述：	埋点-预约订单实体类
 * 创建人： 	可乐
 * 创建时间：    	2018年7月14日14:08:30
 */
public class TApptOrder extends DataEntity<TApptOrder>{
	private static final long serialVersionUID = 4911352479915075457L;
	
	// 预约ID
	private Integer appId;
	// 商品ID
	private int goodsId;
	// 商品名称
	private String goodsName;
	// 用户ID
	private Integer userId;
	// 用户名称
	private String userName;
	// 美容师ID
	private String beauticianId;
	// 美容师名称
	private String beauticianName;
	// 店铺ID
	private String shopId;
	// 店铺名称
	private String shopName;
	// 预约日期
	private String apptDate;
	// 预约消耗业绩
	private double depletePayMoney;
	// 服务费
	private double serviceCharge;
	// 预约项目类型
	private String depleteGoodsType;
	// 服务次数
	private Integer serviceTimes;
	//是否为实物（0：实物；1：虚拟；2：套卡；3：通用卡）
	private Integer isReal;
	
	/**
	 * @return the appId
	 */
	public Integer getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	/**
	 * @return the goodsId
	 */
	public int getGoodsId() {
		return goodsId;
	}
	/**
	 * @param goodsId the goodsId to set
	 */
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * @param goodsName the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the beauticianId
	 */
	public String getBeauticianId() {
		return beauticianId;
	}
	/**
	 * @param beauticianId the beauticianId to set
	 */
	public void setBeauticianId(String beauticianId) {
		this.beauticianId = beauticianId;
	}
	/**
	 * @return the beauticianName
	 */
	public String getBeauticianName() {
		return beauticianName;
	}
	/**
	 * @param beauticianName the beauticianName to set
	 */
	public void setBeauticianName(String beauticianName) {
		this.beauticianName = beauticianName;
	}
	/**
	 * @return the shopId
	 */
	public String getShopId() {
		return shopId;
	}
	/**
	 * @param shopId the shopId to set
	 */
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}
	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	/**
	 * @return the apptDate
	 */
	public String getApptDate() {
		return apptDate;
	}
	/**
	 * @param apptDate the apptDate to set
	 */
	public void setApptDate(String apptDate) {
		this.apptDate = apptDate;
	}
	/**
	 * @return the depletePayMoney
	 */
	public double getDepletePayMoney() {
		return depletePayMoney;
	}
	/**
	 * @param depletePayMoney the depletePayMoney to set
	 */
	public void setDepletePayMoney(double depletePayMoney) {
		this.depletePayMoney = depletePayMoney;
	}
	/**
	 * @return the serviceCharge
	 */
	public double getServiceCharge() {
		return serviceCharge;
	}
	/**
	 * @param serviceCharge the serviceCharge to set
	 */
	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	/**
	 * @return the depleteGoodsType
	 */
	public String getDepleteGoodsType() {
		return depleteGoodsType;
	}
	/**
	 * @param depleteGoodsType the depleteGoodsType to set
	 */
	public void setDepleteGoodsType(String depleteGoodsType) {
		this.depleteGoodsType = depleteGoodsType;
	}
	/**
	 * @return the serviceTimes
	 */
	public Integer getServiceTimes() {
		return serviceTimes;
	}
	/**
	 * @param serviceTimes the serviceTimes to set
	 */
	public void setServiceTimes(Integer serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public Integer getIsReal() {
		return isReal;
	}
	public void setIsReal(Integer isReal) {
		this.isReal = isReal;
	}
}
