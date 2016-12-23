package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 每天美耶-商品类型表
 * 
 * @author kele
 * @version 2016-6-18
 */
public class GoodsType extends DataEntity<GoodsType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int typeId; 			// 类型Id
	private String name;			// 类型名称
	
	/**
	 * GET/SET
	 */
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
