package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
/**
 * 
 * 店铺标签管理实体类
 * @author 土豆
 * @version 2017-06-08
 */
public class ShopSpeciality extends DataEntity<ShopSpeciality> {
	
	private static final long serialVersionUID = 1L;
	
	
	private int shopSpecialityid;		//ShopSpeciality的id
	private String franchiseeId;		//加盟商id
	private String name;				//名称
	private String officeId;			//权限id（后台使用）
	
	
	public int getShopSpecialityid() {
		return shopSpecialityid;
	}
	public void setShopSpecialityid(int shopSpecialityid) {
		this.shopSpecialityid = shopSpecialityid;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	
}
