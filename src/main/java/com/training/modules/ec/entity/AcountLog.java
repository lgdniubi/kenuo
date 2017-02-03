package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 操作日志实体
 * @author water
 *
 */
public class AcountLog extends TreeEntity<AcountLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int logid;				//日志id				
	private int userid;			//用户id				
	private double usermoney;		//用户金额				
	private double frozenmoney; 	//冻结金额
	private int paypoints;			//支付积分
	private Date changetime;		//变动时间
	private String logdesc; 		//描述
	private String ordersn	;		//订单编号
	private String orderid;			//订单id
	private String operator;		//操作人
	
	
	
	public int getLogid() {
		return logid;
	}
	public void setLogid(int logid) {
		this.logid = logid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public double getUsermoney() {
		return usermoney;
	}
	public void setUsermoney(double usermoney) {
		this.usermoney = usermoney;
	}
	public double getFrozenmoney() {
		return frozenmoney;
	}
	public void setFrozenmoney(double frozenmoney) {
		this.frozenmoney = frozenmoney;
	}
	public int getPaypoints() {
		return paypoints;
	}
	public void setPaypoints(int paypoints) {
		this.paypoints = paypoints;
	}
	public Date getChangetime() {
		return changetime;
	}
	public void setChange_time(Date changetime) {
		this.changetime = changetime;
	}
	public String getLogdesc() {
		return logdesc;
	}
	public void setLogdesc(String logdesc) {
		this.logdesc = logdesc;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@Override
	public AcountLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(AcountLog parent) {
		// TODO Auto-generated method stub
		
	}

	

}
