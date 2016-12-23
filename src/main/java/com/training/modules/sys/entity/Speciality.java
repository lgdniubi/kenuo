package com.training.modules.sys.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;

public class Speciality extends DataEntity<Speciality> {
	
	private static final long serialVersionUID = 1L;
	
	private String franchiseeid;		//加盟商id
	private String name;				//特长名称
	private int status;					//状态
	private String createby;			//创建者
	private Date createtime;			//创建时间

	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getFranchiseeid() {
		return franchiseeid;
	}
	public void setFranchiseeid(String franchiseeid) {
		this.franchiseeid = franchiseeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}
