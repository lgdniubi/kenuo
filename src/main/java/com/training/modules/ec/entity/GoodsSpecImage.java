package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品规格图片表
 * @author kele
 * @version 2016-6-22
 */
public class GoodsSpecImage extends DataEntity<GoodsSpecImage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goodsId;			// 商品id
	private int specItemId;			// 规格项id
	private String src;				// 商品规格图片路径
	
	/**
	 * get/set
	 */
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getSpecItemId() {
		return specItemId;
	}
	public void setSpecItemId(int specItemId) {
		this.specItemId = specItemId;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
}
