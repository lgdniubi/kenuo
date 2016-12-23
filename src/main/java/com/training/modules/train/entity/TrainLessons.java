package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 课程
 * @author kele
 * @version 2016年3月10日
 */
public class TrainLessons extends DataEntity<TrainLessons>{

	private static final long serialVersionUID = 1L;
	private String lessonId;		//课程ID
	private TrainCategorys trainCategorys;//课程分类bean
	private String name;			//课程名称
	private String coverPic;		//课程封面
	private String introduce;		//课程介绍
	private int status;				//课程状态：（ 1 已完成，-1 未完成，0 正在学习）
	private int lessontype;			//课程类型：（线上课程 1 ，线上测试 2 ，线下预约 3 ）
	private int lessonScore;		//课程学分
	private String createuser;		//用户ID
	private String officeCode;		//用户机构编码
	
	private Date beginDate;			// 开始日期
	private Date endDate;			// 结束日期
	private String categoryId;		//分类ID
	private String parentId;		//父类ID
	
	public TrainLessons(){
		super();
	}

	public TrainLessons(String lessonId) {
		super();
		this.lessonId = lessonId;
	}

	public TrainLessons(String lessonId, TrainCategorys trainCategorys, String name, String coverPic, String introduce,
			int status, int lessontype, int lessonScore, String createuser, String officeCode, Date beginDate,
			Date endDate, String categoryId, String parentId) {
		super();
		this.lessonId = lessonId;
		this.trainCategorys = trainCategorys;
		this.name = name;
		this.coverPic = coverPic;
		this.introduce = introduce;
		this.status = status;
		this.lessontype = lessontype;
		this.lessonScore = lessonScore;
		this.createuser = createuser;
		this.officeCode = officeCode;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
		this.parentId = parentId;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public TrainCategorys getTrainCategorys() {
		return trainCategorys;
	}

	public void setTrainCategorys(TrainCategorys trainCategorys) {
		this.trainCategorys = trainCategorys;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLessontype() {
		return lessontype;
	}

	public void setLessontype(int lessontype) {
		this.lessontype = lessontype;
	}

	public int getLessonScore() {
		return lessonScore;
	}

	public void setLessonScore(int lessonScore) {
		this.lessonScore = lessonScore;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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
