package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

public class RefundOrderExport extends DataEntity<RefundOrderExport> {

	private static final long serialVersionUID = 1L;
	private String orderId; //订单号
	private String arrearageOfficeName; //商家
	private double arrearagePrice;//欠款金额
	private String arrearageOffice;//欠款机构
	private String franchiseeId;//欠款商家
	private double amount;//实付金额
	private String orderStatus;//订单状态（1：待支付；2：待审核；3：已入账）
	private String userName; //支付人
	private String bankaccount; //银行账号
	private String openbank; //开户行
	private String openname; //开户人
	private String orderCode; //订单编号--下单时的生成
	private double usedLimit; //使用额度
	private String addTime;//创建时间
	private String explains; //说明
	
	private String orderType;//订单类型（1：线上，转账）
	private String payCode;//支付类型(wx:微信，alipay:支付宝,zz:转账)
	
	@ExcelField(title="还款单号", align=2, sort=10)
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@ExcelField(title="还款机构", align=2, sort=20)
	public String getArrearageOfficeName() {
		return arrearageOfficeName;
	}
	public void setArrearageOfficeName(String arrearageOfficeName) {
		this.arrearageOfficeName = arrearageOfficeName;
	}
	@ExcelField(title="还款金额", align=2, sort=30)
	public double getArrearagePrice() {
		return arrearagePrice;
	}
	public void setArrearagePrice(double arrearagePrice) {
		this.arrearagePrice = arrearagePrice;
	}
	/*public String getArrearageOffice() {
		return arrearageOffice;
	}
	public void setArrearageOffice(String arrearageOffice) {
		this.arrearageOffice = arrearageOffice;
	}*/
	@ExcelField(title="实付金额", align=2, sort=40)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	@ExcelField(title="账单状态", align=2, sort=50)
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	@ExcelField(title="支付人", align=2, sort=55)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@ExcelField(title="支付类型", align=2, sort=56)
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	@ExcelField(title="银行账号", align=2, sort=60)
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	@ExcelField(title="开户银行", align=2, sort=70)
	public String getOpenbank() {
		return openbank;
	}
	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	@ExcelField(title="持卡人姓名", align=2, sort=80)
	public String getOpenname() {
		return openname;
	}
	public void setOpenname(String openname) {
		this.openname = openname;
	}
	@ExcelField(title="订单编号", align=2, sort=90)
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	@ExcelField(title="使用额度", align=2, sort=95)
	public double getUsedLimit() {
		return usedLimit;
	}
	public void setUsedLimit(double usedLimit) {
		this.usedLimit = usedLimit;
	}
	@ExcelField(title="创建时间", align=2, sort=100)
	public String getAddTime() {
		return addTime;
	}
	@ExcelField(title="说明", align=2, sort=110)
	public String getExplains() {
		return explains;
	}
	public void setExplains(String explains) {
		this.explains = explains;
	}
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getArrearageOffice() {
		return arrearageOffice;
	}
	public void setArrearageOffice(String arrearageOffice) {
		this.arrearageOffice = arrearageOffice;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	
}