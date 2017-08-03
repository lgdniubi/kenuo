package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;
/**
 * 订单商品详情记录表
 * @author dalong
 *
 */
public class OrderGoodsDetails extends TreeEntity<OrderGoodsDetails> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;			//主订单id
	private String goodsMappingId;	//匹配商品id
	private double totalAmount;		//实付款金额
	private double orderBalance;	//订单余款
	private double orderArrearage;	//订单欠款
	private double itemAmount;		//项目金额
	private double itemCapitalPool;	//项目资金池
	private int serviceTimes;		//剩余服务次数
	private int type;				//详情类型（0：充值；1：使用；2：退款）
	
	private double appTotalAmount;   //app实付金额
	private double appArrearage;      //app欠款金额
	private double orderAmount;       //应付金额（讨价还价的价格）
	
	private double goodsPrice;       //商品的价格
	
	private String createByName;	//操作人
	
	private String advanceFlag;     //（0：否；1：是预约金，2：处理预约金，3：充值，4：退货）
	
	private double surplusAmount;   //套卡剩余金额
	
	private int integral;            //充值全部或者处理预约金等于全部价钱时赠送的云币，对应于mapping中的integral
	private double sumOrderArrearage;    //待付尾款（充值全部或者处理预约金等于全部价钱时赠送云币时用的）
	
	public double getSurplusAmount() {
		return surplusAmount;
	}

	public void setSurplusAmount(double surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGoodsMappingId() {
		return goodsMappingId;
	}

	public void setGoodsMappingId(String goodsMappingId) {
		this.goodsMappingId = goodsMappingId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getOrderBalance() {
		return orderBalance;
	}

	public void setOrderBalance(double orderBalance) {
		this.orderBalance = orderBalance;
	}

	public double getOrderArrearage() {
		return orderArrearage;
	}

	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}

	public double getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public double getItemCapitalPool() {
		return itemCapitalPool;
	}

	public void setItemCapitalPool(double itemCapitalPool) {
		this.itemCapitalPool = itemCapitalPool;
	}

	public int getServiceTimes() {
		return serviceTimes;
	}

	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	public double getAppTotalAmount() {
		return appTotalAmount;
	}

	public void setAppTotalAmount(double appTotalAmount) {
		this.appTotalAmount = appTotalAmount;
	}

	public double getAppArrearage() {
		return appArrearage;
	}

	public void setAppArrearage(double appArrearage) {
		this.appArrearage = appArrearage;
	}

	
	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	
	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	@Override
	public OrderGoodsDetails getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(OrderGoodsDetails parent) {
		// TODO Auto-generated method stub
		
	}

	public String getAdvanceFlag() {
		return advanceFlag;
	}

	public void setAdvanceFlag(String advanceFlag) {
		this.advanceFlag = advanceFlag;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public double getSumOrderArrearage() {
		return sumOrderArrearage;
	}

	public void setSumOrderArrearage(double sumOrderArrearage) {
		this.sumOrderArrearage = sumOrderArrearage;
	}
	
}
