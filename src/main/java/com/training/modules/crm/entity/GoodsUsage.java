package com.training.modules.crm.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * kenuo 产品使用记录的实体类 
 * @author：sharp 
 * @date：2017年3月15日
 */
public class GoodsUsage extends DataEntity<GoodsUsage> {

	private static final long serialVersionUID = 1L;

	private String usageId; // auto increment
	private String userId; // 用户ID
	private String goodsId; // 产品ID,
	private String goodsNo; // 产品编号,
	private String goodsName; // 产品名称,
	private String usageNum; // 使用数量,
	private Date startDate; // 开始时间
	private Date endDate; // 结束时间
	private String effection; // 效果
	private String feedback; // 反馈
	private String remark; // 备注
	
	private String franchiseeId; //区分商家id

	public String getUsageId() {
		return usageId;
	}

	public void setUsageId(String usageId) {
		this.usageId = usageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUsageNum() {
		return usageNum;
	}

	public void setUsageNum(String usageNum) {
		this.usageNum = usageNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEffection() {
		return effection;
	}

	public void setEffection(String effection) {
		this.effection = effection;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
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