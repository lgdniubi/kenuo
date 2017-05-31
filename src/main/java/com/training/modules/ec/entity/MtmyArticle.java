package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * mtmy文章Entity
 * @author coffee
 *
 */
public class MtmyArticle extends DataEntity<MtmyArticle>{
	private static final long serialVersionUID = 1L;
	private String title;		//文章标题
	private String shortTitle;	//短标题
	private String authorId;	//作者id
	private int type;			//文章类型(0:妃子校 1:每天每耶 2:通用)
	private int categoryId;		//文章分类id
	private String firstImage;	//文章首图
	private String contents;	//文章内容
	private String digest;		//文章摘要
	private int auditFlag;		//审核标示 （0：已发布；1：待审核；）
	private int isShow;			//是否显示（0：显示；1：不显示）
	private String isTopline;   //是否头条（0：否；1：是）
	
	private int flag;			//标示
	private int likeNum;		//点赞
	private int comNum;			//评论总数
	
	private Date beginDate;		//开始时间
	private Date endDate;		//结束时间
	
	private Users users;		//评论人详情（临时  用于体验报名管理）
	
	private String categoryName;  //文章分类名称    小叶  2016.12.22
	private int sort;            //排序
	private int isRecommend;     //是否推荐
	private int isTop;           //是否置顶
	private Date taskDate;		//定时发布时间
	private int isTask;			//是否定时发布
	
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getFirstImage() {
		return firstImage;
	}
	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public int getAuditFlag() {
		return auditFlag;
	}
	public void setAuditFlag(int auditFlag) {
		this.auditFlag = auditFlag;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getComNum() {
		return comNum;
	}
	public void setComNum(int comNum) {
		this.comNum = comNum;
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
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	public int getIsTask() {
		return isTask;
	}
	public void setIsTask(int isTask) {
		this.isTask = isTask;
	}
	public String getIsTopline() {
		return isTopline;
	}
	public void setIsTopline(String isTopline) {
		this.isTopline = isTopline;
	}
	
	
}
