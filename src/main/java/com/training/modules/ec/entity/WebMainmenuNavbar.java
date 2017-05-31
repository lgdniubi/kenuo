package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * mtmy_web_mainmenu_navbar 实体类
 * @author 土豆 2017-5-4
 *
 */
public class WebMainmenuNavbar extends DataEntity<WebMainmenuNavbar>{

	private static final long serialVersionUID = 1L;
	
	
	private int webMainmenuNavbarId;		//mtmy_web_mainmenu_navbar表id
	private String name;					//标签组名称
	private String type;					//类型(1：首页；2：商城；3：生活美容；)
	private String imgUrl;					//背景图片
	private String isShou;					//是否显示(0：显示；1：不显示)
	
	
	public int getWebMainmenuNavbarId() {
		return webMainmenuNavbarId;
	}
	public void setWebMainmenuNavbarId(int webMainmenuNavbarId) {
		this.webMainmenuNavbarId = webMainmenuNavbarId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getIsShou() {
		return isShou;
	}
	public void setIsShou(String isShou) {
		this.isShou = isShou;
	}
	
}
