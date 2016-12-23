package com.training.modules.sys.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class BugLog extends DataEntity<BugLog>{
	
	private static final long serialVersionUID = 1L;
	
	private String title;			//操作菜单
	private String requestUri;		//请求地址
	private String content;			//bug内容
	private Date createTime;		//操作时间
	private String createUser;		//操作用户
	private String userAgent;		//用户的浏览器版本
	private String ramek;			//备注
	private String exception; 	// 异常信息
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getRamek() {
		return ramek;
	}
	public void setRamek(String ramek) {
		this.ramek = ramek;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	

}
