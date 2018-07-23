package com.training.modules.track.entity;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：  	TOrderGoodsRecharge
 * 类描述：	埋点-机构信息
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午3:48:57
 */
public class TOffice extends DataEntity<TOffice> {
	
	private static final long serialVersionUID = -6871499465731124505L;
	// 机构ID
	private String officeId;
	// 机构ID
	private String officeName;
	
	/**
	 * @return the officeId
	 */
	public String getOfficeId() {
		return officeId;
	}
	/**
	 * @param officeId the officeId to set
	 */
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}
	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}	
}
