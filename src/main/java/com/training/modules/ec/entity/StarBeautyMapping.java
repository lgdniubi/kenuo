package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 
 * 明星技师自由配置表
 * @author 土豆   2018-3-7
 *
 */
public class StarBeautyMapping extends DataEntity<StarBeautyMapping>{

	private static final long serialVersionUID = 1L;
	
	private int mappingId;		//主键id
	private String starId;		//明星技师组id
	private String userId;		//技师id(妃子校的id)
	private String officeIds;	//组织架构（本身的组织架构加上parent_ids）
	private String starBeautyName;		//技师姓名
	private String starBeautyPhoto;		//用户头像
	private String sort;		//排序(数字越大越靠前)
	
	
	public int getMappingId() {
		return mappingId;
	}
	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}
	public String getStarId() {
		return starId;
	}
	public void setStarId(String starId) {
		this.starId = starId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOfficeIds() {
		return officeIds;
	}
	public void setOfficeIds(String officeIds) {
		this.officeIds = officeIds;
	}
	public String getStarBeautyName() {
		return starBeautyName;
	}
	public void setStarBeautyName(String starBeautyName) {
		this.starBeautyName = starBeautyName;
	}
	public String getStarBeautyPhoto() {
		return starBeautyPhoto;
	}
	public void setStarBeautyPhoto(String starBeautyPhoto) {
		this.starBeautyPhoto = starBeautyPhoto;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	
	
}
