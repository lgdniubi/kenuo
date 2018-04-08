package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 店铺推荐实体类
 * @author xiaoye 2017年9月12日
 *
 */
public class OfficeRecommend extends DataEntity<OfficeRecommend>{

	private static final long serialVersionUID = 1L;
	
	private int officeRecommendId;         //主键ID
	private String name;                      //组名称
	private String img;                   //头图
	private String isOpen;                //是否公开
	private String franchiseeId;          //商家id
	private String franchiseeName;        //商家名称
	private int sort;                     //排序
	private int isShow;                   //是否显示（0：否；1：是）
	private int delflag;                  //删除标识（0：正常；1：删除；）
	
	public int getOfficeRecommendId() {
		return officeRecommendId;
	}
	public void setOfficeRecommendId(int officeRecommendId) {
		this.officeRecommendId = officeRecommendId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	
}
