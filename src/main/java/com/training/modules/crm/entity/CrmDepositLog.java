package com.training.modules.crm.entity;

import com.training.common.persistence.DataEntity;

/**
 * kenuo 
 * @description：crm寄存商品操作日志
 * @author：土豆  	@date：2018年6月4日
 */
public class CrmDepositLog extends DataEntity<CrmDepositLog> {

	private static final long serialVersionUID = 1L;
	
	
	private String consignId; 	//寄存id
	private String productName; //产品名称
	private String buyNum; 		//购买数量
	private String takeNum; 	//取走数量
	private String surplusNum;	//剩余数量
	
	
	public String getConsignId() {
		return consignId;
	}
	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}
	public String getTakeNum() {
		return takeNum;
	}
	public void setTakeNum(String takeNum) {
		this.takeNum = takeNum;
	}
	public String getSurplusNum() {
		return surplusNum;
	}
	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}

	
}
