package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 首页广告图实体类
 * @author xiaoye  2017年5月8日
 *
 */
public class MtmyWebAd extends DataEntity<MtmyWebAd>{

	private static final long serialVersionUID = 1L;
	
	private int mtmyWebAdId;  //广告ID
	private int categoryId;   //分类ID
	private String align;     //方向（0：居中；1:左；2：右上；3：右下；）
	private String name;      // 标题
	private String headImg;   //头图
	private String suboriginalImg;  //原始图
	private String positionType;    //位置类型(1：首页；2 : 商城 ; 3：生活美容；)
	private String dataType;       //数据类型（1：动态；2：静态）
	private String redirectUrl;    //链接地址
	private String isShow;         //是否显示(0：显示；1：不显示)
	
	public int getMtmyWebAdId() {
		return mtmyWebAdId;
	}
	public void setMtmyWebAdId(int mtmyWebAdId) {
		this.mtmyWebAdId = mtmyWebAdId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getSuboriginalImg() {
		return suboriginalImg;
	}
	public void setSuboriginalImg(String suboriginalImg) {
		this.suboriginalImg = suboriginalImg;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	
	
}
