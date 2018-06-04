/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 反馈管理Entity
 * 
 * @version 2013-12-05
 */
/**
 * @author xiaoye
 *
 */
public class MtmyFeedback extends DataEntity<MtmyFeedback> {

	private static final long serialVersionUID = 1L;
	private String msgId;
	private String userId;			//反馈用户ID
	private String userName;		//反馈用户姓名
	private String msgContent;		//反馈内容
	private Date msgTime;			//反馈时间	
	private int msgStatus;			//是否解决		0 未解决  1  解决
	private String messageImg;		//反馈照片
	private Users users;			//反馈用户	
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	private String mobile;       //用户手机号
	private String msgType;            //反馈类型（0：购物商品；1：功能异常；2：新功能建议:；3：门店体验；4：其他）
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public Date getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}
	public int getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(int msgStatus) {
		this.msgStatus = msgStatus;
	}
	public String getMessageImg() {
		return messageImg;
	}
	public void setMessageImg(String messageImg) {
		this.messageImg = messageImg;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
}