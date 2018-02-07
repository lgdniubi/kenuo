package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.FranchiseeBanner;
import com.training.modules.train.entity.TrainsBanner;


/**
 * banner图接口
 * @author coffee
 *
 */
@MyBatisDao
public interface TrainsBannerDao extends CrudDao<TrainsBanner>{
	public TrainsBanner getBanner(int adId);
	public void deleteBanner(TrainsBanner trainsBanner);
	/**
	 * 
	 * @Title: insertBanner
	 * @Description: TODO 插入banner图，返回主键id
	 * @param trainsBanner
	 * @return:
	 * @return: TrainsBanner
	 * @throws
	 * 2018年1月23日 兵子
	 */
	public void insertBanner(TrainsBanner trainsBanner);
	/**
	 * 
	 * @Title: insertFranchiseeBanner
	 * @Description: TODO 插入banner商家权限
	 * @param fBanner:
	 * @return: void
	 * @throws
	 * 2018年1月23日 兵子
	 */
	public void insertFranchiseeBanner(FranchiseeBanner fBanner);
	
	/**
	 * 
	 * @Title: delteFranchiseeBanner
	 * @Description: TODO 删除权限
	 * @param fBanner
	 * @return:
	 * @return: int
	 * @throws
	 * 2018年1月23日 兵子
	 */
	public int delteFranchiseeBanner(FranchiseeBanner fBanner);
}
