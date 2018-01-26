package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 预约日志表
 * @author water
 *
 */
public class ApptOrderLog extends DataEntity<ApptOrderLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int apptId;				//日志id	
	private String title;			//标题
	private String content;			//修改内容（用于界面展示）
	private String contentRecord;	//修改内容（用于记录）
	private String channelFlag; 	//渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	private String platformFlag;	//平台标示（mtmy:每天美耶，fzx:妃子校）
	private String createOfficeIds; //操作者机构IDS
	
	public int getApptId() {
		return apptId;
	}
	public void setApptId(int apptId) {
		this.apptId = apptId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentRecord() {
		return contentRecord;
	}
	public void setContentRecord(String contentRecord) {
		this.contentRecord = contentRecord;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getPlatformFlag() {
		return platformFlag;
	}
	public void setPlatformFlag(String platformFlag) {
		this.platformFlag = platformFlag;
	}
	public String getCreateOfficeIds() {
		return createOfficeIds;
	}
	public void setCreateOfficeIds(String createOfficeIds) {
		this.createOfficeIds = createOfficeIds;
	}
	
}
