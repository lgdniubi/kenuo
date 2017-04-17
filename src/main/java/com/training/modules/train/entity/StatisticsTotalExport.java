package com.training.modules.train.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 统计总揽
 * @author  yangyang
 * @version 
 */
public class StatisticsTotalExport extends DataEntity<StatisticsTotalExport>{

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
	private String collection;				//收藏课程数
	private String totleScore;				//总学分
	private String comment;				//评论课程数
	private String commentScore;		//评论总分
	
	
	
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
	@ExcelField(title="收藏课程数", align=2, sort=9)
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	@JsonIgnore
	@ExcelField(title="总学分", align=2, sort=10)
	public String getTotleScore() {
		return totleScore;
	}
	public void setTotleScore(String totleScore) {
		this.totleScore = totleScore;
	}
	@JsonIgnore
	@ExcelField(title="评价课程数", align=2, sort=11)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@JsonIgnore
	@ExcelField(title="评价总学分", align=2, sort=12)
	public String getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(String commentScore) {
		this.commentScore = commentScore;
	}
	
}
