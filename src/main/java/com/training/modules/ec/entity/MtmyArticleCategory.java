package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * mtmy文章类别Entity
 * @author coffee
 *
 */
public class MtmyArticleCategory extends DataEntity<MtmyArticleCategory>{
	private static final long serialVersionUID = 1L;
	
	private int categoryId;			//文章类别ID
	private String name;				//类别名称
	private int sort;					//排序
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
