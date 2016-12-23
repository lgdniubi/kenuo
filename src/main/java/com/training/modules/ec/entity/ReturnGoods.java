package com.training.modules.ec.entity;

import java.util.Date;


import com.training.common.persistence.DataEntity;

public class ReturnGoods extends DataEntity<ReturnGoods>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int returngoodsid;		//退货申请表id自增
	private String orderid;			//订单id
	private String ordersn;			//订单编号
	private int goodsid;			//商品id
	private int type;				//退货1换货
	private String reason;			//退换货原因
	private String imgs;			//拍照图片路径
	private Date addtime;			//申请时间
	private int returnstatus;		//退款状态
	private String remark;			//客服备注
	private int userid;			//用户id
	private String parentid;		//父id
	private double returnmoney;  	//退还金额
	private int storagestatus;		//入库状态 
	private String retusername;		//退款姓名
	private String bankname;		//银行名称
	private String bankno;			//银行卡号
	private String entryoperator;	//录入退货人
	private String	financialoperator;		//退款财务人员
	
	
	private String username;		//用户名
	private int orderstatus;		//订单状态
	private double orderamount;	//支付金额
	private String mobile;			//手机号
	private String keyword;			//关键字
	private Date begtime;			//查询开始时间
	private Date endtime;			//查询结束时间
	
	public int getReturngoodsid() {
		return returngoodsid;
	}
	public void setReturngoodsid(int returngoodsid) {
		this.returngoodsid = returngoodsid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getImgs() {
		return imgs;
	}
	public void setImgs(String imgs) {
		this.imgs = imgs;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getReturnstatus() {
		return returnstatus;
	}
	public void setReturnstatus(int returnstatus) {
		this.returnstatus = returnstatus;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public double getReturnmoney() {
		return returnmoney;
	}
	public void setReturnmoney(Double returnmoney) {
		this.returnmoney = returnmoney;
	}

	public String getRetusername() {
		return retusername;
	}
	public void setRetusername(String retusername) {
		this.retusername = retusername;
	}
	public int getStoragestatus() {
		return storagestatus;
	}
	public void setStoragestatus(int storagestatus) {
		this.storagestatus = storagestatus;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}
	public double getOrderamount() {
		return orderamount;
	}
	public void setOrderamount(double orderamount) {
		this.orderamount = orderamount;
	}
	public String getEntryoperator() {
		return entryoperator;
	}
	public void setEntryoperator(String entryoperator) {
		this.entryoperator = entryoperator;
	}
	public String getFinancial_operator() {
		return financialoperator;
	}
	public void setFinancial_operator(String financialoperator) {
		this.financialoperator = financialoperator;
	}
	public void setReturnmoney(double returnmoney) {
		this.returnmoney = returnmoney;
	}
	public String getFinancialoperator() {
		return financialoperator;
	}
	public void setFinancialoperator(String financialoperator) {
		this.financialoperator = financialoperator;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	
	

}
