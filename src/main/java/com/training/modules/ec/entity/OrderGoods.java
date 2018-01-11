package com.training.modules.ec.entity;


import java.util.Date;
import java.util.List;

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
	private double ratioPrice;               //异价后价格
 	private String barcode;					//条形码
	private double discount;				//折扣比例
	private double couponPrice;				//红包优惠价格
	private int giveintegral;				//购买商品赠送积分
	private int actiontype;					//订单类型 0 普通订单,1 限时抢购, 2 团购 , 3 促销优惠 
	private int actionid;					//活动id 
	private int issend;						//0未发货，1已发货，2已换货，3已退货
	private int deliveryid;					//发货单ID 
	private Date addtime;					//生成日期
	private Date realityAddTime;			//实际下单时间
	private double membergoodsprice;		//会员折扣价（无用）
	private int remaintimes;				//剩余服务次数
	private int servicemin;					//服务时长
	private int iscomment;					//是否评价（0：未评价；1：已评价；）
	private int isreal;						//是否实物（0：实物商品；1：虚拟商品；2：套卡；3：通用卡）
	private double totalAmount;				//实付款金额
	private double orderAmount;				//应付金额
	private double orderBalance;			//订单余款
	private double orderArrearage;			//订单欠款
	private double singleRealityPrice;		//实际服务单次价
	private double singleNormPrice;			//单次标价
	private int servicetimes;				//预计服务次数
	private int isAfterSales;				//是否售后（0：否；1：是）
	private int expiringDate;				//有效期
	
	private String expiringdate;			//截止时间(为了对应时限卡的有效时间而设置) 土豆添加
	
	
	private Users users;					//商品用户
	private double dealPrice;				//商品成交价格
	private String goodBarCode;				//商品条形码
	private String goodsNo;					//商品编码
	private double totalPrice;				//商品成本总价
	
	private String skillId;					//技能标签id（多个id , 隔开）
	private String labelId;					//设备标签id
	
	private int afterSaleNum;				//申请售后商品数量
