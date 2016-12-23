package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品退货期  实体类
 * @author coffee
 *
 */
public class MtmyReturnGoods extends DataEntity<MtmyReturnGoods>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GoodsCategory goodsCategory;
	
	private int dateVal;		//退货期限

	public GoodsCategory getGoodsCategory() {
		return goodsCategory;
	}
	public void setGoodsCategory(GoodsCategory goodsCategory) {
		this.goodsCategory = goodsCategory;
	}
	public int getDateVal() {
		return dateVal;
	}
	public void setDateVal(int dateVal) {
		this.dateVal = dateVal;
	}
	
}
    