package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 支付方式实体
 * @author yangyang
 *
 */
public class Payment extends TreeEntity<Payment> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int payid;			//表id	
	private String paycode;		//支付code	
	private String payname	;		//支付方式名称
	private String payfee;			//手续费
	private String paydesc	;		//描述
	private int payorder;			//pay_coder
	private String payconfig;		//配置
	private int enabled;	   		 //开启
	private int iscod;    		//是否货到付款
	private int isonline;			//是否在线支付
	
	
	
	
	public int getPayid() {
		return payid;
	}
	public void setPayid(int payid) {
		this.payid = payid;
	}
	public String getPaycode() {
		return paycode;
	}
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	public String getPayname() {
		return payname;
	}
	public void setPayname(String payname) {
		this.payname = payname;
	}
	public String getPayfee() {
		return payfee;
	}
	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}
	public String getPaydesc() {
		return paydesc;
	}
	public void setPaydesc(String paydesc) {
		this.paydesc = paydesc;
	}
	public int getPayorder() {
		return payorder;
	}
	public void setPayorder(int payorder) {
		this.payorder = payorder;
	}
	public String getPayconfig() {
		return payconfig;
	}
	public void setPayconfig(String payconfig) {
		this.payconfig = payconfig;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public int getIscod() {
		return iscod;
	}
	public void setIscod(int iscod) {
		this.iscod = iscod;
	}
	public int getIsonline() {
		return isonline;
	}
	public void setIsonline(int isonline) {
		this.isonline = isonline;
	}
	@Override
	public Payment getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(Payment parent) {
		// TODO Auto-generated method stub
		
	}
	
	


}
