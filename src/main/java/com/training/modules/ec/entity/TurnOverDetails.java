package com.training.modules.ec.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	private String userOfficeId;              //消费者绑定店铺ID
	private String userBeauticianId;           //绑定技师ID
	private String belongOfficeId;               //归属店铺ID
	private String belongOfficeName;            //归属店铺名称
	private String belongOfficeIds;           //归属店铺所有父类ids
	
	private String settleBy;                    //结算人id
	private String settleName;                   //结算人名称
	private Date settleDate;                     //结算时间
	public List<OrderPushmoneyRecord> pushMoneyList;   //点营业额对应的业务员营业额列表
	
	//----------------售后审核编辑店营业额---------------
	private String amounts;			 	//获取编辑营业额的增减值字符串	2017-11-16   土豆添加
	private double returnAmount;		//金额(区分正负)
	private String userName;			//消费者
	private String officeName;			//归属店铺名称
	private String beauticianName;		//绑定技师
	private String createName;			//操作人
	private Date applyDate;             //申请时间
	
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
	public String getSettleBy() {
		return settleBy;
	}
	public void setSettleBy(String settleBy) {
		this.settleBy = settleBy;
	}
	public Date getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}
	public String getBelongOfficeName() {
		return belongOfficeName;
	}
	public void setBelongOfficeName(String belongOfficeName) {
		this.belongOfficeName = belongOfficeName;
	}
	public String getSettleName() {
		return settleName;
	}
	public void setSettleName(String settleName) {
		this.settleName = settleName;
	}
	public List<OrderPushmoneyRecord> getPushMoneyList() {
		return pushMoneyList;
	}
	public void setPushMoneyList(List<OrderPushmoneyRecord> pushMoneyList) {
		if(pushMoneyList == null){
			this.pushMoneyList = new ArrayList<OrderPushmoneyRecord>();
		}else{
			this.pushMoneyList = pushMoneyList;
		}
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
	public double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getBeauticianName() {
		return beauticianName;
	}
	public void setBeauticianName(String beauticianName) {
		this.beauticianName = beauticianName;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
}
