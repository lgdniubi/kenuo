package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * app_start_page 实体类
 * @author 土豆 2017-6-6
 *
 */
public class AppStartPage extends DataEntity<AppStartPage>{

	private static final long serialVersionUID = 1L;
	
	private int appStartPageId;					//app_start_page表id
	private String name;						//启动页名称
	private String imgUrl;						//启动页
	private String redirectUrl;					//链接
	private String isOnSale;					//是否上架（0：下架；1：上架）
	
	public int getAppStartPageId() {
		return appStartPageId;
	}
	public void setAppStartPageId(int appStartPageId) {
		this.appStartPageId = appStartPageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getIsOnSale() {
		return isOnSale;
	}
	public void setIsOnSale(String isOnSale) {
		this.isOnSale = isOnSale;
	}
	
}
