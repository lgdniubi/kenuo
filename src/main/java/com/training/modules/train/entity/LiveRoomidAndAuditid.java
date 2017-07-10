package com.training.modules.train.entity;

public class LiveRoomidAndAuditid {

	private String roomid;			//房间id
	private int auditid;			//申请id
	private String bengtime;		//开始时间
	private int mtmy_user_id;		//每天美耶id
	private double proportion;		//分成比例
	private int company_id;		//商家ID
	
	
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public int getMtmy_user_id() {
		return mtmy_user_id;
	}
	public void setMtmy_user_id(int mtmy_user_id) {
		this.mtmy_user_id = mtmy_user_id;
	}
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
	
	
}
