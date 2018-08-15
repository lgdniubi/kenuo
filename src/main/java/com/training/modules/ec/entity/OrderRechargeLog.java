package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;
/**
 * 订单充值日志表
 * @author dalong
 *
 */
public class OrderRechargeLog extends TreeEntity<OrderRechargeLog> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;			//主订单id
	private int mtmyUserId;			//用户ID（每天美耶用户）
	private double rechargeAmount;	//充值金额
	private double accountBalance;  //账户余额
	private double totalAmount;		//实付款金额
	private String createByName;	//操作这
	private int recid;		//mappingid
	private double singleRealityPrice;		//实际服务单次价
	private double singleNormPrice;			//单次标价
	private double orderArrearage;		//欠款金额
	private int servicetimes;			//预计服务次数
	private int remaintimes;				//实际服务次数
	private int isReal;			//实物虚拟
	
	private String remarks; //处理预约金的备注
	
	private double advance;     //预付金
	
	private String channelFlag;//渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	
	private String belongOfficeId;           //归属机构id
	
	private String useCouponFlag;          //是否使用充值红包（0：未使用，1：已使用）
	private String couponId;               //红包id
	private double couponMoney;            //红包金额
	private String useCouponId;            //用户领取的红包表id
	
	public int getIsReal() {
		return isReal;
	}

	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}

	public int getRemaintimes() {
		return remaintimes;
	}

	public void setRemaintimes(int remaintimes) {
		this.remaintimes = remaintimes;
	}

	public int getServicetimes() {
		return servicetimes;
	}

	public void setServicetimes(int servicetimes) {
		this.servicetimes = servicetimes;
	}

	public double getOrderArrearage() {
		return orderArrearage;
	}

	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public int getRecid() {
		return recid;
	}

	public void setRecid(int recid) {
		this.recid = recid;
	}

	public double getSingleRealityPrice() {
		return singleRealityPrice;
	}

	public void setSingleRealityPrice(double singleRealityPrice) {
		this.singleRealityPrice = singleRealityPrice;
	}

	public double getSingleNormPrice() {
		return singleNormPrice;
	}

	public void setSingleNormPrice(double singleNormPrice) {
		this.singleNormPrice = singleNormPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public int getMtmyUserId() {
		return mtmyUserId;
	}

	public void setMtmyUserId(int mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}

	public double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public OrderRechargeLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(OrderRechargeLog parent) {
		// TODO Auto-generated method stub
		
	}

	public double getAdvance() {
		return advance;
	}

	public void setAdvance(double advance) {
		this.advance = advance;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}

	public String getBelongOfficeId() {
		return belongOfficeId;
	}

	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}

	public String getUseCouponFlag() {
		return useCouponFlag;
	}

	public void setUseCouponFlag(String useCouponFlag) {
		this.useCouponFlag = useCouponFlag;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public double getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getUseCouponId() {
		return useCouponId;
	}

	public void setUseCouponId(String useCouponId) {
		this.useCouponId = useCouponId;
	}
	
}
