package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 商品功效实体类
 * @author coffee
 *
 */
public class Effect extends DataEntity<Effect>{

	private static final long serialVersionUID = 1L;
	private int efId;		//商品功效id
	private String name;	//商品功效名称
	private int sort;		//排序
	private String img;		//照片
	private Date addTime;	//添加时间

	public int getEfId() {
		return efId;
	}
	public void setEfId(int efId) {
		this.efId = efId;
	}
	public String getName() {
		return name; 
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
