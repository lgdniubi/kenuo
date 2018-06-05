package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

public class RefundOrder extends DataEntity<RefundOrder> {

	private static final long serialVersionUID = 1L;
	private String orderId; //订单号
	private String tempOrderId; //临时订单号
	private String orderType;//订单类型（1：线上，转账）
	private double arrearagePrice;//欠款金额
	private String arrearageOffice;//欠款机构
	private double amount;//实付金额
	private String addTime;//创建时间
	private String orderStatus;//订单状态（1：待支付；2：已付款；）
	private String payCode;//支付类型(wx:微信，alipay:支付宝,zz:转账)
	private String payTime; //支付时间
	private String chargeId;//第三方订单号
	private String userId; //用户ID（妃子校用户）
	private String userMobile; //用户手机号码
	private String userOfficeId; //用户组织架构
	private String channelFlag; //渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	private String arrearageOfficeName; //欠款机构名
	private String userName; //用户名
	private String franchiseeName; //商家
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public double getArrearagePrice() {
		return arrearagePrice;
	}
	public void setArrearagePrice(double arrearagePrice) {
		this.arrearagePrice = arrearagePrice;
	}
	public String getArrearageOffice() {
		return arrearageOffice;
	}
	public void setArrearageOffice(String arrearageOffice) {
		this.arrearageOffice = arrearageOffice;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getUserOfficeId() {
		return userOfficeId;
	}
	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getArrearageOfficeName() {
		return arrearageOfficeName;
	}
	public void setArrearageOfficeName(String arrearageOfficeName) {
		this.arrearageOfficeName = arrearageOfficeName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	
}
