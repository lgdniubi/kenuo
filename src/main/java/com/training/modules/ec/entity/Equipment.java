package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

public class Equipment extends DataEntity<Equipment>{

	private static final long serialVersionUID = 1L;

	private int equipmentId; //设备id
	private String name;     //设备名称
	private int type;        //设备类型
	private String officeId; //使用该设备的用户的机构id
	
	private String bazaarId; // 特殊设备对应的市场id
	private String shopId;   // 特殊设备对应的店铺id
	private String bazaarName;  //特殊设备对应的市场名称
	private String shopName;	//特殊店铺对应的店铺名称
	
	private int labelId;        //设备标签表的id
	private String labelName;   //设备标签的name
	
	private int flag;      //标识
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getBazaarId() {
		return bazaarId;
	}
	public void setBazaarId(String bazaarId) {
		this.bazaarId = bazaarId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getBazaarName() {
		return bazaarName;
	}
	public void setBazaarName(String bazaarName) {
		this.bazaarName = bazaarName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	
}
