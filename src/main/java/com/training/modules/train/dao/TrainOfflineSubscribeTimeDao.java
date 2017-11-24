package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Subscribe;
import com.training.modules.train.entity.TrainOfflineSubscribeTime;

/**
 * 线下预约管理DAO
 * @author kele
 * @version 2016年3月17日
 */
@MyBatisDao
public interface TrainOfflineSubscribeTimeDao extends CrudDao<TrainOfflineSubscribeTime>{

	/**
	 * 查询所在状态下的列表
	 * @param trainOfflineSubscribeTime
	 * @return
	 */
	public List<TrainOfflineSubscribeTime> findStatusList(TrainOfflineSubscribeTime trainOfflineSubscribeTime);
	
	
}
