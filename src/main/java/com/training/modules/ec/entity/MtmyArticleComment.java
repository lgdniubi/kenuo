package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * mtmy文章评论
 * @author coffee
 *
 */
public class MtmyArticleComment extends DataEntity<MtmyArticleComment>{
	private static final long serialVersionUID = 1L;
	
	private String articlesId;		//评论文章id
	private String contents;		//评论内容
	private String createName;		//评论者name
	private String createPhoto;		//评论者头像
	private int likeNum;			//评论点赞数
	
	public String getArticlesId() {
		return articlesId;
	}
	public void setArticlesId(String articlesId) {
		this.articlesId = articlesId;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getCreatePhoto() {
		return createPhoto;
	}
	public void setCreatePhoto(String createPhoto) {
		this.createPhoto = createPhoto;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
}
