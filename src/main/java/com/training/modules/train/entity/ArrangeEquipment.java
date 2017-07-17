package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 特殊设备排班Entity
 * @author coffee
 *
 */
public class ArrangeEquipment extends DataEntity<ArrangeEquipment>{
	
	private static final long serialVersionUID = 1L;
	
	private int eId;				//主键id
	private String localBazaarId;   //所在市场ID
	private String localShopId;		//所在店铺ID
	private int equipmentId;		//id
	private Date apptDate;			//排班
	private int month;				//月
	private int day;				//日
	private int time;				//上午下午
	private String shopId;			//店铺ID（office_id：班；1：休；2：假）
	private String shopName;		//店铺name(对应shopId)
	private String officeId;		//权限id
	
	private int flag;				//用于标记当前店铺是否上班
	
	private Date searcTime;			//时间

	public int geteId() {
		return eId;
	}
	public void seteId(int eId) {
		this.eId = eId;
	}
	public String getLocalBazaarId() {
		return localBazaarId;
	}
	public void setLocalBazaarId(String localBazaarId) {
		this.localBazaarId = localBazaarId;
	}
	public String getLocalShopId() {
		return localShopId;
	}
	public void setLocalShopId(String localShopId) {
		this.localShopId = localShopId;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Date getApptDate() {
		return apptDate;
	}
	public void setApptDate(Date apptDate) {
		this.apptDate = apptDate;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Date getSearcTime() {
		return searcTime;
	}
	public void setSearcTime(Date searcTime) {
		this.searcTime = searcTime;
	}
}
