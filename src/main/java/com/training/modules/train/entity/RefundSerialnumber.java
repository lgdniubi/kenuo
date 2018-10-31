package com.training.modules.train.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;
/**
 * 还款流水--使用银行线下支付的时候，可以对一个账单进行多次支付
 * @author: jingfeng
 * @date 2018年9月19日上午10:55:12
 */
public class RefundSerialnumber extends DataEntity<RefundSerialnumber> {

	private static final long serialVersionUID = 10023L;
	private String orderId; //订单号
	private String serialnumber;//流水号
	private String bankaccount; //银行账号
	private String openbank; //开户行
	private String openname;//开户姓名	
	private String proof; //凭证
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public String getOpenname() {
		return openname;
	}
	public void setOpenname(String openname) {
		this.openname = openname;
	}
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	
	
}