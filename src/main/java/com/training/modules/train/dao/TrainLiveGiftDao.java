package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveGift;

/**
 * 直播送的礼物Dao
 * @author xiaoye  2017年3月16日
 *
 */
@MyBatisDao
public interface TrainLiveGiftDao extends CrudDao<TrainLiveGift>{
	
	/**
	 * 根据 trainLiveGiftId查找相应的礼物
	 * @param trainLiveGiftId
	 * @return
	 */
	public TrainLiveGift getTrainLiveGift(int trainLiveGiftId);
	
	/**
	 * 逻辑删除礼物
	 * @param trainLiveGift
	 */
	public void deleteGift(int trainLiveGiftId);
	
	/**
	 * 更改礼物的状态
	 * @param trainLiveGift
	 */
	public void updateIsShow(TrainLiveGift trainLiveGift);
	
	/**
	 * 查找处于显示状态的礼物的个数
	 * @return
	 */
	public int selectNum();
}
