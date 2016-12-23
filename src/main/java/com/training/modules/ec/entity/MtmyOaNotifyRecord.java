/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.entity;

import org.hibernate.validator.constraints.Length;

import com.training.common.persistence.DataEntity;

import java.util.Date;


/**
 * 每天每夜通知通告记录Entity
 * 
 * @version 2014-05-16
 */
public class MtmyOaNotifyRecord extends DataEntity<MtmyOaNotifyRecord> {
	
	private static final long serialVersionUID = 1L;
	private MtmyOaNotify mtmyOaNotify;		// 通知通告ID
	private Users users;						// 接受人
	private String readFlag;				// 阅读标记（0：未读；1：已读）
	private Date readDate;					// 阅读时间
	
	
	public MtmyOaNotifyRecord() {
		super();
	}

	public MtmyOaNotifyRecord(String id){
		super(id);
	}
	
	public MtmyOaNotifyRecord(MtmyOaNotify mtmyOaNotify){
		this.mtmyOaNotify = mtmyOaNotify;
	}

	public MtmyOaNotify getMtmyOaNotify() {
		return mtmyOaNotify;
	}

	public void setMtmyOaNotify(MtmyOaNotify mtmyOaNotify) {
		this.mtmyOaNotify = mtmyOaNotify;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@Length(min=0, max=1, message="阅读标记（0：未读；1：已读）长度必须介于 0 和 1 之间")
	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	
	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	
}