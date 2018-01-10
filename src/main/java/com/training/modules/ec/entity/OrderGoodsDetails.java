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
	private String createOfficeId;   //创建者机构id
	
	private double appTotalAmount;   //app实付金额
	private double appArrearage;      //app欠款金额
	private double orderAmount;       //应付金额（讨价还价的价格）
	
	private double goodsPrice;       //商品的价格
	private int goodsNum;            //购买的数量
	
	private String createByName;	//操作人
	
	private String advanceFlag;     //（0：否；1：是预约金，2：处理预约金，3：充值，4：退货）
	
	private double surplusAmount;   //套卡剩余金额
	
	private int integral;            //充值全部或者处理预约金等于全部价钱时赠送的云币，对应于mapping中的integral
	private double sumOrderArrearage;    //待付尾款（充值全部或者处理预约金等于全部价钱时赠送云币时用的）
	
	private double couponPrice;         //红包面值
	private double memberGoodsPrice;     //会员折扣优惠了多少钱
	private double advancePrice;         //预约金
	
	private String belongOfficeId;           //归属机构id
	private double useBalance;              //使用账户余额
	
	private String returnedId;              //退货订单ID
	
	
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

	public double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public double getMemberGoodsPrice() {
		return memberGoodsPrice;
	}

	public void setMemberGoodsPrice(double memberGoodsPrice) {
		this.memberGoodsPrice = memberGoodsPrice;
	}

	public double getAdvancePrice() {
		return advancePrice;
	}

	public void setAdvancePrice(double advancePrice) {
		this.advancePrice = advancePrice;
	}

	public String getCreateOfficeId() {
		return createOfficeId;
	}

	public void setCreateOfficeId(String createOfficeId) {
		this.createOfficeId = createOfficeId;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getBelongOfficeId() {
		return belongOfficeId;
	}

	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}

	public double getUseBalance() {
		return useBalance;
	}

	public void setUseBalance(double useBalance) {
		this.useBalance = useBalance;
	}

	public String getReturnedId() {
		return returnedId;
	}

	public void setReturnedId(String returnedId) {
		this.returnedId = returnedId;
	}
	
}
