package com.training.modules.ec.entity;


import com.training.common.persistence.DataEntity;

/**
 * banner实体类
 * @author coffee
 *
 */
public class Banner extends DataEntity<Banner>{

	private static final long serialVersionUID = 1L;
	private int bannerId;		//bannerid
	private String bannerName;	//banner名称
	private String imgUrl;		//照片
	private String redirectUrl;	//文本
	private String remark;		//备注
	private int sort;			//排序
	private String isShow;		//是否显示（0：显示；1：不显示） 
	
	private String bannerType;	//位置类型（1：首页；2：分类；3：我的订单；4：商城；5：生活美容；6：医美；）
	private int goodsId;		//商品ID
	private int actionType;		//商品类型（0 普通商品,1 限时抢购, 2 团购 , 3 促销优惠）
	private String isShare;		//是否分享（0：分享；1：不分享）
	private int pageType;		//跳转类型：(0：活动页；1：商品；2：专题页，3：文章)
	
	private String isOpen;             //是否公开（0：公开，1：不公开）
	private String franchiseeIds;   //商家id(以,拼接)
	
	public int getBannerId() {
		return bannerId;
	}
	public void setBannerId(int bannerId) {
		this.bannerId = bannerId;
	}
	public String getBannerName() {
		return bannerName;
	}
	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getBannerType() {
		return bannerType;
	}
	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	public int getPageType() {
		return pageType;
	}
	public void setPageType(int pageType) {
		this.pageType = pageType;
	}
	
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getFranchiseeIds() {
		return franchiseeIds;
	}
	public void setFranchiseeIds(String franchiseeIds) {
		this.franchiseeIds = franchiseeIds;
	}
	
}
