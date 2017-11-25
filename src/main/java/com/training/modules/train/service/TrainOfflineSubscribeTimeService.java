package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainOfflineSubscribeTimeDao;
import com.training.modules.train.entity.TrainOfflineSubscribeTime;

/**
 * 线下预约管理Service
 * @author kele
 * @version 2016年3月17日
 */
@Service
@Transactional(readOnly = false)
public class TrainOfflineSubscribeTimeService extends CrudService<TrainOfflineSubscribeTimeDao, TrainOfflineSubscribeTime>{

	/**
	 * 查询所在状态下的所有数据
	 * @param trainOfflineSubscribeTime status [0发布;1终止]
	 * @return
	 */
	public List<TrainOfflineSubscribeTime> findStatusList(TrainOfflineSubscribeTime trainOfflineSubscribeTime){
		return dao.findStatusList(trainOfflineSubscribeTime);
	}
	
	/**
	 * 修改status[0发布;1终止]
	 * @param trainOfflineSubscribeTime
	 * @return
	 */
	public int updateStatus(TrainOfflineSubscribeTime trainOfflineSubscribeTime){
		return dao.update(trainOfflineSubscribeTime);
	}

}
