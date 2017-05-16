package com.training.modules.forms.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 视频文档信息表
 * 
 * @author stone
 * @date 2017年4月24日
 */
public class LessionTimeReport extends TreeEntity<LessionTimeReport>{

	private static final long serialVersionUID = 1L;
	
	private String contentId;               //视频文档ID
	private String contentName;				//视频文档名称
	private String lessionId;				//课程ID
	private String lessionName;             //课程名称
	private String oneClassify;;			//一级分类
	private String twoClassify;;			//二级分类
	private int collect;                    //课程收藏总量
	private String status;                  //属性（视频OR文档）
	private Date begtime;					//查询开始时间
	private Date endtime;					//查询结束时间
	private String time;					//时间
	private String categoryId;				//分类ID
	
	@ExcelField(title = "视频文档ID" , sort = 100)
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	@ExcelField(title = "视频文档名称" , sort = 200)
	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	
	@ExcelField(title = "课程ID" , sort = 400)
	public String getLessionId() {
		return lessionId;
	}

	public void setLessionId(String lessionId) {
		this.lessionId = lessionId;
	}

	@ExcelField(title = "课程名称" , sort = 500)
	public String getLessionName() {
		return lessionName;
	}

	public void setLessionName(String lessionName) {
		this.lessionName = lessionName;
	}

	@ExcelField(title = "一级分类" , sort = 600)
	public String getOneClassify() {
		return oneClassify;
	}

	public void setOneClassify(String oneClassify) {
		this.oneClassify = oneClassify;
	}

	@ExcelField(title = "二级分类" , sort = 700 )
	public String getTwoClassify() {
		return twoClassify;
	}

	public void setTwoClassify(String twoClassify) {
		this.twoClassify = twoClassify;
	}

	@ExcelField(title = "课程收藏量" , align = 2 , sort = 800)
	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

	@ExcelField(title = "属性" , sort = 900)
	public String getStatus() {
		return status;
	}
	
	@ExcelField(title = "时间" , sort = 100)
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBegtime() {
		return begtime;
	}

	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void setParent(LessionTimeReport parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LessionTimeReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
