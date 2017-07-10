package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.modules.sys.entity.Office;

/**
 * 直播entity
 * @author yangyang
 *
 */
public class TrainLiveAudit extends TreeEntity<TrainLiveAudit> {
	
	private static final long serialVersionUID = 1L;
	private String userId;				//妃子校用户id
	private String title;				//直播标题
	private String name;				//直播房间名称
	private String desc;				//直播间描述
	private Date bengTime;				//开始时间
	private Date endTime;				//结束时间
	private int templatetype;			//直播模板类 1模板一 视频直播2模板二 视频直播+聊天互动+直播问答 3模板三 视频直播+聊天互动
										//4 模板四 视频直播+聊天互动+直播文档5模板五 视频直播+聊天互动+直播文档+直播问答6模板六 视频直播+直播问答
	private int authtype;				//验证方式，0：接口验证，需要填写下面的checkurl；1：密码验证，需要填写下面的playpass；2：免密码验证
	private String publisherpass;		//推流端密码，即讲师密码
	private String playpass;			//播放密码
	private String checkurl;			//验证地址
	private int barrage;				//是否开启弹幕。0：不开启；1：开启	
	private int foreignpublish;		//是否开启第三方推流。0：不开启；1：开启
	private int openlowdelaymode;		//开启直播低延时模式。0为关闭；1为开启  可选，默认为关闭，若对直播实时性要求不高可以关闭该模式。注意：直播间模板4、5不可以关闭该模式
	private int showusercount;			//在页面显示当前在线人数。0表示不显示；1表示显示
	private String auditStatus;			//审核状态 0 审核失败 1 请求审核 2 适合通过  3 已完成
	private Date createDate;			//申请时间
	private String auditUser;			//审核人
	private String userName;			//申请人姓名
	private String remarks;				//申请人描述
	private String imgurl;				//图片路径
	private String oldStatus;				//原来的状态
	private String roomId;				//直播id
	private String liveRemarks;			//直播备注  审核失败的时候必须给说明
	private int isPay;					//是否付费
	
	private String mobile;             //申请人手机号
	private Office organization;       //申请人归属机构
	private String position;           //申请人职位
	
	private double earningsRatio;       //平台云币收益比例
	
	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getBengTime() {
		return bengTime;
	}

	public void setBengTime(Date bengTime) {
		this.bengTime = bengTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getTemplatetype() {
		return templatetype;
	}

	public void setTemplatetype(int templatetype) {
		this.templatetype = templatetype;
	}

	public int getAuthtype() {
		return authtype;
	}

	public void setAuthtype(int authtype) {
		this.authtype = authtype;
	}

	public String getPublisherpass() {
		return publisherpass;
	}

	public void setPublisherpass(String publisherpass) {
		this.publisherpass = publisherpass;
	}

	public String getPlaypass() {
		return playpass;
	}

	public void setPlaypass(String playpass) {
		this.playpass = playpass;
	}

	public String getCheckurl() {
		return checkurl;
	}

	public void setCheckurl(String checkurl) {
		this.checkurl = checkurl;
	}

	public int getBarrage() {
		return barrage;
	}

	public void setBarrage(int barrage) {
		this.barrage = barrage;
	}

	public int getForeignpublish() {
		return foreignpublish;
	}

	public void setForeignpublish(int foreignpublish) {
		this.foreignpublish = foreignpublish;
	}

	public int getOpenlowdelaymode() {
		return openlowdelaymode;
	}

	public void setOpenlowdelaymode(int openlowdelaymode) {
		this.openlowdelaymode = openlowdelaymode;
	}

	public int getShowusercount() {
		return showusercount;
	}

	public void setShowusercount(int showusercount) {
		this.showusercount = showusercount;
	}

	
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getLiveRemarks() {
		return liveRemarks;
	}

	public void setLiveRemarks(String liveRemarks) {
		this.liveRemarks = liveRemarks;
	}

	@Override
	public TrainLiveAudit getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(TrainLiveAudit parent) {
		// TODO Auto-generated method stub
		
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Office getOrganization() {
		return organization;
	}

	public void setOrganization(Office organization) {
		this.organization = organization;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getEarningsRatio() {
		return earningsRatio;
	}

	public void setEarningsRatio(double earningsRatio) {
		this.earningsRatio = earningsRatio;
	}

}
