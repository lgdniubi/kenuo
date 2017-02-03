package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 操作日志实体
 * @author water
 *
 */
public class GoodsDetailSum extends TreeEntity<GoodsDetailSum>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int recId;
	private double singleRealityPrice;
	private int goodsNum;
	private double orderAmount;  
	private double totalAmount;
	private double isFre;
	private double detaiAmount;
	private int times;
	
	
	
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	public double getSingleRealityPrice() {
		return singleRealityPrice;
	}
	public void setSingleRealityPrice(double singleRealityPrice) {
		this.singleRealityPrice = singleRealityPrice;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getIsFre() {
		return isFre;
	}
	public void setIsFre(double isFre) {
		this.isFre = isFre;
	}
	public double getDetaiAmount() {
		return detaiAmount;
	}
	public void setDetaiAmount(double detaiAmount) {
		this.detaiAmount = detaiAmount;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	@Override
	public GoodsDetailSum getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(GoodsDetailSum parent) {
		// TODO Auto-generated method stub
		
	}

	

}
