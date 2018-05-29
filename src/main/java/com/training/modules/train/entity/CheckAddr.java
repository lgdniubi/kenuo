package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户认证手机端填写的支付宝和微信信息
 * @author 
 * @version 2018年3月26日
 */
public class CheckAddr extends DataEntity<CheckAddr>{
	
	private static final long serialVersionUID = 1L;
	private String syrMobile;			//手机号
	private String email;			//邮箱
	private String syrAddress;			//详细地址
	private String provinceName;			//省
	private String cityName;			//城市
	private String districtName;			//区
	private String idcard;			//身份证
	public String getSyrMobile() {
		return syrMobile;
	}
	public void setSyrMobile(String syrMobile) {
		this.syrMobile = syrMobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSyrAddress() {
		return syrAddress;
	}
	public void setSyrAddress(String syrAddress) {
		this.syrAddress = syrAddress;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
}