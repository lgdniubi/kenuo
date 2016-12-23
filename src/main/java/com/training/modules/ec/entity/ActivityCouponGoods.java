package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 红包指定商品
 * @author water
 *
 */
public class ActivityCouponGoods extends TreeEntity<ActivityCouponGoods>{

	private static final long serialVersionUID = 1L;
	private int couponId;           //红包ID
	private int goodsId;            //商品ID
	private String goodName;		//商品名称


	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}


	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	@Override
	public ActivityCouponGoods getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(ActivityCouponGoods parent) {
		// TODO Auto-generated method stub
		
	}

}
