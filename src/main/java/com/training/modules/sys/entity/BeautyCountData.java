/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月7日 下午3:14:50
 * 修改人:	
 * 修改时间:	2017年6月7日 下午3:14:50
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 类名称:	BeautyCountData
 * 类描述:	技师统计数据类
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月7日 下午3:14:50
 */
public class BeautyCountData extends TreeEntity<BeautyCountData> {

	/**
	 *@Fields	serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String beautyId;			//技师id
	private String isSham;				//是否为假数据（0：真实；1：假数据）
	private Float evaluationScore;		//技师评分
	private Integer evaluationCount;	//技师评价数
	private Integer apptCount;			//技师预约数
	private Date updateDate;			//数据更新时间
	
	private String isExist;				//是否在train_beauty_statistics 表中存在（为NULL 不存在）
	
	private Float beautyScore;			//个人形象评价评分(1.5到5)  	 土豆添加
	private Float serviceScore;			//服务评价评分(1.5到5)		 土豆添加
	private Float skillScore;			//技能手法评价评分(1.5到5)	 土豆添加
	
	public String getIsExist() {
		return isExist;
	}
	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
	public String getBeautyId() {
		return beautyId;
	}
	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}
	public String getIsSham() {
		return isSham;
	}
	public void setIsSham(String isSham) {
		this.isSham = isSham;
	}
	public Float getEvaluationScore() {
		return evaluationScore;
	}
	public void setEvaluationScore(Float evaluationScore) {
		this.evaluationScore = evaluationScore;
	}
	public Integer getEvaluationCount() {
		return evaluationCount;
	}
	public void setEvaluationCount(Integer evaluationCount) {
		this.evaluationCount = evaluationCount;
	}
	public Integer getApptCount() {
		return apptCount;
	}
	public void setApptCount(Integer apptCount) {
		this.apptCount = apptCount;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#getParent()
	 */
	@Override
	public BeautyCountData getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(BeautyCountData parent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String toString() {
		return "BeautyCountData [beautyId=" + beautyId + ", isSham=" + isSham + ", evaluationScore=" + evaluationScore
				+ ", evaluationCount=" + evaluationCount + ", apptCount=" + apptCount + ", updateDate=" + updateDate
				+ ", isExist=" + isExist + "]";
	}
	public Float getBeautyScore() {
		return beautyScore;
	}
	public void setBeautyScore(Float beautyScore) {
		this.beautyScore = beautyScore;
	}
	public Float getServiceScore() {
		return serviceScore;
	}
	public void setServiceScore(Float serviceScore) {
		this.serviceScore = serviceScore;
	}
	public Float getSkillScore() {
		return skillScore;
	}
	public void setSkillScore(Float skillScore) {
		this.skillScore = skillScore;
	}
	
	
}
