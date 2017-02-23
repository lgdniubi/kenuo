package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 直播sku配置实体类
 * @author 小叶  2017年2月22日
 *
 */
public class TrainLiveSku extends DataEntity<TrainLiveSku>{

	
	private static final long serialVersionUID = 1L;
	
	private int trainLiveSkuId;  //id
	private int auditId;         //直播申请ID
	private String userId;		//用户id
	private double price;       //价格
	private int num;            //数值（天数）  
	private int type;           //规格类型（1：直播；2：回放）
	private Date updateCreate;   //修改时间
	public int getTrainLiveSkuId() {
		return trainLiveSkuId;
	}
	public void setTrainLiveSkuId(int trainLiveSkuId) {
		this.trainLiveSkuId = trainLiveSkuId;
	}
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getUpdateCreate() {
		return updateCreate;
	}
	public void setUpdateCreate(Date updateCreate) {
		this.updateCreate = updateCreate;
	}
	
	
}
