package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLiveRoomDao;
import com.training.modules.train.entity.TrainLiveRoom;

@Service
@Transactional(readOnly = false)
public class TrainLiveRoomService  extends CrudService<TrainLiveRoomDao,TrainLiveRoom>{
	
	@Autowired
	private TrainLiveRoomDao trainLiveRoomDao;
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public int findByUserId(String userId){
		return trainLiveRoomDao.findByUserId(userId);
	}
	/**
	 * 
	 * @param trainLiveRoom
	 * @return
	 */
	public int insertRoom(TrainLiveRoom trainLiveRoom){
		return trainLiveRoomDao.insertRoom(trainLiveRoom);
	}

}
