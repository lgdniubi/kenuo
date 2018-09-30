package com.training.modules.train.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.common.persistence.DataEntity;

/**
 * 用户审核
 * @author 
 * @version 2018年3月26日
 */
public class ModelFranchisee extends DataEntity<ModelFranchisee>{
	
	private static final long serialVersionUID = 1L;
	private String modid;		//模板id
	private String franchiseeid;	//商家id,当商家id为0时，为手艺人
	private String userid;		//用户id，当用户id为0时，为企业版本
	private int userRoleId;		//插入手艺人用户角色时返回的id
	private String applyid;		//用户申请id
	private Date authStartDate;		//授权开始时间
	private Date authEndDate;		//授权结束时间
	private String discount;		//折扣
	private String paytype;		//支付类型（0：线上支付，1：线下支付）
//	private String remarks;		//备注
	private String status;		//过期状态（0：未过期，1：已过期）
	
	private Integer groupCount ;		//用户可创建群组数量
	private Integer groupUserCount;		//用户可创建的群组成员数值
	private Integer memberCount ;		//用户的专属会员数量
	
	public ModelFranchisee() {
		super();
	}
	public String getModid() {
		return modid;
	}
	public void setModid(String modid) {
		this.modid = modid;
	}
	public String getFranchiseeid() {
		return franchiseeid;
	}
	public void setFranchiseeid(String franchiseeid) {
		this.franchiseeid = franchiseeid;
	}
	public String getUserid() {
		return userid;
	}
	public String getApplyid() {
		return applyid;
	}
	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuthStartDate() {
		return authStartDate;
	}
	public void setAuthStartDate(Date authStartDate) {
		this.authStartDate = authStartDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuthEndDate() {
		return authEndDate;
	}
	public void setAuthEndDate(Date authEndDate) {
		this.authEndDate = authEndDate;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getStatus() {
		return status;
	}
	public int getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getGroupCount() {
		return getValue(groupCount, 50,0, 100, 1000, modid);
	}
	/**
	 * 
	 * @param defVal	默认值-数据库查的
	 * @param commonC	标准版
	 * @param topC	高级版
	 * @param superC	旗舰版
	 * @param modid
	 * @return
	 */
	private Integer getValue(Integer count,Integer defVal,Integer commonC,Integer topC,Integer superC,String modid) {
		Integer value = count;
		if(modid == null){modid = "5";}
		if(count==null){
			switch (modid) {
			case "5":
				value = commonC;
				break;
			case "6":
				value = topC;
				break;
			case "7":
				value=superC;
				break;
			default:
				value = defVal;
				break;
			}
		}
		return value;
	}
	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}
	public Integer getGroupUserCount() {
		return getValue(groupUserCount, 0,50, 50, 300, modid);
	}
	public void setGroupUserCount(Integer groupUserCount) {
		this.groupUserCount = groupUserCount;
	}
	public Integer getMemberCount() {
		return getValue(memberCount,0, 0, 100, 50000, modid);
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	@Override
	public String toString() {
		return "ModelFranchisee [modid=" + modid + ", franchiseeid=" + franchiseeid + ", userid=" + userid
				+ ", authStartDate=" + authStartDate + ", authEndDate=" + authEndDate + ", discount=" + discount
				+ ", paytype=" + paytype + ", remarks=" + remarks + ", status=" + status + "]";
	}
	
	
}
