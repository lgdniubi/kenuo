package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商家主页banner 实体类
 * @author 土豆
 *
 */
public class MtmyFranchiseeBanner extends DataEntity<MtmyFranchiseeBanner>{

	private static final long serialVersionUID = 1L;
	private int franchiseeId;	//商家Id
	private String franchiseeName;	//商家名称
	private String bannerName;	//banner图名称
	private String img;			//照片
	private String redirectUrl;	//链接
	private String isShow;		//是否显示（0：显示；1：不显示） 
	private int sort;			//排序
	private String remark;		//备注
	
	
	public int getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	public String getBannerName() {
		return bannerName;
	}
	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
