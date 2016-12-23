package com.training.modules.report.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 商品排行报表
 * 
 * @author yangyang
 *
 */
public class GoodsReport extends TreeEntity<GoodsReport> {

	private static final long serialVersionUID = 1L;
	private int goodsId; 				// 商品Id
	private String goodsName; 			// 商品名称
	private int goodsNum; 				// 商品数量
	private int amount;				//订单价格
	private int userNum;				//用户数量
	private String addTime; 			// 订单时间
	private Date begtime;				//查询开始时间
	private Date endtime;				//查询结束时间
	private int avTotalMoney;			//商品平均价格
	
	
	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	
	public Date getBegtime() {
		return begtime;
	}

	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public int getAvTotalMoney() {
		return avTotalMoney;
	}

	public void setAvTotalMoney(int avTotalMoney) {
		this.avTotalMoney = avTotalMoney;
	}

	@Override
	public GoodsReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(GoodsReport parent) {
		// TODO Auto-generated method stub

	}

}
