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
 * 类名称:	ArticlesStatisticsCountData
 * 类描述:	文章统计数据类
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
public class ArticlesStatisticsCountData extends TreeEntity<ArticlesStatisticsCountData> {

	/**
	 *@Fields	serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private Integer articlesId;			//文章ID
	private Integer lookCount;			//浏览量
	private Integer shareCount;			//分享量
	private Integer commonCount;		//评论数量
	private Integer likeCount;			//点赞数量
	private String isSham;				//是否为假数据（0：真实；1：假数据）
	
	private String isExist;				//是否在mtmy_articles_statistics 表中存在（为NULL 不存在）
	
	
	
	public String getIsExist() {
		return isExist;
	}
	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}
	
	public Integer getArticlesId() {
		return articlesId;
	}
	public void setArticlesId(Integer articlesId) {
		this.articlesId = articlesId;
	}
	public Integer getLookCount() {
		return lookCount;
	}
	public void setLookCount(Integer lookCount) {
		this.lookCount = lookCount;
	}
	public Integer getShareCount() {
		return shareCount;
	}
	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}
	public Integer getCommonCount() {
		return commonCount;
	}
	public void setCommonCount(Integer commonCount) {
		this.commonCount = commonCount;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public String getIsSham() {
		return isSham;
	}
	public void setIsSham(String isSham) {
		this.isSham = isSham;
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
	public ArticlesStatisticsCountData getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.training.common.persistence.TreeEntity#setParent(java.lang.Object)
	 */
	@Override
	public void setParent(ArticlesStatisticsCountData parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
