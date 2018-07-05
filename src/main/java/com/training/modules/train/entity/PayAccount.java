package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 用户认证手机端填写的支付宝和微信信息
 * @author 
 * @version 2018年3月26日
 */
public class PayAccount extends DataEntity<PayAccount>{

	private static final long serialVersionUID = -8902499398917129860L;
	
//	private int payId;
	private int applyId;	//申请记录ID	
	private String payType;	//类型 ：【1.支付宝 、2.微信】
	private String no; 		//支付账号
	private String name; 	//支付账号对应人
	private String mobile;	//支付账号对应手机号
	public int getApplyId() {
		return applyId;
	}
	public void setApplyId(int applyId) {
		this.applyId = applyId;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}