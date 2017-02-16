package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 回看entity
 * @author yangyang
 *
 */
public class TrainLivePlayback extends TreeEntity<TrainLivePlayback> {
	
	private static final long serialVersionUID = 1L;
	private int auditId;							//申请直播id
	private String playbackId;				//回看id
	private String userId;						//直播用户
	private String liveId;						//直播房间id
	private Date bengtime;					//开始时间
	private Date endtime;						//结束时间
	private String name;						//房间名称
	private String desc;						//描述
	private String imgurl;						//图片
	private String playpass;					//密码
	private int collect;							//收藏次数
	private int thumbup;						//点赞数
	private int playNum;						//播放数
	private int downStatus;					//视频是否下载到资源服务器  0：未同步到资源服务器，1：已同步到资源服务器
	private String downUrl;					//视频地址
	private int isShow;							//是否显示
	private int isPay;							//是否付费
	private String userName;					//用户名
	private String label;						//职位
	
	
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public String getPlaybackId() {
		return playbackId;
	}
	public void setPlaybackId(String playbackId) {
		this.playbackId = playbackId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLiveId() {
		return liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	public Date getBengtime() {
		return bengtime;
	}
	public void setBengtime(Date bengtime) {
		this.bengtime = bengtime;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getPlaypass() {
		return playpass;
	}
	public void setPlaypass(String playpass) {
		this.playpass = playpass;
	}
	public int getCollect() {
		return collect;
	}
	public void setCollect(int collect) {
		this.collect = collect;
	}
	public int getThumbup() {
		return thumbup;
	}
	public void setThumbup(int thumbup) {
		this.thumbup = thumbup;
	}
	public int getPlayNum() {
		return playNum;
	}
	public void setPlayNum(int playNum) {
		this.playNum = playNum;
	}
	public int getDownStatus() {
		return downStatus;
	}
	public void setDownStatus(int downStatus) {
		this.downStatus = downStatus;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public int getIsShow() {
		return isShow;
	}
	public int getIsPay() {
		return isPay;
	}
	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	@Override
	public TrainLivePlayback getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(TrainLivePlayback parent) {
		// TODO Auto-generated method stub
		
	}


	

	
	
	

}
