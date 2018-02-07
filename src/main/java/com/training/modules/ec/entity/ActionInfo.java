package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class ActionInfo extends DataEntity<ActionInfo>{
	
	private static final long serialVersionUID = 1L;
	private int actionId;				//活动id	
	private String actionName;			//活动名称
	private String actionTitle;			//活动短名称
	private Date startTime;				//开始时间
	private Date endTime;				//结束时间
	private Date showTime;				//显示时间
	private Date closeTime;				//关闭时间
	private int ceiling;				//限购数量
	private int level;					//等级
	private int status;				//状态
	private Date createDate;			//创建时间
	private Date updateDate;			//修改时间
	private String goodsId;				//商品id 集合
	private String goodsName;			//商品名称集合
	private int executeStatus;			//定时器执行状态 0 未执行 1已执行
	private int sort;	// 排序
	
	private String isOpen;				//是否公开(0：公开；不公开则保存相应的商家id，以,拼接)
	
	public int getExecuteStatus() {
		return executeStatus;
	}

	public void setExecuteStatus(int executeStatus) {
		this.executeStatus = executeStatus;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
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

	
	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public int getCeiling() {
		return ceiling;
	}

	public void setCeiling(int ceiling) {
		this.ceiling = ceiling;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
}
