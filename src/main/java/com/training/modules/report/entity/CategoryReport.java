package com.training.modules.report.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 报表类目分析实体类
 * @author coffee
 *
 */
public class CategoryReport extends DataEntity<CategoryReport>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryId;	//商品二级分类
	private String parentId;	//商品一级分类
	private String categoryName;//商品二级分类name
	private String amount;		//订单总金额
	private String goodsnum;	//下单商品数
	private String ordernum;	//下单量
	private String addtime;		//添加时间
	
	private Date begtime; 		//开始时间
	private Date endtime;		//结束时间
	
	
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
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
}
