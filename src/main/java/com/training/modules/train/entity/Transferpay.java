package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
/**
 * 账单支付信息
 * @author QJL
 *
 */
public class Transferpay extends DataEntity<Transferpay> {
	private static final long serialVersionUID = 1L;
	
	private int payId;
	private String serialnumber; //流水号
	private String bankaccount; //银行账号
	private String openbank; //开户银行
	private String office_id; //还款机构
	private String order_id; //订单id
	private String repaymonth; //还款月份
	private String user_id; //用户id
	private String proof; //凭证
	private int status; //0：待审核，1：审核失败，2：审核通过
	private String explain; //说明
	private String openname; //开户人名称
	private String create_time;
	private String user_name;
	
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
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
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getRepaymonth() {
		return repaymonth;
	}
	public void setRepaymonth(String repaymonth) {
		this.repaymonth = repaymonth;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getOpenname() {
		return openname;
	}
	public void setOpenname(String openname) {
		this.openname = openname;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
