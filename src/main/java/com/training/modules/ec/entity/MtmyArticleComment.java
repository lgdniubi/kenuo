package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * mtmy文章评论
 * @author coffee
 *
 */
public class MtmyArticleComment extends DataEntity<MtmyArticleComment>{
	private static final long serialVersionUID = 1L;
	
	private int articlesId;		//评论文章id
	private String contents;		//评论内容
	private String createName;		//评论者name
	private String createPhoto;		//评论者头像
	private int likeNum;			//评论点赞数
	
	private int categoryId;     //文章类别ID
	private Date beginDate;		//开始时间
	private Date endDate;		//结束时间
	private String title;        //文章标题
	private int flag;			//标示
	
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
	public int getArticlesId() {
		return articlesId;
	}
	public void setArticlesId(int articlesId) {
		this.articlesId = articlesId;
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}
