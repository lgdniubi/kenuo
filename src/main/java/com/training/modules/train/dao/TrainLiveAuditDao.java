package com.training.modules.train.dao;

import java.util.List;

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
	public List<TrainLiveAudit> selectOutLive();
	
	/**
	 * 过期数据修改
	 * @param id
	 * @return
	 */
	public int updateLiveOut(String id);
	
	/**
	 * 查询将要直播的数据
	 * @return
	 */
	public List<TrainLiveAudit> selectWantLive();
	
}
