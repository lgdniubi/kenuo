package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 妃子校数据权限表fzx_user_role_office
 * @author jf
 *
 */
public class UserRoleOffice extends DataEntity<UserRoleOffice>{

	private static final long serialVersionUID = 1L;
	private Integer count;  //记录数量
	private Integer userRoleId;  //user_role_id
	private String officeId;  //机构id
	private Integer flag;  //标记，1查询数量等于1的，2查询数量大于1的。
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	
}
