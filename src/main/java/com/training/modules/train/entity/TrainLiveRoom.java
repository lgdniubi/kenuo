package com.training.modules.train.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 直播间entity
 * @author yangyang
 *
 */
public class TrainLiveRoom extends TreeEntity<TrainLiveRoom> {
	
	private static final long serialVersionUID = 1L;
	private String userId;						//妃子校用户id
	private String roomId;						//直播间id
	private String publishUrl;					//推流地址，第三方推流用户可以获取到此参数


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getPublishUrl() {
		return publishUrl;
	}

	public void setPublishUrl(String publishUrl) {
		this.publishUrl = publishUrl;
	}

	@Override
	public TrainLiveRoom getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(TrainLiveRoom parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
