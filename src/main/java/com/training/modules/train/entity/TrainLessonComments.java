package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

/**
 * 课程评论管理
 * @author kele
 * @version 2016年3月17日
 */
public class TrainLessonComments extends DataEntity<TrainLessonComments>{

	private static final long serialVersionUID = 1L;
	private String commentId;			//评论ID
	private TrainLessons trainLessons;	//课程ID
	private User user;					//用户ID
	private String content;				//评论内容
	private float star;					//星级
	private Date createtime;			//创建时间
	private int status;					//状态
	
	private Date beginDate;				//开始日期
	private Date endDate;				//结束日期
	private String categoryId;			//分类ID
	private String parentId;			//父类ID
	
	public TrainLessonComments() {
		super();
	}

	public TrainLessonComments(String commentId) {
		super();
		this.commentId = commentId;
	}

	public TrainLessonComments(String commentId, TrainLessons trainLessons, User user, String content, float star,
			Date createtime, int status, Date beginDate, Date endDate, String categoryId, String parentId) {
		super();
		this.commentId = commentId;
		this.trainLessons = trainLessons;
		this.user = user;
		this.content = content;
		this.star = star;
		this.createtime = createtime;
		this.status = status;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
		this.parentId = parentId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public TrainLessons getTrainLessons() {
		return trainLessons;
	}

	public void setTrainLessons(TrainLessons trainLessons) {
		this.trainLessons = trainLessons;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
