package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
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
}
