/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

/**
 * 角色Entity
 * 
 * @version 2013-12-05
 */
public class PcRole extends DataEntity<PcRole> {
	
	private static final long serialVersionUID = 1L;
	private int roleId;		//主键id
	private String name;	//名称
	private String ename;	//英文名称
	private int roleGrade;	//角色等级
	
	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	private String pcRoleIds;		//角色IDS
	private String pcRoleNames;	//角色名称S
	
	private User user;				//用户
	
	private int franchiseeid;		//商家id
	private String officeid;	//机构id
	private int modeid;		//模板id
	private String modName;	//机构id
	private String remarks;	//机构id
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
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
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
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
	public String getpcRoleIds() {
		return pcRoleIds;
	}
	public void setpcRoleIds(String pcRoleIds) {
		this.pcRoleIds = pcRoleIds;
	}
	public String getpcRoleNames() {
		return pcRoleNames;
	}
	public void setpcRoleNames(String pcRoleNames) {
		this.pcRoleNames = pcRoleNames;
	}
	public int getFranchiseeid() {
		return franchiseeid;
	}
	public void setFranchiseeid(int franchiseeid) {
		this.franchiseeid = franchiseeid;
	}
	public String getOfficeid() {
		return officeid;
	}
	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	public int getModeid() {
		return modeid;
	}
	public void setModeid(int modeid) {
		this.modeid = modeid;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
