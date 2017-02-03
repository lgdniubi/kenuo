package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 操作日志实体
 * @author water
 *
 */
public  class OrdersLog extends TreeEntity<OrdersLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int olid;
	private String orderid	;
	private int userid;
	private String operator	;
	private String paycode	;
	private String payname;
	private Date addtime;
	private String ipaddress;
	private String temporderid;


	public int getOlid() {
		return olid;
	}
	public void setOlid(int olid) {
		this.olid = olid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getTemporderid() {
		return temporderid;
	}
	public void setTemporderid(String temporderid) {
		this.temporderid = temporderid;
	}
	
	@Override
	public OrdersLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrdersLog parent) {
		// TODO Auto-generated method stub
		
	}


	

}
