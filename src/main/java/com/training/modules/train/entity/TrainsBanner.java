package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * TrainsBanner实体类
 * @author coffee
 *
 */
public class TrainsBanner extends DataEntity<TrainsBanner>{

	private static final long serialVersionUID = 1L;
	private int adId;		//bannerid
	private String adName;	//banner名称
	private int adType;		//类型  0首页   1提问
	private String adPic;		//照片
	private String adUrl;    //超链接
	private String remark;		//备注
	private String sort;		//排序
	private String flag;
	private Date createTime;    //创建时间
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
	public int getAdType() {
		return adType;
	}
	public void setAdType(int adType) {
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
}
