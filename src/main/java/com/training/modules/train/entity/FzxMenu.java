/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Menu;
import com.training.modules.sys.utils.UserUtils;

/**
 * 菜单Entity
 * 
 * @version 2013-05-15
 */
public class FzxMenu extends DataEntity<FzxMenu> {

	private static final long serialVersionUID = 1L;
	private Integer menuId;		//主键id
	private String name;	//名称
	private String enname;	//英文名称
	private String href;	//链接
	private String icon;	//图标
	private String isShow;	//是否在菜单中显示（0：显示；1：隐藏；）
	private int sort;		//排序
	private int num;    //是否有子类
	/*private String permission; // 权限标识*/
	private FzxMenu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private List<FzxMenu> children;	// 父级菜单
	public Integer getMenuId() {
		return menuId;
	}
	
	public FzxMenu(){
		super();
		this.sort = 30;
		this.isShow = "0";
	}
	
	
	
	public FzxMenu(Integer menuId) {
		super();
		this.menuId = menuId;
	}
	
	@JsonBackReference
	@NotNull
	public FzxMenu getParent() {
		return parent;
	}

	public void setParent(FzxMenu parent) {
		this.parent = parent;
	}
	
	
	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	/*@Length(min=0, max=200)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}*/
	
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
	
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	@JsonIgnore
	public static Integer getRootId(){
		return 1;
	}
	
	public Integer getParentId() {
		return parent != null && parent.getMenuId() != null ? parent.getMenuId() : 0;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@JsonIgnore
	public boolean hasChildren(){
		if(children == null || children.size() == 0){
			return false;
		}
		for(FzxMenu child:children){
			if(child.getIsShow()=="0"){
				return true;
			}
		}
		return false;
	}
	/*@JsonIgnore
	public boolean hasPermisson(){
		List<FzxMenu> menuList = UserUtils.getMenuList();
		for(Menu menu:menuList){
			if(menu.getId().equals(this.getId()))
				return true;
		}
		return false;
	}
	*/
	@JsonIgnore
	public static void sortList(List<FzxMenu> list, List<FzxMenu> sourcelist, Integer parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			FzxMenu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getMenuId()!=null
					&& e.getParent().getMenuId() == parentId){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						FzxMenu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getMenuId()!=null
								&& child.getParent().getMenuId().equals(e.getMenuId())){
							sortList(list, sourcelist, e.getMenuId(), true);
							break;
						}
					}
				}
			}
		}
	}
}