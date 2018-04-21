package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 团购活动对应商品实体类
 * @author coffee
 * @date 2018年3月30日
 */
public class MtmyGroupActivityGoods extends DataEntity<MtmyGroupActivityGoods> {
	
	
	private static final long serialVersionUID = 1L;
	private int gId;				// 主键id
	private String goodsId;			// 商品id
	private int purchaseCount;		// 限购数量
	private double groupActivityPrice;// 团购价
	private double groupActivityAdvancePrice;// 预约金
	private double groupActivityMarketPrice;// 团购市场价
	private int sort;				// 排序
	private Date validityStart;		// 有效期开始 
	private Date validityEnd;		// 有效期结束
	private String exceptDate;		// 除外日期
	private String rangeOfApplication;// 使用范围
	private String otherTips;		// 其他提示
	
	private MtmyGroupActivity mtmyGroupActivity;	// 活动
	private Goods goods;			// 活动中关联的商品
	public int getgId() {
		return gId;
	}
	public void setgId(int gId) {
		this.gId = gId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getPurchaseCount() {
		return purchaseCount;
	}
	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	public double getGroupActivityPrice() {
		return groupActivityPrice;
	}
	public void setGroupActivityPrice(double groupActivityPrice) {
		this.groupActivityPrice = groupActivityPrice;
	}
	public double getGroupActivityAdvancePrice() {
		return groupActivityAdvancePrice;
	}
	public void setGroupActivityAdvancePrice(double groupActivityAdvancePrice) {
		this.groupActivityAdvancePrice = groupActivityAdvancePrice;
	}
	public double getGroupActivityMarketPrice() {
		return groupActivityMarketPrice;
	}
	public void setGroupActivityMarketPrice(double groupActivityMarketPrice) {
		this.groupActivityMarketPrice = groupActivityMarketPrice;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public Date getValidityStart() {
		return validityStart;
	}
	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}
	public Date getValidityEnd() {
		return validityEnd;
	}
	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}
	public String getExceptDate() {
		return exceptDate;
	}
	public void setExceptDate(String exceptDate) {
		this.exceptDate = exceptDate;
	}
	public String getRangeOfApplication() {
		return rangeOfApplication;
	}
	public void setRangeOfApplication(String rangeOfApplication) {
		this.rangeOfApplication = rangeOfApplication;
	}
	public String getOtherTips() {
		return otherTips;
	}
	public void setOtherTips(String otherTips) {
		this.otherTips = otherTips;
	}
	public MtmyGroupActivity getMtmyGroupActivity() {
		return mtmyGroupActivity;
	}
	public void setMtmyGroupActivity(MtmyGroupActivity mtmyGroupActivity) {
		this.mtmyGroupActivity = mtmyGroupActivity;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	
}
