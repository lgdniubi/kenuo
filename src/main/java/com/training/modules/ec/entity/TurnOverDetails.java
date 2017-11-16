package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 营业额明细表实体类
 * @author xiaoye   2017年11月16日
 * 
 */
public class TurnOverDetails extends DataEntity<TurnOverDetails> {

	private static final long serialVersionUID = 1L;
	
	private int turnOverDetailsId;          //主键ID
	private String orderId;                //订单ID
	private int mappingId;               //订单商品中间表ID
	private String detailsId;            //详情ID(detail_id/returned_id)
	private int type;                       //类型(1:下单，2:充值/还欠款，3:退款，4:账户充值)
	private double amount;                 //金额(区分正负)
	private double useBalance;               //使用账户余额
	private int status;                        //状态(0:正常下单，1:预约金，2:处理预约金，3:充值，4:退款)
	private int userId;                    //消费者ID
	
	private String userOfficeId;              //消费者绑定店铺
	private String userBeauticianId;           //绑定技师
	private String belongOfficeId;               //归属店铺
	private String belongOfficeIds;           //归属店铺所有父类
	
	public int getTurnOverDetailsId() {
		return turnOverDetailsId;
	}
	public void setTurnOverDetailsId(int turnOverDetailsId) {
		this.turnOverDetailsId = turnOverDetailsId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getMappingId() {
		return mappingId;
	}
	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}
	public String getDetailsId() {
		return detailsId;
	}
	public void setDetailsId(String detailsId) {
		this.detailsId = detailsId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getUseBalance() {
		return useBalance;
	}
	public void setUseBalance(double useBalance) {
		this.useBalance = useBalance;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserOfficeId() {
		return userOfficeId;
	}
	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}
	public String getUserBeauticianId() {
		return userBeauticianId;
	}
	public void setUserBeauticianId(String userBeauticianId) {
		this.userBeauticianId = userBeauticianId;
	}
	public String getBelongOfficeId() {
		return belongOfficeId;
	}
	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}
	public String getBelongOfficeIds() {
		return belongOfficeIds;
	}
	public void setBelongOfficeIds(String belongOfficeIds) {
		this.belongOfficeIds = belongOfficeIds;
	}
	
	
	
}
