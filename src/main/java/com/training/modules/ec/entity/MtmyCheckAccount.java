package com.training.modules.ec.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 对账实体类
 * @author coffee
 *
 */
public class MtmyCheckAccount extends DataEntity<MtmyCheckAccount>{

	private static final long serialVersionUID = 1L;
	// 0：导出导入；1：仅导出；2：仅导入
	//mtmy_bank_account 
	private String orderNo;				//商户订单号(银行订单号)
	private String pingId;				//ping++订单号
	private Date   payDate;				//交易时间
	private String payAmount;			//实收金额
	private String payChannel;			//交易渠道(支付方式)
	private String payRemark;			//交易详情
	private Date   createDate;			//创建时间
	private String groupFlag;			//标示
	
	//mtmy_orders 
	private String orderId;				//每天每夜订单号
	private String orderAmount;			//支付金额
	private String consignee;			//收货人
	private String mobile1;				//收货者手机号码
	private String address;				//地址
	private String officeName;			//市场
	private Date   addTime;				//下单时间
	private String shippingCode;		//物流订单号
	private String shippingName;		//物流名称
	private String shippingTime;        //发货时间
	private String remark;				//备注
	
	//mtmy_users 
	private String userId;				//用户ID
	private String nickname;			//昵称
	private String mobile;				//手机号码
	
	//mtmy_order_goods_mapping 
	private String goodsId;				//商品ID
	private String goodsSn;				//货号
	private String goodsNum;			//购买数量
	private String goodsName;			//商品名称
	private String specKeyName;			//商品规格
	private String goodsPrice;			//商品原价
	
	private	String goodsNo;			//商品编码
	private String barCode;			//商品条形码
	
	private Date begtime;				//开始时间
	private Date endtime;				//结束时间
	
	@JsonIgnore
	@NotNull(message="商户订单号不能为空")
	@ExcelField(title="商户订单号", type=0, align=2, sort=20)
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@JsonIgnore
	@NotNull(message="ping++订单id不能为空")
	@ExcelField(title="ping++订单id", type=2, align=2, sort=10)
	public String getPingId() {
		return pingId;
	}
	public void setPingId(String pingId) {
		this.pingId = pingId;
	}
	@JsonIgnore
	@NotNull(message="交易时间不能为空")
	@ExcelField(title="交易时间", type=0, align=2, sort=30)
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@JsonIgnore
	@NotNull(message="实收金额不能为空")
	@ExcelField(title="实收金额", type=0, align=2, sort=40)
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	@JsonIgnore
	@NotNull(message="交易渠道不能为空")
	@ExcelField(title="交易渠道", type=0, align=2, sort=50)
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	@JsonIgnore
	@NotNull(message="交易详情不能为空")
	@ExcelField(title="交易详情", type=2, align=2, sort=60)
	public String getPayRemark() {
		return payRemark;
	}
	public void setPayRemark(String payRemark) {
		this.payRemark = payRemark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getGroupFlag() {
		return groupFlag;
	}
	public void setGroupFlag(String groupFlag) {
		this.groupFlag = groupFlag;
	}
	
	
	@JsonIgnore
	@ExcelField(title="每天每耶订单号", type=1, align=2, sort=10)
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@JsonIgnore
	@ExcelField(title="支付金额", type=1, align=2, sort=150)
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	@JsonIgnore
	@ExcelField(title="收件人名称", type=1, align=2, sort=160)
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	@JsonIgnore
	@ExcelField(title="收件人电话", type=1, align=2, sort=170)
	public String getMobile1() {
		return mobile1;
	}
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	@JsonIgnore
	@ExcelField(title="收件人地址", type=1, align=2, sort=180)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@JsonIgnore
	@ExcelField(title="市场", type=1, align=2, sort=190)
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	@JsonIgnore
	@ExcelField(title="下单时间", type=1, align=2, sort=200)
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@JsonIgnore
	@ExcelField(title="物流订单号", type=1, align=2, sort=210)
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	@JsonIgnore
	@ExcelField(title="物流名称", type=1, align=2, sort=220)
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	@JsonIgnore
	@ExcelField(title="发货时间", type=1, align=2, sort=230)
	public String getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(String shippingTime) {
		this.shippingTime = shippingTime;
	}
	@JsonIgnore
	@ExcelField(title="备注", type=1, align=2, sort=240)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@JsonIgnore
	@ExcelField(title="用户ID", type=1, align=2, sort=60)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@JsonIgnore
	@ExcelField(title="昵称", type=1, align=2, sort=70)
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@JsonIgnore
	@ExcelField(title="手机号码", type=1, align=2, sort=80)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@JsonIgnore
	@ExcelField(title="商品ID", type=1, align=2, sort=90)
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	@JsonIgnore
	@ExcelField(title="货号", type=1, align=2, sort=100)
	public String getGoodsSn() {
		return goodsSn;
	}
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	@JsonIgnore
	@ExcelField(title="购买数量", type=1, align=2, sort=110)
	public String getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}
	@JsonIgnore
	@ExcelField(title="商品名称", type=1, align=2, sort=120)
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	@JsonIgnore
	@ExcelField(title="商品规格", type=1, align=2, sort=130)
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	@JsonIgnore
	@ExcelField(title="商品原价", type=1, align=2, sort=140)
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	@JsonIgnore
	@ExcelField(title="商品编码", type=1, align=2, sort=134)
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	@JsonIgnore
	@ExcelField(title="商品条形码", type=1, align=2, sort=138)
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	
}
