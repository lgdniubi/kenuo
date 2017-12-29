package com.training.modules.ec.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 城市异价实体类
 * @author xiaoye  2017年12月27日
 *
 */
public class GoodsPriceRatio extends DataEntity<GoodsPriceRatio> {

	private static final long serialVersionUID = 1L;
	
	private int goodsPriceRatioId;    //主键id
	private double ratio;             //异价比例(0.01到1）
	private int goodsId;               //商品id
	private String goodsIds;           //商品ids
	private String goodsNames;          //商品名字拼接的字符串
	private String cityIds;            //城市ids
	private String cityNames;          //城市名字拼接的字符串
	private List<Integer> goodsIdsList;   //城市ids集合
	private int delflag;                //删除标示(0:正常  1：删除)
	
	private String newRatio;           //异价比例（list页面查询条件用）
	private double minRatio;           //异价比例的小值
	private double maxRatio;           //异价比例的大值
	
	public int getGoodsPriceRatioId() {
		return goodsPriceRatioId;
	}
	public void setGoodsPriceRatioId(int goodsPriceRatioId) {
		this.goodsPriceRatioId = goodsPriceRatioId;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	
	public String getCityIds() {
		return cityIds;
	}
	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}
	public String getCityNames() {
		return cityNames;
	}
	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
	}
	public String getGoodsIds() {
		return goodsIds;
	}
	public void setGoodsIds(String goodsIds) {
		this.goodsIds = goodsIds;
	}
	public String getGoodsNames() {
		return goodsNames;
	}
	public void setGoodsNames(String goodsNames) {
		this.goodsNames = goodsNames;
	}
	public List<Integer> getGoodsIdsList() {
		return goodsIdsList;
	}
	public void setGoodsIdsList(List<Integer> goodsIdsList) {
		this.goodsIdsList = goodsIdsList;
	}
	public String getNewRatio() {
		return newRatio;
	}
	public void setNewRatio(String newRatio) {
		this.newRatio = newRatio;
	}
	public double getMinRatio() {
		return minRatio;
	}
	public void setMinRatio(double minRatio) {
		this.minRatio = minRatio;
	}
	public double getMaxRatio() {
		return maxRatio;
	}
	public void setMaxRatio(double maxRatio) {
		this.maxRatio = maxRatio;
	}
	
	
}
