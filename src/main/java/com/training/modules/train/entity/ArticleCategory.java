package com.training.modules.train.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.common.persistence.DataEntity;
/**
 * 文章类别Entity
 * @author coffee
 *
 */
public class ArticleCategory extends DataEntity<ArticleCategory>{
	private static final long serialVersionUID = 1L;
	
	private String categoryId;			//文章类别ID
	private String name;				//类别名称
	private int status;					//状态		0  显示   -1  不显示
	private Date createtime;			//创建时间
	private String createuser;			//创建者ID（用户ID）
	
	private String roleType;		//权限类型   1 超级管理员   查看所有数据  2  普通管理员 所在区、公司一下所有数据  3  培训师 自己的数据
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
}
