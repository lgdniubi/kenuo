package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 设备日志
 * @author 小叶  2017-1-11 1
 *
 */
public class EquipmentLogs extends DataEntity<EquipmentLogs>{
	private static final long serialVersionUID = 1L;
	
	private int equipmentId;       //设备id
	private String localBazaarId;  //所在市场id
	private String shopName;       //名称（店铺名称、修）
	private Date apptDate;         //预约时间
	private int time;              //0: 上午 1: 下午
	private String bazaarName;     //市场名称
	
	private String content;        //操作内容（店铺通用设备操作日志用）
	private String comEquipmentId;  //通用设备id
	
	public String getLocalBazaarId() {
		return localBazaarId;
	}
	public void setLocalBazaarId(String localBazaarId) {
		this.localBazaarId = localBazaarId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Date getApptDate() {
		return apptDate;
	}
	public void setApptDate(Date apptDate) {
		this.apptDate = apptDate;
	}
	public String getBazaarName() {
		return bazaarName;
	}
	public void setBazaarName(String bazaarName) {
		this.bazaarName = bazaarName;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getComEquipmentId() {
		return comEquipmentId;
	}
	public void setComEquipmentId(String comEquipmentId) {
		this.comEquipmentId = comEquipmentId;
	}
	
}
