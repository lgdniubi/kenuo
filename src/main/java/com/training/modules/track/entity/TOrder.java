package com.training.modules.track.entity;

import java.util.ArrayList;
import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：	TOrder
 * 类描述：	埋点-订单实体类
 * 创建人： 	kele
 * 创建时间：    	2018年1月19日 下午2:53:54
 */
public class TOrder extends DataEntity<TOrder> {
	private static final long serialVersionUID = -6871499465731124505L;
	
	// 订单号
	private String orderId;	
	// 支付订单号
	private String tempOrderId;		
	// 用户id
	private int userId;			
	// 订单状态 ：-2 取消 -1 未支付 1已支付 
	private int orderStatus = -1;		
	// 是否实物（0：实物商品；1：虚拟商品）
	private int isReal = 0;			
	// 支付code
	private String payCode;
	// 支付方式
	private String payName;	
	// 商品总价
	private double goodsPrice;	
	// 邮费
	private double shippingPrice;		
	// 优惠金额
	private double couponPrice = 0.00;	
	// 实付金额（订单总额）
	private double totalAmount = 0.00;	
	// 应付金额
	private double orderAmount = 0.00;	
	// 下单时间
	private String addTime;			
	// 付款时间
	private String payTime;		
	// 折扣率
	private double discount = 0.00;		
	// 会员折扣优惠了多少
	private double memberGodsPrice;	
	// 用户备注
	private String userNote;
	// 渠道标识
	private String channelFlag;
	
	// 使用的红包IDs
	private String couponIds;
	//使用的红包Names
	private String couponNames;
	
	// 是否新订单（0：新订单；1：老订单）
	private Integer isNeworder;
	
	private List<TOrderGoods> OrderGoods = new ArrayList<TOrderGoods>();
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTempOrderId() {
		return tempOrderId;
	}
	public void setTempOrderId(String tempOrderId) {
		this.tempOrderId = tempOrderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getIsReal() {
		return isReal;
	}
	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getShippingPrice() {
		return shippingPrice;
	}
	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	public double getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getMemberGodsPrice() {
		return memberGodsPrice;
	}
	public void setMemberGodsPrice(double memberGodsPrice) {
		this.memberGodsPrice = memberGodsPrice;
	}
	public String getUserNote() {
		return userNote;
	}
	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}
	public List<TOrderGoods> getOrderGoods() {
		return OrderGoods;
	}
	public void setOrderGoods(List<TOrderGoods> orderGoods) {
		OrderGoods = orderGoods;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getCouponIds() {
		return couponIds;
	}
	public void setCouponIds(String couponIds) {
		this.couponIds = couponIds;
	}
	public String getCouponNames() {
		return couponNames;
	}
	public void setCouponNames(String couponNames) {
		this.couponNames = couponNames;
	}
	/**
	 * @return the isNeworder
	 */
	public Integer getIsNeworder() {
		return isNeworder;
	}
	/**
	 * @param isNeworder the isNeworder to set
	 */
	public void setIsNeworder(Integer isNeworder) {
		this.isNeworder = isNeworder;
	}
}
