package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 团购活动实体类
 * @author coffee
 * @date 2018年3月30日
 */
public class MtmyGroupActivity extends TreeEntity<MtmyGroupActivity> {
	
	
	private static final long serialVersionUID = 1L;
	private String activityStatus;	// 活动状态(0：即将开启；1：进行中；2：已结束)
	private Date activityStartDate;	// 活动开始日期
	private Date activityEndDate;	// 活动结束日期
	private Date activityStartTime;	// 活动开始时间点
	private Date activityEndTime;	// 活动结束时间点
	
	private String activityStartTimeStr;	
	private String activityEndTimeStr;
	
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	public Date getActivityStartDate() {
		return activityStartDate;
	}
	public void setActivityStartDate(Date activityStartDate) {
		this.activityStartDate = activityStartDate;
	}
	public Date getActivityEndDate() {
		return activityEndDate;
	}
	public void setActivityEndDate(Date activityEndDate) {
		this.activityEndDate = activityEndDate;
	}
	public Date getActivityStartTime() {
		return activityStartTime;
	}
	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}
	public Date getActivityEndTime() {
		return activityEndTime;
	}
	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	public MtmyGroupActivity getParent() {
		return parent;
	}
	public void setParent(MtmyGroupActivity parent) {
		this.parent = parent;
	}
	public String getActivityStartTimeStr() {
		return activityStartTimeStr;
	}
	public void setActivityStartTimeStr(String activityStartTimeStr) {
		this.activityStartTimeStr = activityStartTimeStr;
	}
	public String getActivityEndTimeStr() {
		return activityEndTimeStr;
	}
	public void setActivityEndTimeStr(String activityEndTimeStr) {
		this.activityEndTimeStr = activityEndTimeStr;
	}
}
