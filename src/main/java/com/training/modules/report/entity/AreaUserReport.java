package com.training.modules.report.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户区域分布统计实体类
 * @author coffee
 *
 */
public class AreaUserReport extends DataEntity<AreaUserReport>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int userNum;		//下单用户数
	private String province;	//省name
	private String city;		//市name
	private String district;    //县name
	private int orderNum;		//下单总数
	private String amount;		//下单总金额
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
