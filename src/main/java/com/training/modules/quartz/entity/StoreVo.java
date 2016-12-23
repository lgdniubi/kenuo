package com.training.modules.quartz.entity;

import com.training.common.persistence.BaseEntity;

public class StoreVo extends BaseEntity<StoreVo> {

	private static final long serialVersionUID = -8056319221091325278L;
	private int  goodsid;
	private int storecount;
	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public int getStorecount() {
		return storecount;
	}

	public void setStorecount(int storecount) {
		this.storecount = storecount;
	}

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

}
