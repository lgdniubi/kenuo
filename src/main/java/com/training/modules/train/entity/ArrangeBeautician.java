package com.training.modules.train.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;
/**
 * 排班Entity
 * @author coffee
 *
 */
public class ArrangeBeautician extends DataEntity<ArrangeBeautician>{
	
	private static final long serialVersionUID = 1L;
	//用于特殊美容师
	private String userId;			//美容师id
	private String name;			//美容师name
	private String userOfficeId;	//美容师所在店铺id
	private List<ArrangeShop> arrangeShops;
	private String userids;			//用于获取排班所有美容师用户id
	// 用于查询市场下所有机构
	private String officeId;		//店铺id

	//用于特殊设备
	private int equipmentId;		//特殊设备id
	private String equipmentName;	//特殊设备name
	private String equipmentIds;		//用于获取排班所有特殊设备id
	private List<ArrangeEquipment> arrangeEquipments;
	
	private int beauticianStatus;	//特殊美容师状态 （0  特殊美容师 1 普通美容师）
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserOfficeId() {
		return userOfficeId;
	}
	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}
	public List<ArrangeShop> getArrangeShops() {
		return arrangeShops;
	}
	public void setArrangeShops(List<ArrangeShop> arrangeShops) {
		this.arrangeShops = arrangeShops;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getUserids() {
		return userids;
	}
	public void setUserids(String userids) {
		this.userids = userids;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getEquipmentIds() {
		return equipmentIds;
	}
	public void setEquipmentIds(String equipmentIds) {
		this.equipmentIds = equipmentIds;
	}
	public List<ArrangeEquipment> getArrangeEquipments() {
		return arrangeEquipments;
	}
	public void setArrangeEquipments(List<ArrangeEquipment> arrangeEquipments) {
		this.arrangeEquipments = arrangeEquipments;
	}
	public int getBeauticianStatus() {
		return beauticianStatus;
	}
	public void setBeauticianStatus(int beauticianStatus) {
		this.beauticianStatus = beauticianStatus;
	}	
	
}
