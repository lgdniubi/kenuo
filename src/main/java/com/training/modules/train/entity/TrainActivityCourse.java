/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 妃子校活动课程Entity
 * @author coffee
 *
 */
public class TrainActivityCourse extends DataEntity<TrainActivityCourse> {

	private static final long serialVersionUID = 1L;
	private int acId;		//主键id
	private String name;		//课程名称
	private Date startDate;		//活动开始时间
	private Date endDate;		//活动结束时间
	private double amount;		//金额
	private int isOnSale;		//是否上架（0：上架；1：下架）
	private String headImg;		//封面首图
	private int sort;			//排序
	
	private int status;			//状态（0：全部、1：未开始、2：报名中、3已结束）
	private Date beginDate;		//开始时间
	private Date lastDate;		//结束时间
	
	private String franchiseeId;	//商家id
	private String label;			//标签4个字
	private Integer isOpen;			//是否公开，1不公开，选择商家
	
	private List<TrainActivityCourseContent> trainActivityCourseContentList;	//内容详情
	
	public int getAcId() {
		return acId;
	}
	public void setAcId(int acId) {
		this.acId = acId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(int isOnSale) {
		this.isOnSale = isOnSale;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public List<TrainActivityCourseContent> getTrainActivityCourseContentList() {
		return trainActivityCourseContentList;
	}
	public void setTrainActivityCourseContentList(List<TrainActivityCourseContent> trainActivityCourseContentList) {
		this.trainActivityCourseContentList = trainActivityCourseContentList;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}
	
}