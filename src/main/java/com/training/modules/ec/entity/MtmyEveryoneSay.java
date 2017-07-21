package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 大家都在说实体类
 * @author xiaoye  2017年6月7日
 *
 */
public class MtmyEveryoneSay extends DataEntity<MtmyEveryoneSay>{

	private static final long serialVersionUID = 1L;
	
	private int mtmyEveryoneSayId;   //说说id
	private String parentId;     //父ID
	private String sayType;      //说说类型（1：说；2：回；3：评论回复）
	private String positionType;   //位置类型（1：店铺；2：技师；3：商品；）
	private String contentId;   //内容ID
	private String content;    //内容
	private int userId;        //用户ID
	private String userName;   //用户昵称
	private String userPhoto;  //用户头像
	private int userLevel;    //用户等级
	private Date createDate;  //创建时间
	private int likeSum;      //说说点赞数
	private Date begtime;		//查询条件开始时间
	private Date endtime;		//查询条件结束时间
	
	private String name;          //查询条件
	private String showName;      //详情的头上
	
	public int getMtmyEveryoneSayId() {
		return mtmyEveryoneSayId;
	}
	public void setMtmyEveryoneSayId(int mtmyEveryoneSayId) {
		this.mtmyEveryoneSayId = mtmyEveryoneSayId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSayType() {
		return sayType;
	}
	public void setSayType(String sayType) {
		this.sayType = sayType;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getLikeSum() {
		return likeSum;
	}
	public void setLikeSum(int likeSum) {
		this.likeSum = likeSum;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	

}
