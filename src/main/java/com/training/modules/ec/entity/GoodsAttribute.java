package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品属性
 * @author kele
 * @version 2016-6-20
 */
public class GoodsAttribute extends DataEntity<GoodsAttribute> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attrId; 				// 属性id
	private String typeId; 				// 商品类型ID
	private GoodsType goodsType;		// 商品类型类
	private String attrName;			// 属性名称
	private int attrIndex;				// 检索类型[0-不需要检索;1-关键字检索;2-范围检索;]
	private int attrType;				// 属性类型[0唯一属性 1单选属性 2复选属性]
	private int attrInputType;			// 属性录入方式[0 手工录入 1从列表中选择 2多行文本框]
	private String attrInputTypeName;	// 属性录入方式[0 手工录入 1从列表中选择 2多行文本框]
	private String attrValues;			// 属性值[可选值列表]
	private int sort;					// 属性排序
	
	/**
	 * get/set
	 */
	public String getAttrId() {
		return attrId;
	}
	public String getAttrInputTypeName() {
		return attrInputTypeName;
	}
	public void setAttrInputTypeName(String attrInputTypeName) {
		this.attrInputTypeName = attrInputTypeName;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	public GoodsType getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public int getAttrIndex() {
		return attrIndex;
	}
	public void setAttrIndex(int attrIndex) {
		this.attrIndex = attrIndex;
	}
	public int getAttrType() {
		return attrType;
	}
	public void setAttrType(int attrType) {
		this.attrType = attrType;
	}
	public int getAttrInputType() {
		return attrInputType;
	}
	public void setAttrInputType(int attrInputType) {
		this.attrInputType = attrInputType;
	}
	public String getAttrValues() {
		return attrValues;
	}
	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}
