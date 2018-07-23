package com.training.modules.track.entity;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：  	TOrderGoodsRecharge
 * 类描述：	埋点-订单商品充值记录
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午3:48:57
 */
public class TOrderGoodsRecharge extends DataEntity<TOrderGoodsRecharge> {
	
	private static final long serialVersionUID = -6871499465731124505L;
	// 用户ID
	private Integer userId;
	// 订单号
	private String orderId;	
	// 订单商品ID
	private Integer recId;
	// 商品ID
	private String goodsId;
	// 商品名称
	private String goodsName;
	// 商品预约金价格
	private double goodsAdvancePrice;
	// 商品总价
	private double goodsPrice;
	// 商品欠款
	private double goodsArrears;
	// 商品余额
	private double orderUnconsumed;
	// 总付款金额
	private double  orderTotalRecharge;
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the recId
	 */
	public Integer getRecId() {
		return recId;
	}
	/**
	 * @param recId the recId to set
	 */
	public void setRecId(Integer recId) {
		this.recId = recId;
	}
	/**
	 * @return the goodsId
	 */
	public String getGoodsId() {
		return goodsId;
	}
	/**
	 * @param goodsId the goodsId to set
	 */
	public void setGoodsId(String goodsId) {
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
	 * @return the goodsAdvancePrice
	 */
	public double getGoodsAdvancePrice() {
		return goodsAdvancePrice;
	}
	/**
	 * @param goodsAdvancePrice the goodsAdvancePrice to set
	 */
	public void setGoodsAdvancePrice(double goodsAdvancePrice) {
		this.goodsAdvancePrice = goodsAdvancePrice;
	}
	/**
	 * @return the goodsPrice
	 */
	public double getGoodsPrice() {
		return goodsPrice;
	}
	/**
	 * @param goodsPrice the goodsPrice to set
	 */
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	/**
	 * @return the goodsArrears
	 */
	public double getGoodsArrears() {
		return goodsArrears;
	}
	/**
	 * @param goodsArrears the goodsArrears to set
	 */
	public void setGoodsArrears(double goodsArrears) {
		this.goodsArrears = goodsArrears;
	}
	/**
	 * @return the orderUnconsumed
	 */
	public double getOrderUnconsumed() {
		return orderUnconsumed;
	}
	/**
	 * @param orderUnconsumed the orderUnconsumed to set
	 */
	public void setOrderUnconsumed(double orderUnconsumed) {
		this.orderUnconsumed = orderUnconsumed;
	}
	/**
	 * @return the orderTotalRecharge
	 */
	public double getOrderTotalRecharge() {
		return orderTotalRecharge;
	}
	/**
	 * @param orderTotalRecharge the orderTotalRecharge to set
	 */
	public void setOrderTotalRecharge(double orderTotalRecharge) {
		this.orderTotalRecharge = orderTotalRecharge;
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
}
