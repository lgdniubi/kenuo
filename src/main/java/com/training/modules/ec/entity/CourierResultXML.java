package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷表
 * @author water
 *
 */
public class CourierResultXML extends TreeEntity<CourierResultXML> {
	
	
	private static final long serialVersionUID = 1L;
	private String waybillNo;					//运单号
	private String uploadTime;					//时间
	private String processInfo;					//内容
	
	
	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getProcessInfo() {
		return processInfo;
	}

	public void setProcessInfo(String processInfo) {
		this.processInfo = processInfo;
	}

	@Override
	public CourierResultXML getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CourierResultXML parent) {
		// TODO Auto-generated method stub
		
	}
	

}
