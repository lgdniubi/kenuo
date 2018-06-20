package com.training.modules.track.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 类名称：	TGoods
 * 类描述：	神策埋点-商品实体类
 * 创建人： 	kele
 * 创建时间：    	2018年1月18日 下午2:17:09
 */
public class TUserAudit extends DataEntity<TUserAudit>{
	
	private static final long serialVersionUID = 1L;
	
	// 申请ID
	private Integer applyId;
	// 妃子校用户ID
	private String fzxUserId;
	// 申请类型 (pt:普通；syr：手艺人)
	private String applyType;
	// 认证类型(syr:手艺人；qy：企业)
	private String auditType;
	// 审核状态（0:待审核;1:未通过;2:已通过;3:已授权;4:不可操作;）
	private String auditStatus;
	// 认证申请时间
	private Date authentApplyDate;
	// 拒绝原因
	private String rejectRemark;
	// 企业名称
	private String franchiseeName;
	
	/**
	 * @return the applyId
	 */
	public Integer getApplyId() {
		return applyId;
	}
	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}
	/**
	 * @return the fzxUserId
	 */
	public String getFzxUserId() {
		return fzxUserId;
	}
	/**
	 * @param fzxUserId the fzxUserId to set
	 */
	public void setFzxUserId(String fzxUserId) {
		this.fzxUserId = fzxUserId;
	}
	/**
	 * @return the applyType
	 */
	public String getApplyType() {
		if("pt".equals(applyType)) {
			return "普通";
		}else if("syr".equals(applyType)) {
			return "手艺人";
		}
		return applyType;
	}
	/**
	 * @param applyType the applyType to set
	 */
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	/**
	 * @return the auditType
	 */
	public String getAuditType() {
		if("syr".equals(auditType)) {
			return "手艺人";
		}else if("qy".equals(auditType)) {
			return "企业";
		}
		return auditType;
	}
	/**
	 * @param auditType the auditType to set
	 */
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	/**
	 * @return the auditStatus
	 */
	public String getAuditStatus() {
		if("0".equals(auditStatus)) {
			return "待审核";
		}else if("1".equals(auditStatus)) {
			return "未通过";
		}else if("2".equals(auditStatus)) {
			return "已通过";
		}else if("3".equals(auditStatus)) {
			return "已授权";
		}else if("4".equals(auditStatus)) {
			return "不可操作";
		}
		return auditStatus;
	}
	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * @return the franchiseeName
	 */
	public String getFranchiseeName() {
		return franchiseeName;
	}
	/**
	 * @param franchiseeName the franchiseeName to set
	 */
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	/**
	 * @return the rejectRemark
	 */
	public String getRejectRemark() {
		return rejectRemark;
	}
	/**
	 * @param rejectRemark the rejectRemark to set
	 */
	public void setRejectRemark(String rejectRemark) {
		this.rejectRemark = rejectRemark;
	}
	/**
	 * @return the authentApplyDate
	 */
	public Date getAuthentApplyDate() {
		return authentApplyDate;
	}
	/**
	 * @param authentApplyDate the authentApplyDate to set
	 */
	public void setAuthentApplyDate(Date authentApplyDate) {
		this.authentApplyDate = authentApplyDate;
	}
}
