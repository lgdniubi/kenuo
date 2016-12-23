package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;


/**
 * 邀请明细
 * @author yangyang
 *
 */

public class InvitationUser extends TreeEntity<InvitationUser>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;					//用户id
	private String userName;				//用户昵称
	private String mobile;					//手机号
	private String invitationCode;			//优惠卷id
	private int status;					//状态  0 未使用  1 已使用 2 已过期
	private Date registerTime;				//领取时间
	private Date consumeTime;				//消费时间
	private String orderId;					//订单Id
	private int sendNum;					//邀请人数
	private int consume;					//消费人数
	private int type;						//类型
	private double consumePrice;			//消费总金额
	private double tiPrice;				//提成总金额
	private double orderPrice;				//订单价格
	private double price;					//账户余额
	
	
	
	
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSendNum() {
		return sendNum;
	}
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}
	public int getConsume() {
		return consume;
	}
	public void setConsume(int consume) {
		this.consume = consume;
	}
	public double getConsumePrice() {
		return consumePrice;
	}
	public void setConsumePrice(double consumePrice) {
		this.consumePrice = consumePrice;
	}
	public double getTiPrice() {
		return tiPrice;
	}
	public void setTiPrice(double tiPrice) {
		this.tiPrice = tiPrice;
	}
	@Override
	public InvitationUser getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(InvitationUser parent) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
	
	

	

}
