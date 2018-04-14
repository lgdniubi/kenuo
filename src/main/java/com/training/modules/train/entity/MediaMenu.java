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
public class MediaMenu extends DataEntity<MediaMenu> {

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
	private MediaMenu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private List<MediaMenu> children;	// 父级菜单
	private Integer isPay; 	// 是否付费
	
	private String userId;
	
	public MediaMenu(){
		super();
		this.sort = 30;
		this.isShow = "1";
	}
	
	public MediaMenu(String id){
		super(id);
	}
	
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	@JsonBackReference
	@NotNull
	public MediaMenu getParent() {
		return parent;
	}

	public void setParent(MediaMenu parent) {
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

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	@JsonIgnore
	public boolean hasChildren(){
		if(children == null || children.size() == 0){
			return false;
		}
		for(MediaMenu child:children){
			if(child.getIsShow().equals("1")){
				return true;
			}
		}
		return false;
	}
//	@JsonIgnore
//	public boolean hasPermisson(){
//		List<PCMenu> menuList = TrainUserUtils.getMenuList();
//		for(PCMenu menu:menuList){
//			if(menu.getId().equals(this.getId()))
//				return true;
//		}
//		return false;
//	}
	
	@JsonIgnore
	public static void sortList(List<MediaMenu> list, List<MediaMenu> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			MediaMenu e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						MediaMenu child = sourcelist.get(j);
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
	
	@Override
	public String toString() {
		return name;
	}

	public void setChildren(List<MediaMenu> children) {
		this.children = children;
	}

	public List<MediaMenu> getChildren() {
		return children;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}