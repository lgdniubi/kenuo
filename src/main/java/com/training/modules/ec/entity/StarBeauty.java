package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 
 * 明星技师自由配置表
 * @author 土豆   2018-3-7
 *
 */
public class StarBeauty extends DataEntity<StarBeauty>{

	private static final long serialVersionUID = 1L;
	private String name;		//组名
	private int isShow;			//是否展示（0：否，1：是）
	private String isOpen;			//是否公开（0：公开，1：不公开）
	private String franchiseeId;	//商家id
	private String franchiseeName;	//商家名称
	private int sort;			//排序
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
