package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;


/**
 * goods_supplier_contacts 实体类
 * @author 土豆  2017.5.9
 *
 */
public class GoodsSupplierContacts extends DataEntity<GoodsSupplierContacts>{

	private static final long serialVersionUID = 1L;
	
	private int goodsSupplierContactsId;	//供应商联系人id
	private int goodsSupplierId;			//供应商id
	private String name;					//联系人名称
	private String mobile;					//联系人电话
	private String status;					//供应商状态（0：商用；1：暂停）
	
	public int getGoodsSupplierContactsId() {
		return goodsSupplierContactsId;
	}
	public void setGoodsSupplierContactsId(int goodsSupplierContactsId) {
		this.goodsSupplierContactsId = goodsSupplierContactsId;
	}
	public int getGoodsSupplierId() {
		return goodsSupplierId;
	}
	public void setGoodsSupplierId(int goodsSupplierId) {
		this.goodsSupplierId = goodsSupplierId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
