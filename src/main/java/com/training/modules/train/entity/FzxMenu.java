/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 菜单Entity
 * 
 * @version 2013-05-15
 */
public class FzxMenu extends DataEntity<FzxMenu> {

	private static final long serialVersionUID = 1L;
	private int menuId;		//主键id
	private String name;	//名称
	private String enname;	//英文名称
	private String href;	//链接
	private String icon;	//图标
	private char isShow;	//是否在菜单中显示（0：显示；1：隐藏；）
	private int sort;		//排序
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public char getIsShow() {
		return isShow;
	}
	public void setIsShow(char isShow) {
		this.isShow = isShow;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}