package com.training.modules.train.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveAudit;

/**
 * 直播dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface TrainLiveAuditDao extends TreeDao<TrainLiveAudit>{
	/**
	 * 直播审核
	 * @return
	 */
	public int updateLiveLiveAudit();
	

}
