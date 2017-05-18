package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.ec.entity.Users;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;

/**
 * 地推Entity
 * @author coffee
 *
 */
public class TrainGtpRelations extends DataEntity<TrainGtpRelations>{
	private static final long serialVersionUID = 1L;
	private int gtpId;
	private int userId;		//邀请人id 妃子校用户 mtmy_user_id
	private String code;	//邀请码
	private int mtmyUserId;	//每天美耶用户id
	private String mtmyUserName;
	private String mobile;
	private int gtpNnm;
	
	private User user;		//妃子校用户
	private Users users;	//每天美耶用户
	private Office office;	//用于检索条件
	private Date beginDate;
	private Date endDate;
	
	public int getGtpId() {
		return gtpId;
	}
	public void setGtpId(int gtpId) {
		this.gtpId = gtpId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getMtmyUserId() {
		return mtmyUserId;
	}
	public void setMtmyUserId(int mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public String getMtmyUserName() {
		return mtmyUserName;
	}
	public void setMtmyUserName(String mtmyUserName) {
		this.mtmyUserName = mtmyUserName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getGtpNnm() {
		return gtpNnm;
	}
	public void setGtpNnm(int gtpNnm) {
		this.gtpNnm = gtpNnm;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}