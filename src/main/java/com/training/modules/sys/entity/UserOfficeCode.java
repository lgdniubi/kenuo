package com.training.modules.sys.entity;

import com.training.common.persistence.BaseEntity;

public class UserOfficeCode extends BaseEntity<UserOfficeCode> {
	
	private static final long serialVersionUID = -5467754351324443552L;
	private String userid;
	private String officeid;


	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}

	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

}
