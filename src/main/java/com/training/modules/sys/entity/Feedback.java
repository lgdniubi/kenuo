/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 反馈管理Entity
 * 
 * @version 2013-12-05
 */
public class Feedback extends DataEntity<Feedback> {

	private static final long serialVersionUID = 1L;
	private String fbId;
	private String userId;				//反馈用户ID
	private String content;
	private Date createtime;			//反馈时间	
	private int status;					//反馈状态		0  显示   -1  不显示
	private int fbStatus;				//是否解决		0 未解决  1  解决
	
	
//	private String loginName;	// 反馈用户姓名
	//获取用户表的数据
	private String name;	// 反馈用户姓名
	private String photo;	// 头像
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public int getFbStatus() {
		return fbStatus;
	}
	public void setFbStatus(int fbStatus) {
		this.fbStatus = fbStatus;
	}
//	public String getLoginName() {
//		return loginName;
//	}
//	public void setLoginName(String loginName) {
//		this.loginName = loginName;
//	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
}