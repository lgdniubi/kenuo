package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 版本管理
 * @author 
 * @version 2018年3月15日
 */
public class TrainModel extends DataEntity<TrainModel>{

	
	private static final long serialVersionUID = 1L;
	private String modName;		//版本名称
	private String modType;		//用户类型
	private String modEname;	//英文名称
	private String modPay;		//是否收费(0:免费1:收费)
	private double modFee;		//价格
	private int modDay;			//天数
	private String remark;		//备注

	private int menuId;		//菜单id
	private String menuIds;	//菜单ids
	
	public TrainModel() {
		super();
	}

	public String getModName() {
		return modName;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}

	public String getModType() {
		return modType;
	}

	public void setModType(String modType) {
		this.modType = modType;
	}

	public String getModEname() {
		return modEname;
	}

	public void setModEname(String modEname) {
		this.modEname = modEname;
	}

	public String getModPay() {
		return modPay;
	}

	public void setModPay(String modPay) {
		this.modPay = modPay;
	}

	public double getModFee() {
		return modFee;
	}

	public void setModFee(double modFee) {
		this.modFee = modFee;
	}

	public int getModDay() {
		return modDay;
	}

	public void setModDay(int modDay) {
		this.modDay = modDay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Override
	public String toString() {
		return "TrainModel [modName=" + modName + ", modType=" + modType + ", modEname=" + modEname + ", modPay="
				+ modPay + ", modFee=" + modFee + ", modDay=" + modDay + ", remark=" + remark + "]";
	}

	
	
}