//  用于发票
	private int openNum;					//已开发票数量
	private double unitPrice;				//商品实付单次价
	
	private int payRemaintimes;				//订单已存值次数
	private int returnNum;					//查询出的售后商品数量
	//--------------------预约金需要字段------------------------------------------
	private int advanceFlag;             //是否预付金,等于1说明查到advance_flag=1,若不为1，则说明是advance_flag=0
	private double advance;              //预约金
	private int advanceServiceTimes;      //预约金的服务次数
	private double debt;                  //欠款
	private double advanceBalance;        //余额
	private double accountBalance;        //用户账户余额
	private int sumAppt;                  //处理预约金前是否有预约
	private double advancePrice;          //用于详情展示的预约金
	private int goodsType;                 //商品区分(0: 老商品 1: 新商品)
	private String officeId;              //组织架构ID
	//-------------------------------------------------------------------------
	
	
	
	private double goodsBalance;         //商品余额，页面展示用
	
	
	private int groupId;                  //组id，卡项订单，一个卡项商品下有多个子项，每个子项的组id就是那个卡项的recid
	
	private double suitCardBalance;       //套卡剩余金额总和（充值时页面展示用）
	
	// begin---------------用于预约   2017年8月10日新增-----------------
	// 使用到的字段 groupId,recid,orderid,userid,goodsname,goodsid,servicetimes,servicemin,users,remaintimes,isreal,advanceFlag,singleRealityPrice,skillId,labelId
	private double surplusAmount;		// 套卡剩余金额
	private int useServiceTimes;		// 已经服务的次数 （非套卡）
	private int cardUseServiceTimes;	// 套卡总已服务次数
	private int isExpiring;				// 是非已过期 （0：正常 1：过期）
	// end---------------用于预约   2017年8月10日新增-----------------
	
	
	//------------------多项目预约新增加的字段------------------
	//使用到的字段recid，orderid，userid，goodsname，goodsid，goodssn，goodsnum，goodsprice，speckey，
	//speckeyname，servicetimes,servicemin,originalimg,isreal,addtime,realityAddTime,
	//advancePrice,remaintimes,surplusAmount,advanceFlag,cards_used_num
	private int usedServiceTimes;	// 已服务(预约)次数
	private double accountArrearage;   //app欠款
	private List<GoodsCard> goodsCards;        //卡项的子项
	private int franchiseeId;                 //归属商家
	private String positionId;			//项目部位ID
	private String positionIds;		//项目部位ids,包含父类
	//--------------------end------------------------------
	
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
	public int getOpenNum() {
		return openNum;
	}
	public void setOpenNum(int openNum) {
		this.openNum = openNum;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getPayRemaintimes() {
		return payRemaintimes;
	}
	public void setPayRemaintimes(int payRemaintimes) {
		this.payRemaintimes = payRemaintimes;
	}
	public int getAdvanceFlag() {
		return advanceFlag;
	}
	public void setAdvanceFlag(int advanceFlag) {
		this.advanceFlag = advanceFlag;
	}
	public double getAdvance() {
		return advance;
	}
	public void setAdvance(double advance) {
		this.advance = advance;
	}
	public int getAdvanceServiceTimes() {
		return advanceServiceTimes;
	}
	public void setAdvanceServiceTimes(int advanceServiceTimes) {
		this.advanceServiceTimes = advanceServiceTimes;
	}
	public double getAdvanceBalance() {
		return advanceBalance;
	}
	public void setAdvanceBalance(double advanceBalance) {
		this.advanceBalance = advanceBalance;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public double getDebt() {
		return debt;
	}
	public void setDebt(double debt) {
		this.debt = debt;
	}
	public int getSumAppt() {
		return sumAppt;
	}
	public void setSumAppt(int sumAppt) {
		this.sumAppt = sumAppt;
	}
	public double getAdvancePrice() {
		return advancePrice;
	}
	public void setAdvancePrice(double advancePrice) {
		this.advancePrice = advancePrice;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public int getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}
	public double getGoodsBalance() {
		return goodsBalance;
	}
	public void setGoodsBalance(double goodsBalance) {
		this.goodsBalance = goodsBalance;
	}
	public Date getRealityAddTime() {
		return realityAddTime;
	}
	public void setRealityAddTime(Date realityAddTime) {
		this.realityAddTime = realityAddTime;
	}
	public String getExpiringdate() {
		return expiringdate;
	}
	public void setExpiringdate(String expiringdate) {
		this.expiringdate = expiringdate;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public double getSuitCardBalance() {
		return suitCardBalance;
	}
	public void setSuitCardBalance(double suitCardBalance) {
		this.suitCardBalance = suitCardBalance;
	}
	public double getSurplusAmount() {
		return surplusAmount;
	}
	public void setSurplusAmount(double surplusAmount) {
		this.surplusAmount = surplusAmount;
	}
	public int getUseServiceTimes() {
		return useServiceTimes;
	}
	public void setUseServiceTimes(int useServiceTimes) {
		this.useServiceTimes = useServiceTimes;
	}
	public int getCardUseServiceTimes() {
		return cardUseServiceTimes;
	}
	public void setCardUseServiceTimes(int cardUseServiceTimes) {
		this.cardUseServiceTimes = cardUseServiceTimes;
	}
	public int getIsExpiring() {
		return isExpiring;
	}
	public void setIsExpiring(int isExpiring) {
		this.isExpiring = isExpiring;
	}
	public int getUsedServiceTimes() {
		return usedServiceTimes;
	}
	public void setUsedServiceTimes(int usedServiceTimes) {
		this.usedServiceTimes = usedServiceTimes;
	}
	public double getAccountArrearage() {
		return accountArrearage;
	}
	public void setAccountArrearage(double accountArrearage) {
		this.accountArrearage = accountArrearage;
	}
	public List<GoodsCard> getGoodsCards() {
		return goodsCards;
	}
	public void setGoodsCards(List<GoodsCard> goodsCards) {
		this.goodsCards = goodsCards;
	}
	public int getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
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
	public double getRatioPrice() {
		return ratioPrice;
	}
	public void setRatioPrice(double ratioPrice) {
		this.ratioPrice = ratioPrice;
	}
}
