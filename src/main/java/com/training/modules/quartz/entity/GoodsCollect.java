package com.training.modules.quartz.entity;

import com.training.common.persistence.BaseEntity;

public class GoodsCollect extends BaseEntity<GoodsCollect> {

	private static final long serialVersionUID = 5555434039026476632L;
	private int collectid;
	private int goodsid;
	private int userid;
	public int getCollectid() {
		return collectid;
	}

	public void setCollectid(int collectid) {
		this.collectid = collectid;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
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
