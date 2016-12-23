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
	private String 	playbackId;					//回看id
	private String userId;
	private String liveId;
	private Date bengtime;
	private Date endtime;
	private String name;
	private String desc;
	private String imgurl;
	private String playpass;
	private int collect;
	private int thumbup;
	private int playNum;

	
	
	
	
	
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
