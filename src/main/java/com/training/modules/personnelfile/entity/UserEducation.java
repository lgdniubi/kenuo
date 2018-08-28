package com.training.modules.personnelfile.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 用户-教育背景
 * 
 * @version 2013-05-15
 */
public class UserEducation extends DataEntity<UserEducation> {

	private static final long serialVersionUID = 6510590215925352897L;
	

	private String userId;//用户ID
	private int education;//学历
	private int degree;//学位
	private Date graduationDate;//毕业时间
	private Date hireDate;//入职时间
	private String graduationSchool;//毕业学校
	private String major;//专业
	private String languageAbili;//外语能力
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public Date getGraduationDate() {
		return graduationDate;
	}
	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}
	public String getGraduationSchool() {
		return graduationSchool;
	}
	public void setGraduationSchool(String graduationSchool) {
		this.graduationSchool = graduationSchool;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getLanguageAbili() {
		return languageAbili;
	}
	public void setLanguageAbili(String languageAbili) {
		this.languageAbili = languageAbili;
	}
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
}