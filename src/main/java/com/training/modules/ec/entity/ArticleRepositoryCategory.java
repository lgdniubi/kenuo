package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * 文章资源Entity
 * @author coffee
 *
 */
public class ArticleRepositoryCategory extends DataEntity<ArticleRepositoryCategory>{
	private static final long serialVersionUID = 1L;
	private int categoryId;			//分类id
	private String name;			//分类名称
	private int sort;			//文章标题
	
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
