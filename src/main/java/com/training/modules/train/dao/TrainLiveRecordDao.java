package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveRecord;

/**
 * 云币充值记录以及佣金兑换云币记录实体类
 * @author xiaoye 2017年3月15日
 *
 */
@MyBatisDao
public interface TrainLiveRecordDao extends CrudDao<TrainLiveRecord> {
	/**
	 * 云币充值记录
	 * @return
	 */
	public List<TrainLiveRecord> selectPayRecord(TrainLiveRecord trainLiveRecord);
	
	/**
	 * 佣金兑换云币记录
	 * @return
	 */
	public List<TrainLiveRecord> selectExchangeRecord(TrainLiveRecord trainLiveRecord);
	
}
