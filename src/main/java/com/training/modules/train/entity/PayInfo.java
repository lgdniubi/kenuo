package com.training.modules.train.entity;

/**
 * 支付信息
 * @author QJL
 *
 */
public class PayInfo {
	private static final long serialVersionUID = 7819954679744577038L;
	
	private int pay_id; //主键
	private String office_id; //机构id
	private String pay_name; //名称（银行名、支付宝、微信）
	private String pay_type; //类型（0:线上，1：线下）
	private String pay_account; //账号（银行卡号、微信号、支付宝号）
	private String pay_username; //账户用户名
	private String pay_mobile; //手机号
	private String pay_fonturl; //卡号前照
	private String pay_backurl; //卡号反照
	private String create_user; //操作人
	
	
	public int getPay_id() {
		return pay_id;
	}
	public void setPay_id(int pay_id) {
		this.pay_id = pay_id;
	}
	public String getPay_name() {
		return pay_name;
	}
	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getPay_account() {
		return pay_account;
	}
	public void setPay_account(String pay_account) {
		this.pay_account = pay_account;
	}
	public String getPay_username() {
		return pay_username;
	}
	public void setPay_username(String pay_username) {
		this.pay_username = pay_username;
	}
	public String getPay_mobile() {
		return pay_mobile;
	}
	public void setPay_mobile(String pay_mobile) {
		this.pay_mobile = pay_mobile;
	}
	public String getPay_fonturl() {
		return pay_fonturl;
	}
	public void setPay_fonturl(String pay_fonturl) {
		this.pay_fonturl = pay_fonturl;
	}
	public String getPay_backurl() {
		return pay_backurl;
	}
	public void setPay_backurl(String pay_backurl) {
		this.pay_backurl = pay_backurl;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
}
