package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * 文章资源Entity
 * @author coffee
 *
 */
public class ArticleImage extends DataEntity<ArticleImage>{
	private static final long serialVersionUID = 1L;
	private int imgId;					//id
	private int articleId;			//分类id
	private String imgUrl;		//文章作者
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
