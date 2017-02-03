package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 红包指定分类entiy
 * @author water
 *
 */
public class ActivityCouponCategory extends TreeEntity<ActivityCouponCategory>{
	

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
	public ActivityCouponCategory getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(ActivityCouponCategory parent) {
		// TODO Auto-generated method stub
		
	}

}
