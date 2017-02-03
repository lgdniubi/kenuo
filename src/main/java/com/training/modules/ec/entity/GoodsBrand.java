package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 每天美耶-商品品牌表
 * 
 * @author kele
 * @version 2016-6-15
 */
public class GoodsBrand extends DataEntity<GoodsBrand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int bandId; 			// 品牌表ID
	private GoodsCategory goodsCategory;//商品分类
	private String goodsCategoryId;	//商品分类ID
	private String name; 			// 品牌名称
	private String logo; 			// 品牌logo
	private String brandDesc; 		// 品牌描述
	private String url; 			// 品牌地址
	private String area;			// 品牌产地
	private int sort; 				// 排序
	private int isHot; 				// 是否推荐
	
	/**
	 * GET/SET
	 */
	public int getBandId() {
		return bandId;
	}
	public void setBandId(int bandId) {
		this.bandId = bandId;
	}
	public GoodsCategory getGoodsCategory() {
		return goodsCategory;
	}
	public void setGoodsCategory(GoodsCategory goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	public String getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(String goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
}
