package com.training.modules.ec.entity;


import java.util.Date;

import com.training.common.persistence.TreeEntity;
/**
 *mtmy_order_goods_mapping实体
 * @author zhangyang
 *
 */
public class OrderGoods extends TreeEntity<OrderGoods> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int recid;						//表id
	private String orderid;					//订单id
	private int userid;						//用户id
	private int goodsid;					//商品id
	private String goodsname;				//商品名称
	private String	goodssn;				//商品货号
	private int goodsnum;					//商品数量
	private String originalimg;				//商品图片
	private String speckey;					//商品规格key
	private String speckeyname;				//规格对应的中文名字
	private double costprice;				//商品成本价格
	private double marketprice;				//商品市场单价
	private double goodsprice;				//商品优惠单价
	private String barcode;					//条形码
	private double discount;				//折扣比例
	private double couponPrice;				//红包优惠价格
	private int giveintegral;				//购买商品赠送积分
	private int actiontype;					//订单类型 0 普通订单,1 限时抢购, 2 团购 , 3 促销优惠 
	private int actionid;					//活动id 
	private int issend;						//0未发货，1已发货，2已换货，3已退货
	private int deliveryid;					//发货单ID 
	private Date addtime;					//生成日期
	private double membergoodsprice;		//会员折扣价（无用）
	private int remaintimes;				//剩余服务次数
	private int servicemin;					//服务时长
	private int iscomment;					//是否评价（0：未评价；1：已评价；）
	private int isreal;						//是否实物（0：实物商品；1：虚拟商品）
	private double totalAmount;				//实付款金额
	private double orderAmount;				//应付金额
	private double orderBalance;			//订单余款
	private double orderArrearage;			//订单欠款
	private double singleRealityPrice;		//实际服务单次价
	private double singleNormPrice;			//单次标价
	private int servicetimes;				//预计服务次数
	private int isAfterSales;				//是否售后（0：否；1：是）
	private int expiringDate;				//有效期
	
	
	private Users users;					//商品用户
	private double dealPrice;				//商品成交价格
	private String goodBarCode;				//商品条形码
	private String goodsNo;					//商品编码
	private double totalPrice;				//商品成本总价
	
	private String skillId;					//技能标签id（多个id , 隔开）
	private String labelId;					//设备标签id
	
	private int afterSaleNum;				//申请售后商品数量
	
	public int getAfterSaleNum() {
		return afterSaleNum;
	}
	public void setAfterSaleNum(int afterSaleNum) {
		this.afterSaleNum = afterSaleNum;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getIsAfterSales() {
		return isAfterSales;
	}
	public void setIsAfterSales(int isAfterSales) {
		this.isAfterSales = isAfterSales;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getOrderBalance() {
		return orderBalance;
	}
	public void setOrderBalance(double orderBalance) {
		this.orderBalance = orderBalance;
	}
	public double getOrderArrearage() {
		return orderArrearage;
	}
	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}
	public double getSingleRealityPrice() {
		return singleRealityPrice;
	}
	public void setSingleRealityPrice(double singleRealityPrice) {
		this.singleRealityPrice = singleRealityPrice;
	}
	public double getSingleNormPrice() {
		return singleNormPrice;
	}
	public void setSingleNormPrice(double singleNormPrice) {
		this.singleNormPrice = singleNormPrice;
	}
	public int getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(int expiringDate) {
		this.expiringDate = expiringDate;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getRecid() {
		return recid;
	}
	public void setRecid(int recid) {
		this.recid = recid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public String getGoodssn() {
		return goodssn;
	}
	public void setGoodssn(String goodssn) {
		this.goodssn = goodssn;
	}
	public int getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}
	public double getMarketprice() {
		return marketprice;
	}
	public void setMarketprice(double marketprice) {
		this.marketprice = marketprice;
	}
	public double getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(double goodsprice) {
		this.goodsprice = goodsprice;
	}
	public double getCostprice() {
		return costprice;
	}
	public void setCostprice(double costprice) {
		this.costprice = costprice;
	}
	public double getMembergoodsprice() {
		return membergoodsprice;
	}
	public void setMembergoodsprice(double membergoodsprice) {
		this.membergoodsprice = membergoodsprice;
	}
	public int getGiveintegral() {
		return giveintegral;
	}
	public void setGiveintegral(int giveintegral) {
		this.giveintegral = giveintegral;
	}
	public String getSpeckey() {
		return speckey;
	}
	public void setSpeckey(String speckey) {
		this.speckey = speckey;
	}
	public String getSpeckeyname() {
		return speckeyname;
	}
	public void setSpeckeyname(String speckeyname) {
		this.speckeyname = speckeyname;
	}
	public int getIscomment() {
		return iscomment;
	}
	public void setIscomment(int iscomment) {
		this.iscomment = iscomment;
	}
	public int getActiontype() {
		return actiontype;
	}
	public void setActiontype(int actiontype) {
		this.actiontype = actiontype;
	}
	public int getActionid() {
		return actionid;
	}
	public void setActionid(int actionid) {
		this.actionid = actionid;
	}
	public int getIssend() {
		return issend;
	}
	public void setIssend(int issend) {
		this.issend = issend;
	}
	public int getDeliveryid() {
		return deliveryid;
	}
	public void setDeliveryid(int deliveryid) {
		this.deliveryid = deliveryid;
	}
	
	public String getOriginalimg() {
		return originalimg;
	}
	public void setOriginalimg(String originalimg) {
		this.originalimg = originalimg;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public int getServicetimes() {
		return servicetimes;
	}
	public void setServicetimes(int servicetimes) {
		this.servicetimes = servicetimes;
	}
	public int getRemaintimes() {
		return remaintimes;
	}
	public void setRemaintimes(int remaintimes) {
		this.remaintimes = remaintimes;
	}
	public int getIsreal() {
		return isreal;
	}
	public void setIsreal(int isreal) {
		this.isreal = isreal;
	}
	
	public int getServicemin() {
		return servicemin;
	}
	public void setServicemin(int servicemin) {
		this.servicemin = servicemin;
	}
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public double getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}
	public double getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(double dealPrice) {
		this.dealPrice = dealPrice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	@Override
	public OrderGoods getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrderGoods parent) {
		// TODO Auto-generated method stub
		
	}
	public String getGoodBarCode() {
		return goodBarCode;
	}
	public void setGoodBarCode(String goodBarCode) {
		this.goodBarCode = goodBarCode;
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
	
}
