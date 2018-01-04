package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 自媒体权限表实体类
 * 
 * @version 2017-17-8  土豆
 */
public class MediaLoginAuth extends DataEntity<MediaLoginAuth> {

	private static final long serialVersionUID = 1L;
	
	private String userId;		// 妃子校用户ID
	private String isLogin;	// 自媒体权限,是否可登陆(0:否 1：是)
	private String platform;	// 平台权限(APP平台)
	private String userTag;	// 用户标签
	private String userType;	// 用户类型(目前区分写手、管理员、超级管理员)

	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getUserTag() {
		return userTag;
	}
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}