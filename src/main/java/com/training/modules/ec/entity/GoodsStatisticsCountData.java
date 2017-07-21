/**
 * 项目名称:	kenuo
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 * 修改人:	
 * 修改时间:	2017年6月19日
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 类名称:	GoodsStatisticsCountData
 * 类描述:	商品统计数据类
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
public class GoodsStatisticsCountData extends TreeEntity<GoodsStatisticsCountData> {

	/**
	 *@Fields	serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String goodsId;				//商品ID
	private String isSham;				//是否为假数据（0：真实；1：假数据）
	private Float evaluationScore;		//商品评分
	private Integer evaluationCount;	//评价数
	private Integer buyCount;			//购买数量
	private Date updateDate;			//数据更新时间
	
	private String isExist;				//是否在mtmy_goods_statistics 表中存在（为NULL 不存在）
	
	
	
	public String getIsExist() {
		return isExist;
	}
	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
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
	public GoodsStatisticsCountData getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(GoodsStatisticsCountData parent) {
		// TODO Auto-generated method stub
		
	}
	
}
