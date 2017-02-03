package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 每天美耶-商品分类表
 * @author kele
 * @version 2016-6-15
 */
public class GoodsCategory extends TreeEntity<GoodsCategory>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	categoryId;			//商品分类id
	private String 	name;				//商品分类名称
	private String 	mobileName;			//手机端显示的商品分类名
	private String 	level;				//等级
	private String 	isShow;				//是否显示[0:是；1：否；]
	private String 	isHot;				//是否推荐为热门分类[0:是；1：否；]
	private String 	image;				//分类图片
	private String 	code;				//分类编码
	private String 	catGroup = "0";		//分类分组默认0
	private int     type;				//分类类型   0：普通商品分类，1：抢购商品分类
	
	private int num;					//查看是否有子类
	
	@Override
	public GoodsCategory getParent() {
		return parent;
	}
	@Override
	public void setParent(GoodsCategory parent) {
		this.parent = parent;
	}
	
	/**
	 *	GET/SET 
	 */
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileName() {
		return mobileName;
	}
	public void setMobileName(String mobileName) {
		this.mobileName = mobileName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getCatGroup() {
		return catGroup;
	}
	public void setCatGroup(String catGroup) {
		this.catGroup = catGroup;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}
