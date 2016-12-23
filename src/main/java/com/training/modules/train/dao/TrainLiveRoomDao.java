package com.training.modules.train.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveRoom;

/**
 * 直播间dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface TrainLiveRoomDao extends TreeDao<TrainLiveRoom>{
	
	/**
	 * 查询数据
	 * @param userId
	 * @return
	 */
	public int findByUserId(String userId);
	/**
	 * 保存直播数据
	 * @param trainLiveRoom
	 * @return
	 */
	
	public int insertRoom(TrainLiveRoom trainLiveRoom);
	

}
