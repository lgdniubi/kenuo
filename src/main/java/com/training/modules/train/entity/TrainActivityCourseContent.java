/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 妃子校活动课程Entity
 * @author coffee
 *
 */
public class TrainActivityCourseContent extends DataEntity<TrainActivityCourseContent> {

	private static final long serialVersionUID = 1L;
	private int cId;		//主键id
	private int acId;		//活动课程id
	private int type;		//类型（1：图片）
	private String img;		//图片路径
	private int sort;		//排序
	
	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public int getAcId() {
		return acId;
	}
	public void setAcId(int acId) {
		this.acId = acId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}