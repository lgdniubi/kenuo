package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品属性值表
 * @author kele
 * @version 2016-6-22
 */
public class GoodsAttributeMappings extends DataEntity<GoodsAttributeMappings> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int goodsAttrId;			// 表id
	private String goodsId;				// 商品id
	private int attrId;					// 商品属性id
	private String attrValue;			// 属性值
	private String attrPrice;			// 属性价格(目前没有起作用)
	
	/**
	 * get/set
	 */
	public int getGoodsAttrId() {
		return goodsAttrId;
	}
	public void setGoodsAttrId(int goodsAttrId) {
		this.goodsAttrId = goodsAttrId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getAttrId() {
		return attrId;
	}
	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public String getAttrPrice() {
		return attrPrice;
	}
	public void setAttrPrice(String attrPrice) {
		this.attrPrice = attrPrice;
	}
	
}
