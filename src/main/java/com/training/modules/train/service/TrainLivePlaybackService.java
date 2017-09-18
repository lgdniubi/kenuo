package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainLivePlaybackDao;
import com.training.modules.train.entity.TrainLivePlayback;

@Service
@Transactional(readOnly = false)
public class TrainLivePlaybackService  extends CrudService<TrainLivePlaybackDao,TrainLivePlayback>{
	

	@Autowired
	private TrainLivePlaybackDao trainLivePlaybackDao;
	@Autowired
	private UserDao userDao;

	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLivePlayback> findPlaybackList(Page<TrainLivePlayback> page, TrainLivePlayback trainLivePlayback) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLivePlayback.getSqlMap().put("dsf", companyDateScope((String)userDao.findFranchiseeAuth(UserUtils.getUser()).get("companyIds"),UserUtils.getUser()));
		trainLivePlayback.setPage(page);
		// 执行分页查询
		page.setList(trainLivePlaybackDao.findAllList(trainLivePlayback));
		return page;
	}
	
	/**
	 * 显示隐藏回看
	 * @param trainLivePlayback
	 * @return
	 */
	public int updateIsShow(TrainLivePlayback trainLivePlayback){
		return trainLivePlaybackDao.updateIsShow(trainLivePlayback);
	}

	
}
