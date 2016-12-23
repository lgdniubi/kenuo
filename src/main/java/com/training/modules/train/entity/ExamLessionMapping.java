package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class ExamLessionMapping  extends DataEntity<ExamLessionMapping>{
	private static final long serialVersionUID = 1L;
	private String exerciseId;		//试题ID
	private String categoryId;		//单元测试题ID
	private String lessonId;		//课程ID
	private Date craetetime;		//添加时间
	private int status;				//状态  0  显示  -1  不显示
	private String lessontype;		//试题类型   1 课后习题    2 单元测试题
	
	public String getLessontype() {
		return lessontype;
	}
	public void setLessontype(String lessontype) {
		this.lessontype = lessontype;
	}
	public String getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public Date getCraetetime() {
		return craetetime;
	}
	public void setCraetetime(Date craetetime) {
		this.craetetime = craetetime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
