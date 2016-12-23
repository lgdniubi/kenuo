package com.training.modules.sys.entity;

import com.training.common.persistence.BaseEntity;

public class UserVo extends BaseEntity<UserVo> {

	private static final long serialVersionUID = -8861168162471198768L;
	private String userid;
	private double totlescore;
	private int usertype;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getTotlescore() {
		return totlescore;
	}
	public void setTotlescore(double totlescore) {
		this.totlescore = totlescore;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
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
