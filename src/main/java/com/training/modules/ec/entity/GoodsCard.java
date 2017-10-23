package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 每天美耶-卡项子项表
 * 
 * @author 土豆
 * @version 2017-7-26
 */
public class GoodsCard extends DataEntity<GoodsCard> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int goodsCardId; 		// 主键ID
	private int cardId; 			// 卡项ID
	private int goodsId; 			// 商品ID
	private String goodsName; 		// 商品名称
	private String originalImg; 	// 商品上传原始图
	private int goodsNum; 			// 次（个）数
	private int serviceMin; 		// 服务时长（虚拟商品）
	private String isReal; 			// 是否为实物（0：实物；1：虚拟；）
	private double marketPrice;		// 市场单价
	private double price; 			// 优惠价
	private double totalMarketPrice;// 市场价合计
	private double totalPrice; 		// 优惠价合计
	
	//---------------------多项目预约新加的字段--------
	private int groupId;				//组ID
	private int recId;					//mappingID
	private int serviceTimes;			//预约总次数
	private int usedNum;				//已预约次数
	private int franchiseeId;                 //归属商家
	private String skillId;              //技能标签 （多个id , 隔开）
	private String labelId;              //设备标签（多个id , 隔开）
	//---------------------end--------
	private String positionId;			//项目部位ID
	private String positionIds;		//项目部位ids,包含父类
	
	public int getGoodsCardId() {
		return goodsCardId;
	}
	public void setGoodsCardId(int goodsCardId) {
		this.goodsCardId = goodsCardId;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public int getServiceMin() {
		return serviceMin;
	}
	public void setServiceMin(int serviceMin) {
		this.serviceMin = serviceMin;
	}
	public String getIsReal() {
		return isReal;
	}
	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}
	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOriginalImg() {
		return originalImg;
	}
	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public int getUsedNum() {
		return usedNum;
	}
	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
	public int getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionIds() {
		return positionIds;
	}
	public void setPositionIds(String positionIds) {
		this.positionIds = positionIds;
	}
	
}
