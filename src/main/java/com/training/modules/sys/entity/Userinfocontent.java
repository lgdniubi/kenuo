package com.training.modules.sys.entity;

import java.sql.Date;

import com.training.common.persistence.DataEntity;

public class Userinfocontent extends DataEntity<Userinfocontent> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userid;			//用户id
	private String name;			//内容名称
	private	 String	url;			//内容地址
	private int type;				//类型
	private int status;			//状态
	private Date createtime;		//创建时间
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
