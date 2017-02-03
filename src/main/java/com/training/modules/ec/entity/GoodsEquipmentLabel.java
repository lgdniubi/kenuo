package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品设备标签中间表实体类
 * @author 小叶   2017-1-9 
 *
 */
public class GoodsEquipmentLabel extends DataEntity<GoodsEquipmentLabel>{

	private static final long serialVersionUID = 1L;

	private int goodsId;        //商品id
	private int labelId;   //设备标签id
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getLabelId() {
		return labelId;
	}
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	
}
