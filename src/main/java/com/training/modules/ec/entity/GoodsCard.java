package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 每天美耶-卡项子项表
 * 
 * @author 土豆
 * @version 2017-7-26
 */
public class GoodsCard extends DataEntity<GoodsCard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int goodsCardId; 		// 主键ID
	private int cardId; 			// 卡项ID
	private int goodsId; 			// 商品ID
	private String goodsName; 		// 商品名称
	private int goodsNum; 			// 次（个）数
	private int serviceMin; 		// 服务时长（虚拟商品）
	private String isReal; 			// 是否为实物（0：实物；1：虚拟；）
	private double marketPrice;		// 市场单价
	private double price; 			// 优惠价
	private double totalMarketPrice;// 市场价合计
	private double totalPrice; 		// 优惠价合计
	
	
	
	public int getGoodsCardId() {
		return goodsCardId;
	}
	public void setGoodsCardId(int goodsCardId) {
		this.goodsCardId = goodsCardId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
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
	public int getServiceMin() {
		return serviceMin;
	}
	public void setServiceMin(int serviceMin) {
		this.serviceMin = serviceMin;
	}
	public String getIsReal() {
		return isReal;
	}
	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}
	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
