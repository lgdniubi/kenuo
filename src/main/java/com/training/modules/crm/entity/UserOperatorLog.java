package com.training.modules.crm.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.User;

/**
 * kenuo @description：
 * 
 * @author：sharp @date：2017年3月9日
 */
public class UserOperatorLog extends DataEntity<UserOperatorLog> {

	private static final long serialVersionUID = 1L;
	private String id; // 自增ID
	private String userId; // 每天美耶用户ID
	private String operatorType; // 操作类型
	private String content; // 操作内容
	private Date createTime;//创建时间
	private String creatorName;//创建人名字

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
