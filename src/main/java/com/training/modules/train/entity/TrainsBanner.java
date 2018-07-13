package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Dict;
import com.training.modules.sys.entity.User;

/**
 * TrainsBanner实体类
 * @author coffee
 *
 */
public class TrainsBanner extends DataEntity<TrainsBanner>{

	private static final long serialVersionUID = 1L;
	private int adId;		//bannerid
	private String adName;	//banner名称
	private Integer adType =100;		//类型  0首页   1动态 2工作台，100是默认值查询全部
	private String adPic;		//照片
	private String adUrl;    //超链接
	private String remark;		//备注
	private String sort;		//排序
	private String flag;
	private Date createTime;    //创建时间
	
	private Dict dict;
	
	private User user;			//用户
	private FranchiseeBanner franchiseeBanner;	//banner商家权限
	private String soFranchiseeIds;		//可见商家ids
	private String parentIds;	//添加者的机构及父节点
	
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public Integer getAdType() {
		return adType;
	}
	public void setAdType(Integer adType) {
		this.adType = adType;
	}
	public String getAdPic() {
		return adPic;
	}
	public void setAdPic(String adPic) {
		this.adPic = adPic;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Dict getDict() {
		return dict;
	}
	public void setDict(Dict dict) {
		this.dict = dict;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public FranchiseeBanner getFranchiseeBanner() {
		return franchiseeBanner;
	}
	public void setFranchiseeBanner(FranchiseeBanner franchiseeBanner) {
		this.franchiseeBanner = franchiseeBanner;
	}
	public String getSoFranchiseeIds() {
		return soFranchiseeIds;
	}
	public void setSoFranchiseeIds(String soFranchiseeIds) {
		this.soFranchiseeIds = soFranchiseeIds;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
}
