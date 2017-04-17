package com.training.modules.crm.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;


/**
 * @author Administrator
 *
 */
public class Complain extends DataEntity<Complain>{
	private static final long serialVersionUID = 1L;
	private String theme;					//主题
	private Integer questionType;			//问题分类
	private String brandType;				//品牌类型
	private String nickName;                //客户昵称
	private String name;					//客户姓名
	private Integer member;					//是否会员
	private Date creatDate;				    //记录日期
	private String recordTime;				//花费时间
	private Integer degree;					//紧急程度
	private String creatBy;					//记录人ID
	private String handler;					//处理人
	private String handlerID;				//处理人工号
	private Integer status;					//问题状态(已/未处理)
	private Integer changeTimes;			//转交次数
	private String mobile;					//投诉号码
	private String callBack;				//回电号码
	private String consumeShop;				//消费门店
	private String content;					//问题描述
	private Integer solveId;				//处理过程表id
	private Integer questionSource;			//问题来源
	private Integer questionDegree;			//紧急程度
	private Integer handResult; 			//处理结果
	private Date cretDate;				    //处理时间
	private Integer solveTimes;				//处理次数
	private String solveContent;			//处理方案
	private String redirectUserId;			//转接对象
	private String remarks;					//备注
	private String officeId;				//officeId
	private String officeName;				//officeName
	private String stamp;                   //标记
	private String userId;                  //用户Id
	private String tab;                     //标记     
	private String tamb;      
	
	
	public String getTamb() {
		return tamb;
	}
	public void setTamb(String tamb) {
		this.tamb = tamb;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public Integer getSolveId() {
		return solveId;
	}
	public void setSolveId(Integer solveId) {
		this.solveId = solveId;
	}
	public Integer getSolveTimes() {
		return solveTimes;
	}
	public void setSolveTimes(Integer solveTimes) {
		this.solveTimes = solveTimes;
	}
	public Integer getQuestionSource() {
		return questionSource;
	}
	public void setQuestionSource(Integer questionSource) {
		this.questionSource = questionSource;
	}
	public String getSolveContent() {
		return solveContent;
	}
	public void setSolveContent(String solveContent) {
		this.solveContent = solveContent;
	}
	public String getConsumeShop() {
		return consumeShop;
	}
	public void setConsumeShop(String consumeShop) {
		this.consumeShop = consumeShop;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public Integer getQuestionType() {
		return questionType;
	}
	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}
	public String getBrandType() {
		return brandType;
	}
	public void setBrandType(String brandType) {
		this.brandType = brandType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMember() {
		return member;
	}
	public void setMember(Integer member) {
		this.member = member;
	}
	public Date getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public Integer getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	public String getCreatBy() {
		return creatBy;
	}
	public void setCreatBy(String creatBy) {
		this.creatBy = creatBy;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getChangeTimes() {
		return changeTimes;
	}
	public void setChangeTimes(Integer changeTimes) {
		this.changeTimes = changeTimes;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHandlerID() {
		return handlerID;
	}
	public void setHandlerID(String handlerID) {
		this.handlerID = handlerID;
	}
	public Integer getQuestionDegree() {
		return questionDegree;
	}
	public void setQuestionDegree(Integer questionDegree) {
		this.questionDegree = questionDegree;
	}
	public Integer getHandResult() {
		return handResult;
	}
	public void setHandResult(Integer handResult) {
		this.handResult = handResult;
	}

	public Date getCretDate() {
		return cretDate;
	}
	public void setCretDate(Date cretDate) {
		this.cretDate = cretDate;
	}
	public String getRedirectUserId() {
		return redirectUserId;
	}
	public void setRedirectUserId(String redirectUserId) {
		this.redirectUserId = redirectUserId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStamp() {
		return stamp;
	}
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	
}