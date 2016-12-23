package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;


/**
 * 用户特长中间表实体类
 * @author water
 *
 */
public class UserSpeciality extends DataEntity<UserSpeciality> {
	
	private static final long serialVersionUID = 1L;
	
	private String userid;			//用户id
	private String specialityid;	//特长id
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSpecialityid() {
		return specialityid;
	}
	public void setSpecialityid(String specialityid) {
		this.specialityid = specialityid;
	}

}
