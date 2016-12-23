package com.training.modules.train.entity;

import java.util.Date;
import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

public class TrainOfflineSubscribeTime extends DataEntity<TrainOfflineSubscribeTime>{

	private static final long serialVersionUID = 1L;
	private String subscribeId;				//预约ID
	private TrainCategorys trainCategorys;	//分类ID
	private User user;						//用户ID
	private int status;						//状态
	private Date subscribeTime;				//预约时间
	private Date createtime;				//创建时间
	
	private Date[] subscribeDate;			//预约时间	
	private Date beginDate;					//开始日期
	private Date endDate;					//结束日期
	private String categoryId;				//分类ID
	private String parentId;				//父类ID 
	
	public TrainOfflineSubscribeTime() {
		super();
	}
	
	public TrainOfflineSubscribeTime(String subscribeId) {
		super();
		this.subscribeId = subscribeId;
	}

	public TrainOfflineSubscribeTime(String subscribeId, TrainCategorys trainCategorys, User user, int status,
			Date subscribeTime, Date createtime, Date[] subscribeDate, Date beginDate, Date endDate, String categoryId,
			String parentId) {
		super();
		this.subscribeId = subscribeId;
		this.trainCategorys = trainCategorys;
		this.user = user;
		this.status = status;
		this.subscribeTime = subscribeTime;
		this.createtime = createtime;
		this.subscribeDate = subscribeDate;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
		this.parentId = parentId;
	}

	public String getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}

	public TrainCategorys getTrainCategorys() {
		return trainCategorys;
	}

	public void setTrainCategorys(TrainCategorys trainCategorys) {
		this.trainCategorys = trainCategorys;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date[] getSubscribeDate() {
		return subscribeDate;
	}

	public void setSubscribeDate(Date[] subscribeDate) {
		this.subscribeDate = subscribeDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
