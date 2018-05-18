/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import java.util.ArrayList;
import java.util.List;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Dict;
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
	private String enameLable;	//英文名称-字典表显示字母
	private int roleGrade;	//角色等级
	
	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	private String pcRoleIds;		//角色IDS
	private String pcRoleNames;	//角色名称S
	
	private User user;				//用户
	
	private int franchiseeid;		//商家id
	private String grade;	//等级（管理员为100）
	private int modeid;		//模板id
	private String modName;	//机构id
	private String remarks;	//机构id
	private String roleRange;	//0:全部范围，1：管理端，2：店铺端
	private List<Dict> roleRangeList;	//0:全部范围，1：管理端，2：店铺端--供展示列表用
	
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
	public String getEnameLable() {
		return enameLable;
	}
	public void setEnameLable(String enameLable) {
		this.enameLable = enameLable;
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getRoleRange() {
		return roleRange;
	}
	public void setRoleRange(String roleRange) {
		this.roleRange = roleRange;
	}
	//0:全部范围，1：管理端，2：店铺端
	public List<Dict> getRoleRangeList() {
		roleRangeList = new ArrayList<Dict>();
		Dict c  = new Dict();
		c.setLabel("全部范围");
		c.setValue("0");
		Dict c1  = new Dict();
		c1.setLabel("管理端");
		c1.setValue("1");
		Dict c2  = new Dict();
		c2.setLabel("店铺端");
		c2.setValue("2");
		roleRangeList.add(c);
		roleRangeList.add(c1);
		roleRangeList.add(c2);
		return roleRangeList;
	}
}
