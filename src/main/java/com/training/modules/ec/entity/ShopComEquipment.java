package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 店铺通用设备实体类
 * @author 小叶  2017-1-6 
 *
 */

public class ShopComEquipment extends DataEntity<ShopComEquipment>{

	private static final long serialVersionUID = 1L;
	
	private String shopComEquipmentId;   //店铺通用设备id
	private String shopId;            //店铺id
	private int equipmentId;          //通用设备id
	private String equipmentNo;       //通用设备编号
	private String name;              //通用设备名字
	private int isEnabled;            //是否可用
	private int sum;                  //设备数量   
	private int delflag;              //删除标识
	
	private int labelId;             //通用设备标签id
	private String bazaarId;          //店铺对应的市场id

	
	public String getShopComEquipmentId() {
		return shopComEquipmentId;
	}
	public void setShopComEquipmentId(String shopComEquipmentId) {
		this.shopComEquipmentId = shopComEquipmentId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEquipmentNo() {
		return equipmentNo;
	}
	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getBazaarId() {
		return bazaarId;
	}
	public void setBazaarId(String bazaarId) {
		this.bazaarId = bazaarId;
	}
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	
	
	
}
