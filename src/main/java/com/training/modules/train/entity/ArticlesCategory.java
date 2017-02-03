package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
/**
 * 文章类别Entity
 * @author coffee
 *
 */
public class ArticlesCategory extends DataEntity<ArticlesCategory>{
	private static final long serialVersionUID = 1L;
	
	private int categoryId;			//文章类别ID
	private String name;				//类别名称
	private int sort;					//排序
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}
