package com.training.modules.personnelfile.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 用户图片Entity
 * 
 * @version 2013-05-15
 */
public class UserImages extends DataEntity<UserImages> {

	private static final long serialVersionUID = 6510590215925352897L;
	

	private String userId;//用户ID
	private int imgType;//照片类型(1:身份证正面;2:身份证反面;3:生活照;4:着装工作照;5:与店铺合照;)
	private String imgLocation; //照片路径
	private String imgUrl;//链接地址
	private String imgDescribe;//描述
	private Date createDate;//创建时间
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getImgType() {
		return imgType;
	}
	public void setImgType(int imgType) {
		this.imgType = imgType;
	}
	public String getImgLocation() {
		return imgLocation;
	}
	public void setImgLocation(String imgLocation) {
		this.imgLocation = imgLocation;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgDescribe() {
		return imgDescribe;
	}
	public void setImgDescribe(String imgDescribe) {
		this.imgDescribe = imgDescribe;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}