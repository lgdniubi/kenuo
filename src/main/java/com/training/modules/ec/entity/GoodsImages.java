package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品相册
 * @author kele
 * @version 2016-6-22
 */
public class GoodsImages extends DataEntity<GoodsImages> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String imgId;			// 图片ID
	private String goodsId;			// 商品id
	private String imageUrl;		// 图片地址
	private int imageType;			// 图片类型[0　是商品图　　1　护理流程图]
	private String imageDesc;		// 图片描述
	private String sort;			// 排序
	
	/**
	 * get/set
	 */
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getImageType() {
		return imageType;
	}
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	public String getImageDesc() {
		return imageDesc;
	}
	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
