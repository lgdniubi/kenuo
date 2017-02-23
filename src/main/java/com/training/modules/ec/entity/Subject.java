package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 主题图片实体类
 * @author 小叶  2017年2月17日
 *
 */
public class Subject extends DataEntity<Subject>{
	
	private static final long serialVersionUID = 1L;

	private int subId;          //专题id
	private String subName;     //专题名字
	private int  position;      //图片所在位置
	private String subHeadImg;  //头图
	private String subOriginalImg;  //原始图
	private Date addTime;   // 创建时间
	private int del_flag;    //删除标识
	
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getSubHeadImg() {
		return subHeadImg;
	}
	public void setSubHeadImg(String subHeadImg) {
		this.subHeadImg = subHeadImg;
	}
	public String getSubOriginalImg() {
		return subOriginalImg;
	}
	public void setSubOriginalImg(String subOriginalImg) {
		this.subOriginalImg = subOriginalImg;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(int del_flag) {
		this.del_flag = del_flag;
	}
	
	
	
}
