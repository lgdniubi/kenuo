package com.training.modules.crm.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * kenuo
 * 寄存记录的实体类
 * @author：sharp @date：2017年3月15日
 */
public class Consign extends DataEntity<Consign> {

	private static final long serialVersionUID = 1L;

	private String consignId;
	private String userId;
	private String orderId;
	private String goodsId;//产品ID
	private String goodsNo; //产品编号
	private String goodsName; //产品名称
	private String purchaseNum; //购买数量
	private String consignNum; //存储数量
	private String takenNum; //取走数量
	private String remark; //备注
	private Date createDate; //创建时间
	private String officeName; //机构名称
	private String officeId; //机构ID
	
	private String franchiseeId; //区分商家id
		
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}


	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(String purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public String getConsignNum() {
		return consignNum;
	}

	public void setConsignNum(String consignNum) {
		this.consignNum = consignNum;
	}

	public String getTakenNum() {
		return takenNum;
	}

	public void setTakenNum(String takenNum) {
		this.takenNum = takenNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFranchiseeId() {
		return franchiseeId;
	}

	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

}