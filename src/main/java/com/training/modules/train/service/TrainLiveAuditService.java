package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLiveAuditDao;
import com.training.modules.train.dao.TrainLivePlaybackDao;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLivePlayback;

@Service
@Transactional(readOnly = false)
public class TrainLiveAuditService  extends CrudService<TrainLiveAuditDao,TrainLiveAudit>{
	
	@Autowired
	private TrainLiveAuditDao trainLiveAuditDao;
	@Autowired
	private TrainLivePlaybackDao trainLivePlaybackDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLiveAudit> findLive(Page<TrainLiveAudit> page, TrainLiveAudit trainLiveAudit) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLiveAudit.setPage(page);
		// 执行分页查询
		page.setList(trainLiveAuditDao.findList(trainLiveAudit));
		return page;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLivePlayback> findback(Page<TrainLivePlayback> page, TrainLivePlayback trainLivePlayback) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLivePlayback.setPage(page);
		// 执行分页查询
		page.setList(trainLivePlaybackDao.findList(trainLivePlayback));
		return page;
	}
	
	
	/**
	 * 保存数据
	 * @param trainLiveAudit
	 * @return
	 */
	public int update(TrainLiveAudit trainLiveAudit){
		return trainLiveAuditDao.update(trainLiveAudit);
	}
	/**
	 * 直播审核
	 * @return
	 */
	public int updateLiveLiveAudit(){
		return dao.updateLiveLiveAudit();
	}
}
