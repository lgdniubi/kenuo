package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 广告图分类实体类
 * @author xiaoye 2017年5月4日
 *
 */
public class MtmyWebAdCategory extends DataEntity<MtmyWebAdCategory>{

	private static final long serialVersionUID = 1L;
	
	private int mtmyWebAdCategoryId; //分类id
	private String parentId;         //父id
	private String parentIds;       //祖宗id
	private int level;              //等级(目前只有二级1,2),默认为1
	private String name;            //分类名称
	private String positionType;    //位置类型(1：首页；2 : 商城 ; 3：生活美容；)
	private int sort;              //顺序排序
	private String isShow;         //是否显示(0：显示；1：不显示)
	private int num;              ////父类数量
	
	public int getMtmyWebAdCategoryId() {
		return mtmyWebAdCategoryId;
	}
	public void setMtmyWebAdCategoryId(int mtmyWebAdCategoryId) {
		this.mtmyWebAdCategoryId = mtmyWebAdCategoryId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
	
}
