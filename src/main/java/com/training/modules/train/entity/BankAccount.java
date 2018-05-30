package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户认证手机端填写的银行卡信息
 * @author 
 * @version 2018年3月26日
 */
public class BankAccount extends DataEntity<BankAccount>{

	private static final long serialVersionUID = 7134970025537710580L;
	
//	private int bank_id;
	private int applyId;		//申请记录ID
	private String accountname; //账户名称
	private String openbank; 	//开户银行
	private String bankaccount;	//银行账号
	private String openaddress; //开户地址
	private String detailedaddress;		//详细地址
	private String cardup;		//银行卡正面照片
	private String carddown;	//银行卡反面照片
	private String bankType; 	//银行卡类型
	public int getApplyId() {
		return applyId;
	}
	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getOpenbank() {
		return openbank;
	}
	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getOpenaddress() {
		return openaddress;
	}
	public void setOpenaddress(String openaddress) {
		this.openaddress = openaddress;
	}
	public String getDetailedaddress() {
		return detailedaddress;
	}
	public void setDetailedaddress(String detailedaddress) {
		this.detailedaddress = detailedaddress;
	}
	public String getCardup() {
		return cardup;
	}
	public void setCardup(String cardup) {
		this.cardup = cardup;
	}
	public String getCarddown() {
		return carddown;
	}
	public void setCarddown(String carddown) {
		this.carddown = carddown;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
	
	
}