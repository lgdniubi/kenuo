package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.BeanUtil;
import com.training.common.utils.StringUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;

/**
 * 商品规格价格表
 * @author kele
 * @version 2016-6-22
 */
public class GoodsSpecPrice extends DataEntity<GoodsSpecPrice> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goodsId;			// 商品id
	private String specKey;			// 规格键
	private String specKeyValue;	// 规格键名中文
	private double price;			// 优惠价格
	private double marketPrice;		// 市场价格
	private int storeCount;			// 库存数量
	private String barCode;			// 商品条形码
	private String goodsNo;			//商品编码
	private int serviceTimes;		// 服务次数
	private int expiringDate;		// 截止日期（月）
	private int goodsWeight;	//商品重量
	
	private String specKeyName;	//规格名称
	private double costPrice;       //成本价格
	
	private double cargoPrice;		//报货价格
	private double purchasePrice;	//采购价格
	private String suplierGoodsNo;	//供应商商品编号
	/**
	 * get/set
	 */
	public String getGoodsId() {
		return goodsId;
	}
	public int getGoodsWeight() {
		return goodsWeight;
	}
	public void setGoodsWeight(int goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getSpecKey() {
		return specKey;
	}
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}
	public String getSpecKeyValue() {
		return specKeyValue;
	}
	public void setSpecKeyValue(String specKeyValue) {
		this.specKeyValue = specKeyValue;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getStoreCount() {
		RedisClientTemplate redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
		String store_count = redisClientTemplate.get(RedisConfig.GOODS_SPECPRICE_PREFIX + this.getGoodsId()+"#"+this.specKey);
		if(StringUtils.isNotBlank(store_count)){
			storeCount = Integer.parseInt(store_count);
		}
		return storeCount;
	}
	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public int getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(int expiringDate) {
		this.expiringDate = expiringDate;
	}
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public double getCargoPrice() {
		return cargoPrice;
	}
	public void setCargoPrice(double cargoPrice) {
		this.cargoPrice = cargoPrice;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getSuplierGoodsNo() {
		return suplierGoodsNo;
	}
	public void setSuplierGoodsNo(String suplierGoodsNo) {
		this.suplierGoodsNo = suplierGoodsNo;
	}
	
}
