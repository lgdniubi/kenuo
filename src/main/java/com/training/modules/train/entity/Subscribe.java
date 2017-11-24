package com.training.modules.train.entity;

import com.training.common.persistence.TreeEntity;

public class Subscribe extends TreeEntity<Subscribe> {

	private static final long serialVersionUID = 1L;
	private int appt_id;					//预约id
	private String beautician_id;			//预约美容师id
	private String shop_id;					//预约店铺id
	private int service_min;				//项目时长
	

	public int getAppt_id() {
		return appt_id;
	}
	public void setAppt_id(int appt_id) {
		this.appt_id = appt_id;
	}
	public String getBeautician_id() {
		return beautician_id;
	}
	public void setBeautician_id(String beautician_id) {
		this.beautician_id = beautician_id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public int getService_min() {
		return service_min;
	}
	public void setService_min(int service_min) {
		this.service_min = service_min;
	}
	@Override
	public Subscribe getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(Subscribe parent) {
		// TODO Auto-generated method stub
		
	}
	
	
}
