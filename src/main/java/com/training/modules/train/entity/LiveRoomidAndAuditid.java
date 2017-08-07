package com.training.modules.train.entity;

public class LiveRoomidAndAuditid {

	private String roomid;			//房间id
	private int auditid;			//申请id
	private String bengtime;		//开始时间
	private int mtmyUserId;		//每天美耶id
	private double proportion;		//分成比例
	private int companyId;		//商家ID
	
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public int getAuditid() {
		return auditid;
	}
	public void setAuditid(int auditid) {
		this.auditid = auditid;
	}
	public String getBengtime() {
		return bengtime;
	}
	public void setBengtime(String bengtime) {
		this.bengtime = bengtime;
	}
	public int getMtmyUserId() {
		return mtmyUserId;
	}
	public void setMtmyUserId(int mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	
}
