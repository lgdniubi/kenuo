package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 快递物流
 * @author yangyang
 *
 */
public class CourierCompany extends TreeEntity<CourierCompany> {
	
	
	private static final long serialVersionUID = 1L;
	private String courierName;					//物流公司名称
	private String courierNo;					//物流公司编号
	private String remark;						//备注
	private Date createDate;					//日期


	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierNo() {
		return courierNo;
	}

	public void setCourierNo(String courierNo) {
		this.courierNo = courierNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public CourierCompany getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(CourierCompany parent) {
		// TODO Auto-generated method stub
		
	}
	

}
