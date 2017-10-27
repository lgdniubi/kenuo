package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 新客表对应的实体类（mtmy_customer）
 * @author xiaoye   2017年10月26日
 *
 */
public class Customer extends DataEntity<Customer>{

	private static final long serialVersionUID = 1L;
	
	private int cusId;           //顾客id
	private int userId;          //消费者id（mtmy用户id）
	private int franchiseeId;    //绑定商家id
	private String officeId;     //绑定店铺id
	private String officePids;    //绑定店铺PIDS
	private String beautyId;      //绑定美容师id
	private Date officeTime;      //绑定店铺时间
	private Date franchiseeTime;   //绑定商家时间
	private Date beautyTime;      //绑定美容师时间
	
	public int getCusId() {
		return cusId;
	}
	public void setCusId(int cusId) {
		this.cusId = cusId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficePids() {
		return officePids;
	}
	public void setOfficePids(String officePids) {
		this.officePids = officePids;
	}
	public String getBeautyId() {
		return beautyId;
	}
	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}
	public Date getOfficeTime() {
		return officeTime;
	}
	public void setOfficeTime(Date officeTime) {
		this.officeTime = officeTime;
	}
	public Date getFranchiseeTime() {
		return franchiseeTime;
	}
	public void setFranchiseeTime(Date franchiseeTime) {
		this.franchiseeTime = franchiseeTime;
	}
	public Date getBeautyTime() {
		return beautyTime;
	}
	public void setBeautyTime(Date beautyTime) {
		this.beautyTime = beautyTime;
	}
	
	
}
