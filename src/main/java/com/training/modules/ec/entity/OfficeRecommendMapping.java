package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 店铺推荐中间表实体类
 * @author xiaoye  2017-9-12
 *
 */
public class OfficeRecommendMapping extends DataEntity<OfficeRecommendMapping>{

	private static final long serialVersionUID = 1L;
	
	private int officeRecommendMappingId;   //主键ID
	private int recommendId;                 //店铺推荐组ID
	private String officeId;                // 机构ID
	private int sort;                        //排序(数字越大越靠前)
	private String officeName;              //店铺名称
	
	
	public int getOfficeRecommendMappingId() {
		return officeRecommendMappingId;
	}
	public void setOfficeRecommendMappingId(int officeRecommendMappingId) {
		this.officeRecommendMappingId = officeRecommendMappingId;
	}
	public int getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(int recommendId) {
		this.recommendId = recommendId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

}

