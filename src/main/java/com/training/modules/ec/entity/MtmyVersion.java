package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 每天美耶版本控制Entity
 * @author coffee
 *
 */
public class MtmyVersion extends DataEntity<MtmyVersion>{
	private static final long serialVersionUID = 1L;
	
	private String versionCode;
	private Date addTime;
	private String remark;
	private int type;
	private String client;
	private int Flag;
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}
}
