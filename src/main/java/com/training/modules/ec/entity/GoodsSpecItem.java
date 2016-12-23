package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品规格项表
 * @author kele
 * @version 2016-6-21
 */
public class GoodsSpecItem extends DataEntity<GoodsSpecItem>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int specItemId;		//规格项Id
	private int specId;			//规格id
	private String specName;	//规格名称
	private String item;		//规格项	
	
	private Integer[] specItemIdList;	//规格项ID数组
	
	/**
	 * get/set
	 */
	public int getSpecItemId() {
		return specItemId;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public void setSpecItemId(int specItemId) {
		this.specItemId = specItemId;
	}
	public int getSpecId() {
		return specId;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Integer[] getSpecItemIdList() {
		return specItemIdList;
	}
	public void setSpecItemIdList(Integer[] specItemIdList) {
		this.specItemIdList = specItemIdList;
	}
	
}
