package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 日结算
 * @author yangyang
 *
 */
public class SaleRebatesLog extends TreeEntity<SaleRebatesLog> {
	
	
	private static final long serialVersionUID = 1L;
	private int rebateUser;
	private String rebateName;
	private String rebateLayer;			//返利人等级
	private int receiveUser;
	private String receiveName;
	private String receiveLayer;		//收利人等级
	private String depth;
	private String orderId;
	private double orderAmount;
	private double balancePercent;
	private double balanceAmount;
	private double integralPercent;
	private int integralAmount;	
	private int rebateFlag;	
	private Date rabateDate;
	private Date createDate;
	//private int delFlag;
	
	private Date startTime;
	private Date endTime;


	

	public int getRebateUser() {
		return rebateUser;
	}

	public void setRebateUser(int rebateUser) {
		this.rebateUser = rebateUser;
	}

	public String getRebateName() {
		return rebateName;
	}

	public void setRebateName(String rebateName) {
		this.rebateName = rebateName;
	}

	public int getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(int receiveUser) {
		this.receiveUser = receiveUser;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getBalancePercent() {
		return balancePercent;
	}

	public void setBalancePercent(double balancePercent) {
		this.balancePercent = balancePercent;
	}

	public double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public double getIntegralPercent() {
		return integralPercent;
	}

	public void setIntegralPercent(double integralPercent) {
		this.integralPercent = integralPercent;
	}

	public int getIntegralAmount() {
		return integralAmount;
	}

	public void setIntegralAmount(int integralAmount) {
		this.integralAmount = integralAmount;
	}

	public int getRebateFlag() {
		return rebateFlag;
	}

	public void setRebateFlag(int rebateFlag) {
		this.rebateFlag = rebateFlag;
	}

	public Date getRabateDate() {
		return rabateDate;
	}

	public void setRabateDate(Date rabateDate) {
		this.rabateDate = rabateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getRebateLayer() {
		return rebateLayer;
	}

	public void setRebateLayer(String rebateLayer) {
		if("C".equals(rebateLayer)){
			this.rebateLayer = "AB";
		}else if("D".equals(rebateLayer)){
			this.rebateLayer = "C";
		}else if("Z".equals(rebateLayer)){
			this.rebateLayer = "无等级";
		}else{
			this.rebateLayer = rebateLayer;
		}
	}

	public String getReceiveLayer() {
		return receiveLayer;
	}

	public void setReceiveLayer(String receiveLayer) {
		if("C".equals(receiveLayer)){
			this.receiveLayer = "AB";
		}else if("D".equals(receiveLayer)){
			this.receiveLayer = "C";
		}else if("Z".equals(receiveLayer)){
			this.receiveLayer = "无等级";
		}else{
			this.receiveLayer = receiveLayer;
		}
	}
	@Override
	public SaleRebatesLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(SaleRebatesLog parent) {
		// TODO Auto-generated method stub
		
	}
	

}
