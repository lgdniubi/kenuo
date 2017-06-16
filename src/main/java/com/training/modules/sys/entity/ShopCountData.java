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
 * 类描述:	店铺统计数据类
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月7日 下午3:14:50
 */
public class ShopCountData extends TreeEntity<ShopCountData> {

	/**
	 *@Fields	serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String shopId;				//店铺ID
	private String isSham;				//是否为假数据（0：真实；1：假数据）
	private Float evaluationScore;		//店铺评分
	private Integer evaluationCount;	//店铺评价数
	private Integer apptCount;			//店铺预约数
	private Date updateDate;			//数据更新时间
	
	private String isExist;				//是否在train_shop_statistics 表中存在（为NULL 不存在）
	
	
	
	public String getIsExist() {
		return isExist;
	}
	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
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
	public ShopCountData getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(ShopCountData parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
