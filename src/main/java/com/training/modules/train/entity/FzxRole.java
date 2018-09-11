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
public class FzxRole extends DataEntity<FzxRole> {
	
	private static final long serialVersionUID = 1L;
	private int roleId;		//主键id
	private String name;	//名称
	private String enname;	//英文名称
	private String enamer;	//角色英文名称
	private int roleGrade;	//角色等级
	
	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	private String fzxRoleIds;		//角色IDS
	private String fzxRoleNames;	//角色名称S
	
	private User user;				//用户
	
	private int franchiseeid;		//商家id
	private String officeid;	//机构id
	private int modeid;		//模板id
	private String modName;	//机构id
	private String isDefault;	//0:非默认，1：默认角色
	private String opflag;	//pt:平台，qy:企业
	
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
	public String getEnamer() {
		return enamer;
	}
	public void setEnamer(String enamer) {
		this.enamer = enamer;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getOpflag() {
		return opflag;
	}
	public void setOpflag(String opflag) {
		this.opflag = opflag;
	}
	
}
