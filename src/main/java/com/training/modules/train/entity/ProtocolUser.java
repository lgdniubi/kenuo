package com.training.modules.train.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.IdGen;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 用户迁移协议表
 * @author: jingfeng
 * @date 2018年5月2日下午3:18:53
 */
public class ProtocolUser extends DataEntity<ProtocolUser>{
	  
	private static final long serialVersionUID = 2L;
	private String userId;				//用户id--签订人
	private Integer protocolId;				//协议模板id
	private String protocolName;			//协议状态：1履约中,2审核中,3审核未通过,4已作废,5已失效
	private String status;			//协议状态：1履约中,2审核中,3审核未通过,4已作废,5已失效
	private int typeId;			//协议类型：1妃子校注册,2用户认证手艺人,3用户认证企业,4用户登录商家PC
	private Integer franchiseeId;			//商家id
	private String companyName;			//商家名称
	private String officeId;			//机构id
	private String officeName;			//机构名称
	private Date authStartDate;			//有效期---授权开始时间
	private Date authEndDate;			//有效期---授权结束时间
	private String authBy;			//审核人
	@Override
	public void preInsert(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public Integer getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(Integer franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public Date getAuthStartDate() {
		return authStartDate;
	}
	public void setAuthStartDate(Date authStartDate) {
		this.authStartDate = authStartDate;
	}
	public Date getAuthEndDate() {
		return authEndDate;
	}
	public void setAuthEndDate(Date authEndDate) {
		this.authEndDate = authEndDate;
	}
	public String getAuthBy() {
		return authBy;
	}
	public void setAuthBy(String authBy) {
		this.authBy = authBy;
	}
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
}
