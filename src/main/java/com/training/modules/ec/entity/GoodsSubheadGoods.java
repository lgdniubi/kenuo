package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品副标题活动与商品的中间表
 * @author xiaoye
 *
 */
public class GoodsSubheadGoods extends DataEntity<GoodsSubheadGoods>{

	private static final long serialVersionUID = 1L;
	
	private int goodsSubheadGoodsId;   //中间表id 
	private Goods goods;                //商品实体类
	private GoodsSubhead goodsSubhead;   //商品副标题实体类
	
	private String newGoodsId;              //商品id，用来查询用的
	private String newGoodsName;              //商品id，用来查询用的
	private String newGoodsCategoryId;      //商品分类id，用来查询用的
	private String newGoodsCategoryName;    //商品分类名称，回显用的
	
	public int getGoodsSubheadGoodsId() {
		return goodsSubheadGoodsId;
	}
	public void setGoodsSubheadGoodsId(int goodsSubheadGoodsId) {
		this.goodsSubheadGoodsId = goodsSubheadGoodsId;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public GoodsSubhead getGoodsSubhead() {
		return goodsSubhead;
	}
	public void setGoodsSubhead(GoodsSubhead goodsSubhead) {
		this.goodsSubhead = goodsSubhead;
	}
	public String getNewGoodsId() {
		return newGoodsId;
	}
	public void setNewGoodsId(String newGoodsId) {
		this.newGoodsId = newGoodsId;
	}
	public String getNewGoodsName() {
		return newGoodsName;
	}
	public void setNewGoodsName(String newGoodsName) {
		this.newGoodsName = newGoodsName;
	}
	public String getNewGoodsCategoryId() {
		return newGoodsCategoryId;
	}
	public void setNewGoodsCategoryId(String newGoodsCategoryId) {
		this.newGoodsCategoryId = newGoodsCategoryId;
	}
	public String getNewGoodsCategoryName() {
		return newGoodsCategoryName;
	}
	public void setNewGoodsCategoryName(String newGoodsCategoryName) {
		this.newGoodsCategoryName = newGoodsCategoryName;
	}
	
}
