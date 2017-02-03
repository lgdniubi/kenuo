package com.training.modules.report.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 报表新增用户实体类
 * @author coffee
 *
 */
public class UserReport extends DataEntity<UserReport>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int usernum;		//新增用户数
	private String addtime;		//新增用户添加时间
	
	private Date begtime;		//开始时间
	private Date endtime;		//结束时间
	
	public int getUsernum() {
		return usernum;
	}
	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
}
