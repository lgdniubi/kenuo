package com.training.modules.track.entity;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：	TOrderGoods
 * 类描述：	埋点-订单商品Mapping关系类
 * 创建人： 	kele
 * 创建时间：    	2018年1月19日 下午2:53:54
 */
public class TOrderGoods extends DataEntity<TOrderGoods> {
	private static final long serialVersionUID = -6871499465731124505L;
	
	// 订单商品ID
	private int recId;
	// 订单id
	private String orderId;		
	// 用户id
	private int userId;		
	
	/*商品信息*/
	// 商品id
	private int goodsId;		
	// 商家ID
	private String franchiseeId;
	// 商家名称
	private String franchiseeName;
	// 品牌ID
	private String goodsBrandId;
	// 品牌名称
	private String goodsBrandName;
	// 商品名称
	private String goodsName;		
	// 商品数量
	private int goodsNum;		
	// 规格key
	private String specKey;		
	// 规格名称
	private String specKeyName;	
	// 成本价格
	private double costPrice;
	// 市场价格
	private double marketPrice;
	// 商品价格
	private double goodsPrice;		
	// 折扣率
	private double discount;	
	// 会员折扣优惠了多少
	private double memberGoodsPrice;
	// 优惠价格
	private double couponPrice;	
	
	// 增值信息
	// 活动类型 0 普通订单,1 限时抢购, 2 团购 , 3 促销优惠
	private int actionType;
	// 活动id
	private int actionId;		
	// 0未发货，1已发货，2已换货，3已退货
	private int isSend;			
	// app端显示
	private int isAppshow;	
	// 是否评价  0 未评价  1评价
	private int isComment;		
	// 实物还是虚物 0实物，1虚物
	private int isReal;	
	
	// 价格信息
	// 实付金额
	private double totalAmount;	
	// 应付金额
	private double orderAmount;
	// 售后数量
	private int afterSaleNum;	
	// 商品退货换货状态
	private int isAfterSales;	
	// 退货金额
	private double returnAmount;	
	// 有效期
	private int expiringDate;		
	
	// 实际服务单次价
	private double singleRealityPrice;
	// 单次标价
	private double singleNormPrice;
	// 服务市场
	private int serviceMin;		
	// 预计服务次数
	private int serviceTimes;	
	// 欠款金额
	private double orderArrearage = 0.00; 
	
	// 可开发票数量
	private int notOpenNum;    	
	// 已开发票数量
	private int openNum;   
	// 购物车id
	private String carId;			
	// 定金价格
	private double advancePrice;
	
	// 一级分类
	private String categoryNameOne;
	// 二级分类
	private String categoryNameTwo;
	
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getSpecKey() {
		return specKey;
	}
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getMemberGoodsPrice() {
		return memberGoodsPrice;
	}
	public void setMemberGoodsPrice(double memberGoodsPrice) {
		this.memberGoodsPrice = memberGoodsPrice;
	}
	public double getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public int getIsSend() {
		return isSend;
	}
	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}
	public int getIsAppshow() {
		return isAppshow;
	}
	public void setIsAppshow(int isAppshow) {
		this.isAppshow = isAppshow;
	}
	public int getIsComment() {
		return isComment;
	}
	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}
	public int getIsReal() {
		return isReal;
	}
	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public int getAfterSaleNum() {
		return afterSaleNum;
	}
	public void setAfterSaleNum(int afterSaleNum) {
		this.afterSaleNum = afterSaleNum;
	}
	public int getIsAfterSales() {
		return isAfterSales;
	}
	public void setIsAfterSales(int isAfterSales) {
		this.isAfterSales = isAfterSales;
	}
	public double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public int getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(int expiringDate) {
		this.expiringDate = expiringDate;
	}
	public double getSingleRealityPrice() {
		return singleRealityPrice;
	}
	public void setSingleRealityPrice(double singleRealityPrice) {
		this.singleRealityPrice = singleRealityPrice;
	}
	public double getSingleNormPrice() {
		return singleNormPrice;
	}
	public void setSingleNormPrice(double singleNormPrice) {
		this.singleNormPrice = singleNormPrice;
	}
	public int getServiceMin() {
		return serviceMin;
	}
	public void setServiceMin(int serviceMin) {
		this.serviceMin = serviceMin;
	}
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public double getOrderArrearage() {
		return orderArrearage;
	}
	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}
	public int getNotOpenNum() {
		return notOpenNum;
	}
	public void setNotOpenNum(int notOpenNum) {
		this.notOpenNum = notOpenNum;
	}
	public int getOpenNum() {
		return openNum;
	}
	public void setOpenNum(int openNum) {
		this.openNum = openNum;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public double getAdvancePrice() {
		return advancePrice;
	}
	public void setAdvancePrice(double advancePrice) {
		this.advancePrice = advancePrice;
	}
	public String getCategoryNameOne() {
		return categoryNameOne;
	}
	public void setCategoryNameOne(String categoryNameOne) {
		this.categoryNameOne = categoryNameOne;
	}
	public String getCategoryNameTwo() {
		return categoryNameTwo;
	}
	public void setCategoryNameTwo(String categoryNameTwo) {
		this.categoryNameTwo = categoryNameTwo;
	}
	/**
	 * @return the franchiseeId
	 */
	public String getFranchiseeId() {
		return franchiseeId;
	}
	/**
	 * @param franchiseeId the franchiseeId to set
	 */
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	/**
	 * @return the franchiseeName
	 */
	public String getFranchiseeName() {
		return franchiseeName;
	}
	/**
	 * @param franchiseeName the franchiseeName to set
	 */
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	/**
	 * @return the goodsBrandId
	 */
	public String getGoodsBrandId() {
		return goodsBrandId;
	}
	/**
	 * @param goodsBrandId the goodsBrandId to set
	 */
	public void setGoodsBrandId(String goodsBrandId) {
		this.goodsBrandId = goodsBrandId;
	}
	/**
	 * @return the goodsBrandName
	 */
	public String getGoodsBrandName() {
		return goodsBrandName;
	}
	/**
	 * @param goodsBrandName the goodsBrandName to set
	 */
	public void setGoodsBrandName(String goodsBrandName) {
		this.goodsBrandName = goodsBrandName;
	}   
}
