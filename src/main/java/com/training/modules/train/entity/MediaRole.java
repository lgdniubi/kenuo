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
public class MediaRole extends DataEntity<MediaRole> {
	
	private static final long serialVersionUID = 1L;
	private int roleId;		//主键id
	private String name;	//名称
	private String ename;	//英文名称
	private int roleGrade;	//角色等级
	
	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	private String mediaRoleIds;		//角色IDS
	private String mediaRoleNames;	//角色名称S
	
	private User user;				//用户
	
	private int franchiseeid;		//商家id
	private String officeid;	//机构id
	private int modeid;		//模板id
	private String modName;	//机构id
	private String remarks;	//机构id
	private String type;	//角色类型：0：写手，1：管理员
	private List<Dict> typeList;	//角色类型集合
	private String publicto;	//发布平台：a:妃子校，b:每天美耶，ab:每天美耶和妃子校
	private String[] publictoArr;	//发布平台：a:妃子校，b:每天美耶，ab:每天美耶和妃子校
	private List<Dict> publictoList;	//发布平台集合
	
	private String opflag;	//dy:平台，qy:企业
	
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
	public String getMediaRoleIds() {
		return mediaRoleIds;
	}
	public void setMediaRoleIds(String mediaRoleIds) {
		this.mediaRoleIds = mediaRoleIds;
	}
	public String getMediaRoleNames() {
		return mediaRoleNames;
	}
	public void setMediaRoleNames(String mediaRoleNames) {
		this.mediaRoleNames = mediaRoleNames;
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
	public String getType() {
//		if (type == null)type ="0";
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Dict> getTypeList() {
		typeList = new ArrayList<Dict>();
		Dict c  = new Dict();
		c.setLabel("写手");
		c.setValue("0");
		Dict c1  = new Dict();
		c1.setLabel("管理员");
		c1.setValue("1");
		typeList.add(c);
		typeList.add(c1);
		return typeList;
	}
	public String getPublicto() {
		if (publictoArr == null) return publicto;
		StringBuilder sb = new StringBuilder();
		for (String a : publictoArr) {
			sb.append(a);
			sb.append(",");
		}
		return sb.toString();
	}
	public void setPublicto(String publicto) {
		this.publicto = publicto;
	}
	public String[] getPublictoArr() {
		if(publicto!=null)return publicto.split(",");
		return publictoArr;
	}
	public void setPublictoArr(String[] publictoArr) {
		this.publictoArr = publictoArr;
	}
	public List<Dict> getPublictoList() {
		publictoList = new ArrayList<Dict>();
		Dict c  = new Dict();
		c.setLabel("妃子校");
		c.setValue("a");
		Dict c1  = new Dict();
		c1.setLabel("每天美耶");
		c1.setValue("b");
		Dict c2  = new Dict();
		c2.setLabel("蓓丽莲娜");
		c2.setValue("c");
		publictoList.add(c);
		publictoList.add(c1);
		publictoList.add(c2);
		return publictoList;
	}
	public String getOpflag() {
		return opflag;
	}
	public void setOpflag(String opflag) {
		this.opflag = opflag;
	}
	
	
}
