package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLiveRecordDao;
import com.training.modules.train.entity.TrainLiveRecord;

@Service
@Transactional(readOnly = false)
public class TrainLiveRecordService extends CrudService<TrainLiveRecordDao,TrainLiveRecord>{

		@Autowired
		private TrainLiveRecordDao trainLiveRecordDao;
		
		/**
		 * 云币充值记录
		 * @return
		 */
		public Page<TrainLiveRecord> selectPayRecord(Page<TrainLiveRecord> page,TrainLiveRecord trainLiveRecord){
			trainLiveRecord.setPage(page);
			page.setList(trainLiveRecordDao.selectPayRecord(trainLiveRecord));
			return page;
		}
		
		/**
		 * 佣金兑换云币记录
		 * @return
		 */
		public Page<TrainLiveRecord> selectExchangeRecord(Page<TrainLiveRecord> page,TrainLiveRecord trainLiveRecord){
			trainLiveRecord.setPage(page);
			page.setList(trainLiveRecordDao.selectExchangeRecord(trainLiveRecord));
			return page;
		}
}
