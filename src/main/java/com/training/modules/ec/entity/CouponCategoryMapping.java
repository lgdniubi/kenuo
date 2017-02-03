package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷商品中间
 * @author water
 *
 */
public class CouponCategoryMapping extends TreeEntity<CouponCategoryMapping>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int couponId;           //优惠券ID
	private int categoryId;         //分类Id
	private String categoryName;	 //分类名称



	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public CouponCategoryMapping getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CouponCategoryMapping parent) {
		// TODO Auto-generated method stub
		
	}

}
