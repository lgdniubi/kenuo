package com.training.modules.train.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.common.persistence.DataEntity;

public class ExercisesCategorys extends DataEntity<ExercisesCategorys>{
private static final long serialVersionUID = 1L;


	//对应课程表的
	private String exerciseId;
	private int exerciseType;          //所属分类     1 单选题　　２　多选题　　３　是非题  
	private String exerciseTitle;		//试题名称
	private String exerciseContent;	//试题内容
	private String exerciseResult;		//试题答案
	private Date createtime;			//上传时间
	private int status;					//状态		0  显示  -1  不显示
	private String categoryId;			//课程二级ID
	private String tags; 				//快捷搜索关键字
	private String officeCode;			//机构管理CODE
	private String createuser;			//创建者ID（用户ID）
	
	//对应课程表的
	private String name;			//课程表的  类别名称
	private String parentId;		//一级类别id
	private String lessonId;		//课程ID
	//对应权限表的
	private String roleType;		//权限类型   1 超级管理员   查看所有数据  2  普通管理员 所在区、公司一下所有数据  3  培训师 自己的数据
	
	//随机试题
	private String Tid;				//课后习题 单元测试通用id
	private String lessontype;		//试题类型   1 课后习题    2 单元测试题
	private String num1;			//单选题数量
	private String num2;			//多选题
	private String num3;			//判断题
	private int newNum;				//用于将String类型的数量转换为int类型	
	
	public String getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getExerciseType() {
		return exerciseType;
	}
	public void setExerciseType(int exerciseType) {
		this.exerciseType = exerciseType;
	}
	public String getExerciseTitle() {
		return exerciseTitle;
	}
	public void setExerciseTitle(String exerciseTitle) {
		this.exerciseTitle = exerciseTitle;
	}
	public String getExerciseContent() {
		return exerciseContent;
	}
	public void setExerciseContent(String exerciseContent) {
		this.exerciseContent = exerciseContent;
	}
	public String getExerciseResult() {
		return exerciseResult;
	}
	public void setExerciseResult(String exerciseResult) {
		this.exerciseResult = exerciseResult;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getTid() {
		return Tid;
	}
	public void setTid(String tid) {
		Tid = tid;
	}
	public String getLessontype() {
		return lessontype;
	}
	public void setLessontype(String lessontype) {
		this.lessontype = lessontype;
	}
	public String getNum1() {
		return num1;
	}
	public void setNum1(String num1) {
		this.num1 = num1;
	}
	public String getNum2() {
		return num2;
	}
	public void setNum2(String num2) {
		this.num2 = num2;
	}
	public String getNum3() {
		return num3;
	}
	public void setNum3(String num3) {
		this.num3 = num3;
	}
	public int getNewNum() {
		return newNum;
	}
	public void setNewNum(int newNum) {
		this.newNum = newNum;
	}
}
