package com.training.modules.ec.entity;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 虚拟订单导入的实体类
 * @author xiaoye    2017年4月11日
 * 
 */
public class ImportVirtualOrders {
	
	private String distinction;   //订单性质（0：电商；1：售前卖；2：售后卖；3：老带新）
	private String firstType;    //一级分类
	private String senondType;    //二级分类
	private String goodsChoose;   //商品选择
	private String goodsId;      //商品id
	private String specChoose;    //规格选择
	private String specKey;       //商品规格id
	private String mobile;        //手机号码
	private String name;          //姓名
	private String actualTimes;    //实际次数
	private String payCode;        //支付方式
	private String note;           //留言备注
	private String checkout;       //预留校验
	
	@JsonIgnore
	@NotNull(message="订单性质不能为空")
	@ExcelField(title="订单性质", align=2, sort=5)
	public String getDistinction() {
		return distinction;
	}
	public void setDistinction(String distinction) {
		this.distinction = distinction;
	}
	
	@JsonIgnore
	@ExcelField(title="一级分类", align=2, sort=10)
	public String getFirstType() {
		return firstType;
	}
	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}
	
	@JsonIgnore
	@ExcelField(title="二级分类", align=2, sort=15)
	public String getSenondType() {
		return senondType;
	}
	public void setSenondType(String senondType) {
		this.senondType = senondType;
	}
	
	@JsonIgnore
	@ExcelField(title="商品选择", align=2, sort=20)
	public String getGoodsChoose() {
		return goodsChoose;
	}
	public void setGoodsChoose(String goodsChoose) {
		this.goodsChoose = goodsChoose;
	}
	
	@JsonIgnore
	@NotNull(message="商品id不能为空")
	@ExcelField(title="商品id", align=2, sort=25)
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
	@JsonIgnore
	@ExcelField(title="规格选择", align=2, sort=30)
	public String getSpecChoose() {
		return specChoose;
	}
	public void setSpecChoose(String specChoose) {
		this.specChoose = specChoose;
	}
	
	@JsonIgnore
	@NotNull(message="规格id不能为空")
	@ExcelField(title="规格id", align=2, sort=35)
	public String getSpecKey() {
		return specKey;
	}
	public void setSpecKey(String specKey) {
		this.specKey = specKey;
	}
	
	@JsonIgnore
	@NotNull(message="手机号码不能为空")
	@ExcelField(title="手机号码", align=2, sort=40)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@JsonIgnore
	@ExcelField(title="姓名或昵称", align=2, sort=45)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@NotNull(message="实际次数不能为空")
	@ExcelField(title="实际次数", align=2, sort=50)
	public String getActualTimes() {
		return actualTimes;
	}
	public void setActualTimes(String actualTimes) {
		this.actualTimes = actualTimes;
	}
	
	@JsonIgnore
	@NotNull(message="付款方式不能为空")
	@ExcelField(title="付款方式", align=2, sort=55)
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	
	@JsonIgnore
	@ExcelField(title="留言备注", align=2, sort=60)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@JsonIgnore
	@ExcelField(title="预校验", align=2, sort=65)
	public String getCheckout() {
		return checkout;
	}
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	
	
}
