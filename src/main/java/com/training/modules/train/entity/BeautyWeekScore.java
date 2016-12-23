package com.training.modules.train.entity;

import com.training.common.persistence.BaseEntity;

public class BeautyWeekScore extends BaseEntity<BeautyWeekScore> {

	private static final long serialVersionUID = 5996826215746029070L;
	private int bws_id;
	private String userid;
	private int week;
	private double weekscore;
	private int year;
	private long arearank;
	private String officeid;
	private String areaid;


	public int getBws_id() {
		return bws_id;
	}

	public void setBws_id(int bws_id) {
		this.bws_id = bws_id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public double getWeekscore() {
		return weekscore;
	}

	public void setWeekscore(double weekscore) {
		this.weekscore = weekscore;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public long getArearank() {
		return arearank;
	}

	public void setArearank(long arearank) {
		this.arearank = arearank;
	}

	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}


	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
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
