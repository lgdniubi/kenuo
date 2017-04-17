package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 直播打赏实体类
 * @author xiaoye  2017年3月15日
 *
 */
public class TrainLiveRewardRecord extends DataEntity<TrainLiveRewardRecord>{
	
	private static final long serialVersionUID = 1L;
	
	private int trainLiveRewardRecordId;  //直播打赏id
	private String auditId;               //直播ID
	private String receiveUserId;         //收礼人ID
	private String sendUserId;            //送礼人ID
	private int giftId;                   //礼物配置ID
	private int integrals;                //云币
	
	private String name;                  //打赏人姓名
	private int ranking;                  //打赏人排名
	private String phone;                 //打赏人手机号
	private	String position;              //打赏人职位
	private String organization;          //打赏人归属机构
	private int num;                      //贡献云币数量
	
	public int getTrainLiveRewardRecordId() {
		return trainLiveRewardRecordId;
	}
	public void setTrainLiveRewardRecordId(int trainLiveRewardRecordId) {
		this.trainLiveRewardRecordId = trainLiveRewardRecordId;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public int getGiftId() {
		return giftId;
	}
	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}
	public int getIntegrals() {
		return integrals;
	}
	public void setIntegrals(int integrals) {
		this.integrals = integrals;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
}
