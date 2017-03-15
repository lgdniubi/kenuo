package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;

/**
 * 云币充值记录以及佣金兑换云币记录实体类
 * @author xiaoye  2017年3月15日
 *
 */
public class TrainLiveRecord extends DataEntity<TrainLiveRecord>{

	private static final long serialVersionUID = 1L;
	
	private String orderId;        //订单id
	private String userId;         //用户id
	private int type;              //1：直播；2：回放；3：购买云币；4：兑换云币
	private double specPrice;     //人民币或者佣金
	private int integrals;         //云币
	private Date payDate;          //充值云币的支付时间
	private Date createDate;       //佣金兑换云币的兑换时间
	private String payCode;        //支付方式
	
	private String name;           //充值人或者兑换人的姓名
	private String phone;          //充值人或者兑换人手机号
	private Office organization;   //充值人或者兑换人归属机构
	
	private Date beginTime;       //开始时间
	private Date endTime;         //结束时间
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getSpecPrice() {
		return specPrice;
	}
	public void setSpecPrice(double specPrice) {
		this.specPrice = specPrice;
	}
	public int getIntegrals() {
		return integrals;
	}
	public void setIntegrals(int integrals) {
		this.integrals = integrals;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Office getOrganization() {
		return organization;
	}
	public void setOrganization(Office organization) {
		this.organization = organization;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
