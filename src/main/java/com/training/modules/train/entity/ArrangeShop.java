package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 美容师排班Entity
 * @author coffee
 *
 */
public class ArrangeShop extends DataEntity<ArrangeShop>{
	
	private static final long serialVersionUID = 1L;
	
	private int arrangeId;			//id
	private String localBazaarId;   //所在市场ID
	private String localShopId;		//所在店铺ID
	private String beauticianId;	//美容师ID
	private Date apptDate;			//排班日期
	private int month;				//月
	private int day;				//日
	private String shopId;			//店铺ID（office_id：班；1：休；2：假）
	private String shopName;		//店铺name(对应shopId)
	private String officeId;		//权限id
	
	private String searchOfficeId;	//查询时传officeId
	private String searchOfficeName;//查询时传officeName
	
	private int flag;				//用于标记当前店铺是否上班
	
	private Date searcTime;			//时间
	
	public int getArrangeId() {
		return arrangeId;
	}
	public void setArrangeId(int arrangeId) {
		this.arrangeId = arrangeId;
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
	public String getBeauticianId() {
		return beauticianId;
	}
	public void setBeauticianId(String beauticianId) {
		this.beauticianId = beauticianId;
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
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	public String getSearchOfficeId() {
		return searchOfficeId;
	}
	public void setSearchOfficeId(String searchOfficeId) {
		this.searchOfficeId = searchOfficeId;
	}
	public String getSearchOfficeName() {
		return searchOfficeName;
	}
	public void setSearchOfficeName(String searchOfficeName) {
		this.searchOfficeName = searchOfficeName;
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
