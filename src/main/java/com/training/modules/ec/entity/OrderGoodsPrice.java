package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

public class OrderGoodsPrice extends DataEntity<OrderGoodsPrice> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double youprice;			//优惠价格
	private double totlprice;			//总价价格
	private double shippingPrice;		//邮费
	private String name;				//姓名
	private String phone;				//手机号
	private String address;				//收货地址
	private String orderid;				//订单编号
	
	
	
	public double getShippingPrice() {
		return shippingPrice;
	}
	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public double getYouprice() {
		return youprice;
	}
	public void setYouprice(double youprice) {
		this.youprice = youprice;
	}
	public double getTotlprice() {
		return totlprice;
	}
	public void setTotlprice(double totlprice) {
		this.totlprice = totlprice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
