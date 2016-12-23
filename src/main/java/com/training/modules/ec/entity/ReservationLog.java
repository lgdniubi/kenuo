/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 预约Entity日志表
 * 
 * @version 2013-12-05
 */
public class ReservationLog extends DataEntity<ReservationLog> {

	private static final long serialVersionUID = 1L;
	private int reservationId;		//预约id
	private String beauticianId;	//美容师id
	private String shopId;			//店铺id
	private int reservationStatus;	//预约状态
	
	private String userName;		//美容师name
	private String officeName;		//店铺name
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public String getBeauticianId() {
		return beauticianId;
	}
	public void setBeauticianId(String beauticianId) {
		this.beauticianId = beauticianId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public int getReservationStatus() {
		return reservationStatus;
	}
	public void setReservationStatus(int reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
}