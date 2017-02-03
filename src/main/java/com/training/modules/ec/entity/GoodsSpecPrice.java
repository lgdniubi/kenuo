package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品规格价格表
 * @author kele
 * @version 2016-6-22
 */
public class GoodsSpecPrice extends DataEntity<GoodsSpecPrice> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goodsId;			// 商品id
	private String specKey;			// 规格键
	private String specKeyValue;	// 规格键名中文
	private double price;			// 优惠价格
	private double marketPrice;		// 市场价格
	private int storeCount;			// 库存数量
	private String barCode;			// 商品条形码
	private String goodsNo;			//商品编码
	private int serviceTimes;		// 服务次数
	private int expiringDate;		// 截止日期（月）
	
	private double costPrice;       //成本价格
	/**
	 * get/set
	 */
	
	public String getGoodsId() {
		return goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getSpecKey() {
		return specKey;
	}
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}
	public String getSpecKeyValue() {
		return specKeyValue;
	}
	public void setSpecKeyValue(String specKeyValue) {
		this.specKeyValue = specKeyValue;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getStoreCount() {
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public int getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(int expiringDate) {
		this.expiringDate = expiringDate;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	
}
