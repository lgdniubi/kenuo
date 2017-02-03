/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户Entity
 * 
 * @version 2013-12-05
 */
public class UserLog extends DataEntity<UserLog> {

	private static final long serialVersionUID = 1L;
	private String userId;		//用户id
	private String content;		//修改内容
	
	private String name;		//创建用户名
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}