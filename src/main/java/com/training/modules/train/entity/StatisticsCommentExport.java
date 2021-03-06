package com.training.modules.train.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 评论统计
 * @author  yangyang
 * @version 
 */
public class StatisticsCommentExport extends DataEntity<StatisticsCommentExport>{

	/**
	 * 课程统计总揽
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;					//用户id
	private String userName;				//用户名
	private String mobile;					//手机号
	private String position;				//职位
	private String createTime;				//日期
	private String shopName;			//店铺
	private String market;					//市场
	private String area;						//区域
	private String lessonsId;				//课程Id
	private String categorysName;		//课程分类名称
	private String lessonsName;		//课程名称
	private String content;				//评论内容
	private String  commentTime;			//评论日期
	private String star;						//评论星级
	
	
	
	@JsonIgnore
	@ExcelField(title="用户ID", align=2, sort=1)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonIgnore
	@ExcelField(title="用户名", align=2, sort=2)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@JsonIgnore
	@ExcelField(title="手机号", align=2, sort=3)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@JsonIgnore
	@ExcelField(title="职位", align=2, sort=4)
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@JsonIgnore
	@ExcelField(title="日期", align=2, sort=5)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateDate(String createTime) {
		this.createTime = createTime;
	}
	@JsonIgnore
	@ExcelField(title="店铺", align=2, sort=6)
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	@JsonIgnore
	@ExcelField(title="市场", align=2, sort=7)
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	@JsonIgnore
	@ExcelField(title="区域", align=2, sort=8)
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@JsonIgnore
	@ExcelField(title="课程ID", align=2, sort=9)
	public String getLessonsId() {
		return lessonsId;
	}
	public void setLessonsId(String lessonsId) {
		this.lessonsId = lessonsId;
	}
	@JsonIgnore
	@ExcelField(title="分类名称", align=2, sort=10)
	public String getCategorysName() {
		return categorysName;
	}
	public void setCategorysName(String categorysName) {
		this.categorysName = categorysName;
	}
	
	@JsonIgnore
	@ExcelField(title="课程名称", align=2, sort=11)
	public String getLessonsName() {
		return lessonsName;
	}
	public void setLessonsName(String lessonsName) {
		this.lessonsName = lessonsName;
	}
	
	@JsonIgnore
	@ExcelField(title="评论内容", align=2, sort=12)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@JsonIgnore
	@ExcelField(title="评论时间", align=2, sort=13)
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	@JsonIgnore
	@ExcelField(title="评论星级", align=2, sort=14)
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	
}
