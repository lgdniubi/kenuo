package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainOfflineTestSubscribe;

/**
 * 线下预约管理DAO
 * @author kele
 * @version 2016年3月17日
 */
@MyBatisDao
public interface TrainOfflineTestSubscribeDao extends CrudDao<TrainOfflineTestSubscribe>{

	/**
	 * 查询学生预约集合
	 * @param trainOfflineTestSubscribe
	 * @return
	 */
	public List<TrainOfflineTestSubscribe> findSetList(TrainOfflineTestSubscribe trainOfflineTestSubscribe); 

	
	/**
	 * 查询学生预约详细信息
	 */
	public List<TrainOfflineTestSubscribe> findList(TrainOfflineTestSubscribe trainOfflineTestSubscribe);
	
}
