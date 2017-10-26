package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;
/**
 * 用户账户充值记录表
 * @author 土豆	2017-10-17
 *
 */
public class UserAccountsLog extends TreeEntity<UserAccountsLog> {
	
	private static final long serialVersionUID = 1L;
	
	private int mtmyUserId;				//充值账户ID(每天美耶用户ID)
	private double amount;				//金额(支持负数)
	private int type;					//类型(0:订单,1:账户充值)
	private int status;					//状态(0:收入,1:支出)
	private String channelFlag;			//渠道标示(wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理)
	private String sourceFlag;			//来源渠道(mtmy/fzx)
	private String remarks;				//备注
	private String createOfficeId;		//操作者当前所属机构

	private String belongUserId;		//操作者当前所属机构
	private String belongOfficeId;		//操作者当前所属机构
	
	
	public int getMtmyUserId() {
		return mtmyUserId;
	}
	public void setMtmyUserId(int mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		if(channelFlag == null || "".equals(channelFlag)){
			this.channelFlag="bm";
		}else{
			this.channelFlag=channelFlag;
		}
	}
	public String getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateOfficeId() {
		return createOfficeId;
	}
	public void setCreateOfficeId(String createOfficeId) {
		this.createOfficeId = createOfficeId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getBelongUserId() {
		return belongUserId;
	}
	public void setBelongUserId(String belongUserId) {
		this.belongUserId = belongUserId;
	}
	public String getBelongOfficeId() {
		return belongOfficeId;
	}
	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}
	@Override
	public UserAccountsLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(UserAccountsLog parent) {
		// TODO Auto-generated method stub
		
	}
	
}
