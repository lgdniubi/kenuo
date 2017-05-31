package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;


/**
 * goods_supplier 实体类
 * @author 土豆  2017.5.9
 *
 */
public class GoodsSupplier extends DataEntity<GoodsSupplier>{

	private static final long serialVersionUID = 1L;

	private int goodsSupplierId;		//供应商id 
	private String name;				//供应商名称
	private String status;				//供应商状态（0：商用；1：暂停）
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
