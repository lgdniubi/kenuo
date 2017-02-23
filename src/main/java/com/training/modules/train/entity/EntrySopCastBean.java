package com.training.modules.train.entity;

public class EntrySopCastBean {

	private String userId; //申请人ID
	private String title;	//直播主题
	private String desc;	//内容描述
	private String imgurl;	//直播封面
	private String bengtime;	//开始时间
	private String playpass;	//密码
	private int audit_status;
	private String liveId;
	private String nowtime;
	private int auditId;
	private int authtype;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getBengtime() {
		return bengtime;
	}
	public void setBengtime(String bengtime) {
		this.bengtime = bengtime;
	}
	public String getPlaypass() {
		return playpass;
	}
	public void setPlaypass(String playpass) {
		this.playpass = playpass;
	}
	public int getAudit_status() {
		return audit_status;
	}
	public void setAudit_status(int audit_status) {
		this.audit_status = audit_status;
	}
	public String getLiveId() {
		return liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	public String getNowtime() {
		return nowtime;
	}
	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public int getAuthtype() {
		return authtype;
	}
	public void setAuthtype(int authtype) {
		this.authtype = authtype;
	}
	
	

	
	
}
