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
import com.training.modules.train.utils.TrainUserUtils;

/**
 * 菜单Entity
 * 
 * @version 2013-05-15
 */
public class PCMenu extends DataEntity<PCMenu> {

	private static final long serialVersionUID = 1L;
	private PCMenu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private List<PCMenu> children;	// 父级菜单
	private String name; 	// 名称
	private String href; 	// 链接
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	private Integer isPay; 	// 排序
	
	private int num;		//是否包含子类
	
	private String userId;
	
	public PCMenu(){
		super();
		this.sort = 30;
		this.isShow = "1";
	}
	
	public PCMenu(String id){
		super(id);
	}
	
	@JsonBackReference
	@NotNull
	public PCMenu getParent() {
		return parent;
	}

	public void setParent(PCMenu parent) {
		this.parent = parent;
	}

	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=2000)
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	@NotNull
	public Integer getIsPay() {
		return isPay;
	}

	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}

	@Length(min=0, max=100)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@NotNull
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Length(min=1, max=1)
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@Length(min=0, max=200)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	@JsonIgnore
	public boolean hasChildren(){
		if(children == null || children.size() == 0){
			return false;
		}
		for(PCMenu child:children){
			if(child.getIsShow().equals("1")){
				return true;
			}
		}
		return false;
	}
	@JsonIgnore
	public boolean hasPermisson(){
		List<PCMenu> menuList = TrainUserUtils.getMenuList();
		for(PCMenu menu:menuList){
			if(menu.getId().equals(this.getId()))
				return true;
		}
		return false;
	}
	
	@JsonIgnore
	public static void sortList(List<PCMenu> list, List<PCMenu> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			PCMenu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						PCMenu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())){
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static String getRootId(){
		return "1";
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setChildren(List<PCMenu> children) {
		this.children = children;
	}

	public List<PCMenu> getChildren() {
		return children;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
}