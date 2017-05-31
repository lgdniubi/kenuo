/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 角色Entity
 * 
 * @version 2013-12-05
 */
public class FzxRole extends DataEntity<FzxRole> {
	
	private static final long serialVersionUID = 1L;
	private int roleId;		//主键id
	private String name;	//名称
	private String enname;	//英文名称
	private int roleGrade;	//角色等级
	
	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	private String fzxRoleIds;		//角色IDS
	private String fzxRoleNames;	//角色名称S
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
	public int getRoleGrade() {
		return roleGrade;
	}
	public void setRoleGrade(int roleGrade) {
		this.roleGrade = roleGrade;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getFzxRoleIds() {
		return fzxRoleIds;
	}
	public void setFzxRoleIds(String fzxRoleIds) {
		this.fzxRoleIds = fzxRoleIds;
	}
	public String getFzxRoleNames() {
		return fzxRoleNames;
	}
	public void setFzxRoleNames(String fzxRoleNames) {
		this.fzxRoleNames = fzxRoleNames;
	}
	
}
