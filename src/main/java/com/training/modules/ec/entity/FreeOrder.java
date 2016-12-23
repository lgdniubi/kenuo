package com.training.modules.ec.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
/**
 * 免费体验实体类
 * @author coffee
 *
 */
public class FreeOrder extends DataEntity<FreeOrder>{

	private static final long serialVersionUID = 1L;
	
	private int freeNum;				//免费体验号
	private String userName;			//用户姓名
	private String mobile;				//手机号
	private Date expTime;				//预约时间
	private String shopId;				//预约店铺id
	private String serverId;			//活动id
	private String recommen;			//推荐人id
	private Date addTime;				//添加时间
	private String type;					//体验状态   默认 0 未体验  1 已体验  2 放弃体验
	private String beautyId;			//美容师id
	
	private Office office;				//店铺实体类
	private User user;					//美容师实体类
	private String keyword;				//关键字
	
	//备注表
	private int divId;			//免费体验id
	private String contents;	//备注内容
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getExpTime() {
		return expTime;
	}
	public void setExpTime(Date expTime) {
		this.expTime = expTime;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	public String getRecommen() {
		return recommen;
	}
	public void setRecommen(String recommen) {
		this.recommen = recommen;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBeautyId() {
		return beautyId;
	}
	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
