package com.training.modules.ec.entity;


import com.training.common.persistence.DataEntity;

/**
 * 品类推荐实体类
 * @author 蜜蜂
 *
 */
public class MtmyCategoryRecommended extends DataEntity<MtmyCategoryRecommended>{

	private static final long serialVersionUID = 1L;
	private int categoryId;	//商品分类ID
	private String categoryName;	//商品分类名称
	private String name;	//品类名称(自定义名称)
	private String imgUrl;	//品类图片
	private String isCategoryShow;		//是否分类展示(0：显示 1：不显示)
	private String isMallShow;		//是否商城显示(0：显示 1：不显示)
	private int sort;			//排序
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getIsCategoryShow() {
		return isCategoryShow;
	}
	public void setIsCategoryShow(String isCategoryShow) {
		this.isCategoryShow = isCategoryShow;
	}
	public String getIsMallShow() {
		return isMallShow;
	}
	public void setIsMallShow(String isMallShow) {
		this.isMallShow = isMallShow;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
//	public MtmyCategoryRecommended(int categoryId, String name, String imgUrl, String isCategoryShow, String isMallShow,
//			int sort) {
//		super();
//		this.categoryId = categoryId;
//		this.name = name;
//		this.imgUrl = imgUrl;
//		this.isCategoryShow = isCategoryShow;
//		this.isMallShow = isMallShow;
//		this.sort = sort;
//	}
//	public MtmyCategoryRecommended() {
//		super();
//	}
	
	

	
	
}
