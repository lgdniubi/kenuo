package com.training.modules.train.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;

public class RefundOrder extends DataEntity<RefundOrder> {

	private static final long serialVersionUID = 1L;
	private String orderId; //订单号
	private String tempOrderId; //临时订单号
	private String orderType;//订单类型（1：线上，转账）
	private double arrearagePrice;//欠款金额
	private double aftersalesPrice;//售后金额
	private String arrearageOffice;//欠款机构
	private String franchiseeId;//欠款商家
	private double amount;//实付金额
	private String addTime;//创建时间
	private String orderStatus;//订单状态（1：待支付；2：待审核；3：已入账）
	private String payCode;//支付类型(wx:微信，alipay:支付宝,zz:转账)
	private String payTime; //支付时间
	private String chargeId;//第三方订单号
	private String userId; //用户ID（妃子校用户）
	private String userMobile; //用户手机号码
	private String userOfficeId; //用户组织架构
	private String channelFlag; //渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	private String arrearageOfficeName; //欠款机构名
	private String userName; //用户名
	private String franchiseeName; //商家
	private String billmonth; //账单月份
	private String serialnumber;//流水号
	private String bankaccount; //银行账号
	private String openbank; //开户行
	private String proof; //凭证
	private String explains;//说明
	private String openname;//开户姓名
	private String remarks; //驳回原因
	
	private List<String> proofs;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTempOrderId() {
		return tempOrderId;
	}
	public void setTempOrderId(String tempOrderId) {
		this.tempOrderId = tempOrderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public double getArrearagePrice() {
		return arrearagePrice;
	}
	public void setArrearagePrice(double arrearagePrice) {
		this.arrearagePrice = arrearagePrice;
	}
	public double getAftersalesPrice() {
		return aftersalesPrice;
	}
	public void setAftersalesPrice(double aftersalesPrice) {
		this.aftersalesPrice = aftersalesPrice;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getUserOfficeId() {
		return userOfficeId;
	}
	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getArrearageOfficeName() {
		return arrearageOfficeName;
	}
	public void setArrearageOfficeName(String arrearageOfficeName) {
		this.arrearageOfficeName = arrearageOfficeName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	public String getBillmonth() {
		return billmonth;
	}
	public void setBillmonth(String billmonth) {
		this.billmonth = billmonth;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getOpenbank() {
		return openbank;
	}
	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	public String getExplains() {
		return explains;
	}
	public void setExplains(String explains) {
		this.explains = explains;
	}
	public String getOpenname() {
		return openname;
	}
	public void setOpenname(String openname) {
		this.openname = openname;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<String> getProofs() {
		return proofs;
	}
	public void setProofs(List<String> proofs) {
		this.proofs = proofs;
	}
}
