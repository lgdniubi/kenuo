package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class ArticlesComment extends DataEntity<ArticlesComment>{
	private static final long serialVersionUID = 1L;
	private int commentId;	//评论ID
	private int articleId;
	private String content;		//评论内容
	private int likeNum;		//点赞数
	//获取用户表的数据
	private String createName;	// 姓名
	private String createPhoto;	// 头像
	
	private Date beginDate;			//开始时间
	private Date endDate;			//结束时间
	
	private String articleName;		//文章标题
	private int categoryId;			//分类id
	
	private int flag;				//标示
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
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
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
