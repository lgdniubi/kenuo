package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 特殊美容师
 * @author 小叶  2016-12-29 
 *
 */
public class SpecBeautician extends DataEntity<SpecBeautician>{

	private static final long serialVersionUID = 1L;

	private int SpecBeauticianId;    //特殊美容师id
	private String userId;          //特殊美容师的用户id
	private String userName;        //特殊美容师的名字
	private String userPhone;        //特殊美容师手机
	
	private String bazaarId;        //特殊美容师所在市场id
	private String shopId;          //特殊美容师所在店铺id
	private int status;             //特殊美容师状态（0：在职；1：离职）
	private String officeId;        //当前登录用户的office的id
	
	private String shopName;        //特殊美容师所在店铺
	private String bazaarName;      //特殊美容师所在的市场
	
	public int getSpecBeauticianId() {
		return SpecBeauticianId;
	}
	public void setSpecBeauticianId(int specBeauticianId) {
		SpecBeauticianId = specBeauticianId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getBazaarId() {
		return bazaarId;
	}
	public void setBazaarId(String bazaarId) {
		this.bazaarId = bazaarId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBazaarName() {
		return bazaarName;
	}
	public void setBazaarName(String bazaarName) {
		this.bazaarName = bazaarName;
	}
	
	
}
