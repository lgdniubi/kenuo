package com.training.modules.crm.entity;

import com.training.common.persistence.DataEntity;

/**    
* kenuo      
* @description：用户联系信息实体类   
* @author：sharp   
* @date：2017年3月6日            
*/
public class UserContactInfo extends DataEntity<UserContactInfo>{
	
		private static final long serialVersionUID = 1L;

		private String userId;
		private String qq;
		private String wechat;
		private String email;
	    private String companyName;
		private String areaId;
		private String address;
		
		
		
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getQq() {
			return qq;
		}
		public void setQq(String qq) {
			this.qq = qq;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	
		public String getWechat() {
			return wechat;
		}
		public void setWechat(String wechat) {
			this.wechat = wechat;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getAreaId() {
			return areaId;
		}
		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
}
