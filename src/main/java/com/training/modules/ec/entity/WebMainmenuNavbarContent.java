package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * mtmy_web_mainmenu_navbar_content 实体类
 * @author 土豆 2017-5-5
 *
 */
public class WebMainmenuNavbarContent extends DataEntity<WebMainmenuNavbarContent>{

	private static final long serialVersionUID = 1L;
	
	private int webMainmenuNavbarContentId;		//mtmy_web_mainmenu_navbar_content表id
	private int mainmenuId;						//导航栏ID
	private String name;						//名称
	private String imgUrl;						//图片链接
	private String redirectUrl;					//链接地址
	private String sort;						//排序
	
	
	public int getWebMainmenuNavbarContentId() {
		return webMainmenuNavbarContentId;
	}
	public void setWebMainmenuNavbarContentId(int webMainmenuNavbarContentId) {
		this.webMainmenuNavbarContentId = webMainmenuNavbarContentId;
	}
	public int getMainmenuId() {
		return mainmenuId;
	}
	public void setMainmenuId(int mainmenuId) {
		this.mainmenuId = mainmenuId;
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
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}
