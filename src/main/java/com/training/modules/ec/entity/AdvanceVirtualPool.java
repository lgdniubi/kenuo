package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 预付款虚拟池实体类
 * @author 小叶  2017-1-10 
 *
 */
public class AdvanceVirtualPool extends DataEntity<AdvanceVirtualPool>{
	
	private static final long serialVersionUID = 1L;

	private String orderId;    //订单号
	private int recId;    //虚拟订单id
	private int goodsId;     //商品id
	private String goodsName;  //商品名称
	private String specKeyName;     //商品规格
	private String goodsSn;     //商品编号
	private int goodsNum;    //购买数量
	private double orderAmount;     //应付款
	private double totalAmount;     //实付款
	private double orderArrearage;  //欠款
	private int serviceTimes;        //预计成交次数
	private int payTimes;           //购买次数
	private int useTimes;             //消耗次数
	private double actualUseFee;      //实际消耗业绩
	private String officeId;         //权限id(当前登录用户的office的id)
	
	@ExcelField(title="订单号", type=1, align=2, sort=10)
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@ExcelField(title="虚拟订单号", type=1, align=2, sort=20)
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	
	@ExcelField(title="商品id", type=1, align=2, sort=30)
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	
	@ExcelField(title="商品名称", type=1, align=2, sort=40)
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	@ExcelField(title="商品规格", type=1, align=2, sort=50)
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	
	@ExcelField(title="商品编号", type=1, align=2, sort=60)
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	
	@ExcelField(title="购买数量", type=1, align=2, sort=70)
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	
	@ExcelField(title="应付款", type=1, align=2, sort=80)
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	@ExcelField(title="实付款", type=1, align=2, sort=90)
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@ExcelField(title="欠款", type=1, align=2, sort=100)
	public double getOrderArrearage() {
		return orderArrearage;
	}
	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}
	
	@ExcelField(title="预计成交次数", type=1, align=2, sort=110)
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	
	@ExcelField(title="购买次数", type=1, align=2, sort=120)
	public int getPayTimes() {
		return payTimes;
	}
	public void setPayTimes(int payTimes) {
		this.payTimes = payTimes;
	}
	
	@ExcelField(title="消耗次数", type=1, align=2, sort=130)
	public int getUseTimes() {
		return useTimes;
	}
	public void setUseTimes(int useTimes) {
		this.useTimes = useTimes;
	}
	
	@ExcelField(title="实际消耗业绩", type=1, align=2, sort=140)
	public double getActualUseFee() {
		return actualUseFee;
	}
	public void setActualUseFee(double actualUseFee) {
		this.actualUseFee = actualUseFee;
	}
	
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
}
