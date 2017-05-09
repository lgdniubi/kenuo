package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainGtpRelationsDao;
import com.training.modules.train.entity.TrainGtpRelations;


/**
 * 地推service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainGtpRelationsService extends CrudService<TrainGtpRelationsDao, TrainGtpRelations>{
	
	/**
	 * 地推list
	 * @param page
	 * @param trainGtpRelations
	 * @return
	 */
	public Page<TrainGtpRelations> findList(Page<TrainGtpRelations> page, TrainGtpRelations trainGtpRelations) {
		trainGtpRelations.getSqlMap().put("dsf", dataScopeFilter(trainGtpRelations.getCurrentUser(),"c"));
		// 设置分页参数
		trainGtpRelations.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(trainGtpRelations));
		return page;
	}
	/**
	 * 地推邀请详情
	 * @param page
	 * @param trainGtpRelations
	 * @return
	 */
	public Page<TrainGtpRelations> findAllList(Page<TrainGtpRelations> page, TrainGtpRelations trainGtpRelations) {
		trainGtpRelations.setPage(page);
		page.setList(dao.findAllList(trainGtpRelations));
		return page;
	}
	/**
	 * 邀请总数
	 * @param office
	 * @return
	 */
	public int report(TrainGtpRelations trainGtpRelations){
		trainGtpRelations.getSqlMap().put("dsf", dataScopeFilter(trainGtpRelations.getCurrentUser(),"c"));
		return dao.report(trainGtpRelations);
	}
}