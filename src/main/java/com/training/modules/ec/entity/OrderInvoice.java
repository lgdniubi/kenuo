package com.training.modules.ec.entity;


import java.util.Date;

import com.training.common.persistence.TreeEntity;
/**
 *订单发票实体
 * @author zhangyang
 *
 */
public class OrderInvoice extends TreeEntity<OrderInvoice> {

	private static final long serialVersionUID = 1L;

	private int userId;						//用户id
	private int invoiceType;					//发票类型（1：个人普票；2：公司普通；3：公司专票）
	//private int headType;						//发票抬头类型 （1：个人；2：公司）
	private String headContent;					//抬头内容
	private String taxNum;						//税号
	private String accountHolder;				//开户人
	private String bankName;					//开户银行名称
	private String bankNo;						//开户银行卡号
	private String phone;						//联系电话
	private String address;						//地址
	private double invoiceAmount;				//发票总金额
	private String invoiceContent;				//发票美容
	private String recipientsName;				//收件人
	private String recipientsPhone;				//收件联系电话
	private String recipientsAddress;			//收件人地址
	private String shippingCode;				//物流运单号
	private Date shippingTime;					//发货时间
	private String shippingName;				//物流名称
	private int invoiceStatus;					//发票状态（0：已填写；1：已发出）
//	private String createBy;					//创建人
	private Date createDate;					//创建时间
//	private String updateBy;					//更新者
	private Date updateDate;					//更新日期
	private String remarks;						//备注
	private String userName;					//用户名
	private String mobile;						//手机号
	private String orderId;

	

	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getHeadContent() {
		return headContent;
	}
	public void setHeadContent(String headContent) {
		this.headContent = headContent;
	}
	public String getTaxNum() {
		return taxNum;
	}
	public void setTaxNum(String taxNum) {
		this.taxNum = taxNum;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceContent() {
		return invoiceContent;
	}
	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}
	public String getRecipientsName() {
		return recipientsName;
	}
	public void setRecipientsName(String recipientsName) {
		this.recipientsName = recipientsName;
	}
	public String getRecipientsPhone() {
		return recipientsPhone;
	}
	public void setRecipientsPhone(String recipientsPhone) {
		this.recipientsPhone = recipientsPhone;
	}
	public String getRecipientsAddress() {
		return recipientsAddress;
	}
	public void setRecipientsAddress(String recipientsAddress) {
		this.recipientsAddress = recipientsAddress;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public Date getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public int getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(int invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public OrderInvoice getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrderInvoice parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
