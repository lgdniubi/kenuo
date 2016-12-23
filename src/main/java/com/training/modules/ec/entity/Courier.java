package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 快递物流
 * @author yangyang
 *
 */
public class Courier extends TreeEntity<Courier> {
	
	
	private static final long serialVersionUID = 1L;
	private int courierId;				//快递物流id
	private String startingPoint;		//发货地点
	private String courierName;			//快递名称
	private int type;					//类型
	private int defaultWeight;			//默认首重
	private double defaultPrice;		//默认价格
	private int defaultAdd;			//默认续重
	private double defaultAddPrice;	//默认续重价格

	private Date createDate;			//创建日期
	
	
	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public String getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(String startingPoint) {
		this.startingPoint = startingPoint;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getDefaultWeight() {
		return defaultWeight;
	}

	public void setDefaultWeight(int defaultWeight) {
		this.defaultWeight = defaultWeight;
	}

	public double getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public int getDefaultAdd() {
		return defaultAdd;
	}

	public void setDefaultAdd(int defaultAdd) {
		this.defaultAdd = defaultAdd;
	}

	public double getDefaultAddPrice() {
		return defaultAddPrice;
	}

	public void setDefaultAddPrice(double defaultAddPrice) {
		this.defaultAddPrice = defaultAddPrice;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public Courier getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Courier parent) {
		// TODO Auto-generated method stub
		
	}
	

}
