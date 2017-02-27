package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
/**
 * 作者头像Entity
 * @author coffee
 *
 */
public class ArticleAuthorPhoto extends DataEntity<ArticleAuthorPhoto>{
	private static final long serialVersionUID = 1L;
	private int authorId;			// id
	private String authorName;		// 作者名称
	private String photoUrl;		// 作者头像
	
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
}
