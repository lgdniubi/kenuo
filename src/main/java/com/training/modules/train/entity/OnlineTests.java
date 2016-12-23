package com.training.modules.train.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 在线考试Entity
 * @author Superman
 */
public class OnlineTests {
	private String lessonId;	//考题id
	private String categoryId;	//课程类别id
	private int isPsaa;			//是否通过
	private float lessonScore;	//学分
	private int status;			//状态
	private Date createtime;	//开始时间
	private int correctNum;		//正确的题数
	private int errorNum;		//错误题数
	private String exerciseTime;	//用时
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public int getIsPsaa() {
		return isPsaa;
	}
	public void setIsPsaa(int isPsaa) {
		this.isPsaa = isPsaa;
	}
	public float getLessonScore() {
		return lessonScore;
	}
	public void setLessonScore(float lessonScore) {
		this.lessonScore = lessonScore;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
	public int getCorrectNum() {
		return correctNum;
	}
	public void setCorrectNum(int correctNum) {
		this.correctNum = correctNum;
	}
	public int getErrorNum() {
		return errorNum;
	}
	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}
	public String getExerciseTime() {
		return exerciseTime;
	}
	public void setExerciseTime(String exerciseTime) {
		this.exerciseTime = exerciseTime;
	}
	
}
