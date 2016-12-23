package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainOfflineTestSubscribeDao;
import com.training.modules.train.entity.TrainOfflineTestSubscribe;

/**
 * 线下“学生”预约管理Service
 * @author kele
 * @version 2016-4-7
 */
@Service
@Transactional(readOnly = false)
public class TrainOfflineTestSubscribeService extends CrudService<TrainOfflineTestSubscribeDao,TrainOfflineTestSubscribe>{

	/**
	 * 学生预约集合
	 * @param page
	 * @param trainOfflineTestSubscribe
	 * @return
	 */
	public Page<TrainOfflineTestSubscribe> findSetList(Page<TrainOfflineTestSubscribe> page, TrainOfflineTestSubscribe trainOfflineTestSubscribe) {
		trainOfflineTestSubscribe.setPage(page);
		page.setList(dao.findSetList(trainOfflineTestSubscribe));
		return page;
	}
	
	/**
	 * 查询学生预约详细信息
	 * @param page
	 * @param trainOfflineTestSubscribe
	 * @return
	 */
	public Page<TrainOfflineTestSubscribe> findList(Page<TrainOfflineTestSubscribe> page, TrainOfflineTestSubscribe trainOfflineTestSubscribe) {
		trainOfflineTestSubscribe.setPage(page);
		page.setList(dao.findList(trainOfflineTestSubscribe));
		return page;
	}
	
}
