package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 直播订单实体类
 * @author 小叶   2017年2月22日
 *
 */
public class TrainLiveOrder extends DataEntity<TrainLiveOrder>{

	private static final long serialVersionUID = 1L;

	private String trainLiveOrderId;   //直播订单id
	private int auditId;          //直播申请ID
	private String userId;         //用户ID（妃子校用户）
	private int specId;            //直播规格ID
	private double specPrice;      //规格价格
	private int specNum;            //规格有效时间（天数）
	private int type;              //规格类型（1：直播；2：回放）
	private Date payDate;           //支付时间
	private Date validDate;          //有效期时间
	private int orderStatus;         //订单状态（1：取消订单；2：待支付；3：已付款；4：已退款；）
	private int delflag;             // 删除标识（0：正常；1：成功）
	public String getTrainLiveOrderId() {
		return trainLiveOrderId;
	}
	public void setTrainLiveOrderId(String trainLiveOrderId) {
		this.trainLiveOrderId = trainLiveOrderId;
	}
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSpecId() {
		return specId;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
	public double getSpecPrice() {
		return specPrice;
	}
	public void setSpecPrice(double specPrice) {
		this.specPrice = specPrice;
	}
	public int getSpecNum() {
		return specNum;
	}
	public void setSpecNum(int specNum) {
		this.specNum = specNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	
	
}
