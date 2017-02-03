package com.training.modules.ec.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.training.common.persistence.DataEntity;

/**
 * 商品规格
 * @author kele
 * @version 2016-6-20
 */
public class GoodsSpec extends DataEntity<GoodsSpec> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int specId; 			// 规格表
	private int typeId;				// 类型id
	private GoodsType goodsType;	// 商品类型类
	private String name; 			// 规格名称
	private int searchIndex;		// 是否需要检索
	private int sort; 				// 排序
	private String specItem;		// 规格项
	private String specItems;		// 规格项
	private String specItemValues;	// 规格项
	
	private List<GoodsSpecItem> specItemList = Lists.newArrayList(); // 规格项
	
	/**
	 * get/set
	 */
	public int getSpecId() {
		return specId;
	}
	public GoodsType getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
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
	public int getSearchIndex() {
		return searchIndex;
	}
	public void setSearchIndex(int searchIndex) {
		this.searchIndex = searchIndex;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public List<GoodsSpecItem> getSpecItemList() {
		return specItemList;
	}
	public void setSpecItemList(List<GoodsSpecItem> specItemList) {
		this.specItemList = specItemList;
	}
	
	public String getSpecItem() {
		return specItem;
	}
	public void setSpecItem(String specItem) {
		this.specItem = specItem;
	}
	/**
	 * 获取规格项的名称,以“,”隔开
	 * @return
	 */
	public String getSpecItems() {
		specItems = "";
		for (int i = 0; i < specItemList.size(); i++) {
			specItems += specItemList.get(i).getItem()+",";
		}
		return specItems.length()>1?specItems.substring(0, specItems.length()-1):specItems;
	}
	
	/**
	 * 获取规格项的名称,以“/n”隔开
	 * @return
	 */
	public String getSpecItemValues() {
		specItemValues = "";
		for (int i = 0; i < specItemList.size(); i++) {
			specItemValues += specItemList.get(i).getItem()+"\n";
		}
		return specItemValues.length()>1?specItemValues.substring(0, specItemValues.length()-1):specItemValues;
	}

}
