package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 操作日志实体
 * @author 小叶     2018年1月24日
 *
 */
public class OrdersLog extends TreeEntity<OrdersLog>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int olid;
	private String orderid;
	private int userid;
	private String operator;
	private String paycode;
	private String payname;
	private Date addtime;
	private String ipaddress;
	private String temporderid;
	
	private String title;              //操作的内容
	private String content;           //修改内容（用于界面展示）
	private String contentRecord;      //修改内容（用于记录）
	private String channelFlag;        //渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	private String platformFlag;        //平台标示（mtmy:每天美耶，fzx:妃子校）
	private String createOfficeIds;     //操作者机构IDS
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentRecord() {
		return contentRecord;
	}
	public void setContentRecord(String contentRecord) {
		this.contentRecord = contentRecord;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}
	public String getPlatformFlag() {
		return platformFlag;
	}
	public void setPlatformFlag(String platformFlag) {
		this.platformFlag = platformFlag;
	}
	public String getCreateOfficeIds() {
		return createOfficeIds;
	}
	public void setCreateOfficeIds(String createOfficeIds) {
		this.createOfficeIds = createOfficeIds;
	}
	public int getOlid() {
		return olid;
	}
	public void setOlid(int olid) {
		this.olid = olid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPaycode() {
		return paycode;
	}
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	
	public String getPayname() {
		return payname;
	}
	public void setPayname(String payname) {
		this.payname = payname;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getTemporderid() {
		return temporderid;
	}
	public void setTemporderid(String temporderid) {
		this.temporderid = temporderid;
	}
	
	@Override
	public OrdersLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrdersLog parent) {
		// TODO Auto-generated method stub
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
