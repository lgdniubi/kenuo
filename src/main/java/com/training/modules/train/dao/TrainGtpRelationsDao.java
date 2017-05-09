package com.training.modules.train.dao;


import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainGtpRelations;

/**
 * 地推Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface TrainGtpRelationsDao extends CrudDao<TrainGtpRelations>{
	
	/**
	 * 邀请总数
	 * @param office
	 * @return
	 */
	public int report(TrainGtpRelations trainGtpRelations);
}