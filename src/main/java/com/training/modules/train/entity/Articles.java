package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 文章Entity
 * @author coffee
 *
 */
public class Articles extends DataEntity<Articles>{
	private static final long serialVersionUID = 1L;
	private int articleId;				//文章id
	private int categoryId;				//文章类别ID
	private String authorName;			//作者
	private String authorPhoto;			//作者头像
	private String title;				//标题
	private String shortTitle;			//短标题
	private int imageType;				//图片类型（0：无图；1：一图；2：二图；3：三图）
	private String keywords;			//关键字
	private String digest;				//摘要
	private String contents;			//文章内容
	private int isRecommend;			//是否推荐
	private int isShow;					//是否展示
	private int isTop;					//是否置顶
	private int sort;					//排序
	private int auditFlag;				//是否发布
	private int isTask;					//是否定时发布
	private Date taskDate;				//定时发布时间
	
	private Date beginDate;			//开始时间
	private Date endDate;			//结束时间
	
	private int commentNum;				//评论人数
	private ArticlesCategory category;	//文章分类
	private int flag;					//标示
	
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorPhoto() {
		return authorPhoto;
	}
	public void setAuthorPhoto(String authorPhoto) {
		this.authorPhoto = authorPhoto;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public int getImageType() {
		return imageType;
	}
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getAuditFlag() {
		return auditFlag;
	}
	public void setAuditFlag(int auditFlag) {
		this.auditFlag = auditFlag;
	}
	public int getIsTask() {
		return isTask;
	}
	public void setIsTask(int isTask) {
		this.isTask = isTask;
	}
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
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
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public ArticlesCategory getCategory() {
		return category;
	}
	public void setCategory(ArticlesCategory category) {
		this.category = category;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
