package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

/**
 * 课程线下 “学生”预约
 * @author kele
 * @version 2016-4-7
 */
public class TrainOfflineTestSubscribe extends DataEntity<TrainOfflineTestSubscribe>{

	private static final long serialVersionUID = 1L;
	private String categoryId;				//课程分类ID
	private String subscribeId;				//课程预约ID
	private Date subscribeTime;				//课程预约时间
	private Date createTime;				//学生预约时间
	
	private User user;						//用户类
	private TrainOfflineSubscribeTime trainOfflineSubscribeTime;	//课程线下预约
	private TrainCategorys trainCategorys;	//课程分类
	private	String userNum;					//用户数量
	private String userName;				//用户名称
	
	private Date beginDate;					//开始日期
	private Date endDate;					//结束日期
	private String parentId;				//父类ID 
	
	public TrainOfflineTestSubscribe() {
		super();
	}

	public TrainOfflineTestSubscribe(String categoryId, String subscribeId, Date subscribeTime, Date createTime,
			User user, TrainOfflineSubscribeTime trainOfflineSubscribeTime, TrainCategorys trainCategorys,
			String userNum, String userName, Date beginDate, Date endDate, String parentId) {
		super();
		this.categoryId = categoryId;
		this.subscribeId = subscribeId;
		this.subscribeTime = subscribeTime;
		this.createTime = createTime;
		this.user = user;
		this.trainOfflineSubscribeTime = trainOfflineSubscribeTime;
		this.trainCategorys = trainCategorys;
		this.userNum = userNum;
		this.userName = userName;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.parentId = parentId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TrainOfflineSubscribeTime getTrainOfflineSubscribeTime() {
		return trainOfflineSubscribeTime;
	}

	public void setTrainOfflineSubscribeTime(TrainOfflineSubscribeTime trainOfflineSubscribeTime) {
		this.trainOfflineSubscribeTime = trainOfflineSubscribeTime;
	}

	public TrainCategorys getTrainCategorys() {
		return trainCategorys;
	}

	public void setTrainCategorys(TrainCategorys trainCategorys) {
		this.trainCategorys = trainCategorys;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
