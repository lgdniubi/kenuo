package com.training.modules.train.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLivePlayback;

/**
 * 回看dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface TrainLivePlaybackDao extends TreeDao<TrainLivePlayback>{
	
	/**
	 * 显示隐藏回看
	 * @param trainLivePlayback
	 * @return
	 */
	public int updateIsShow(TrainLivePlayback trainLivePlayback);

}
