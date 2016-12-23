package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyFeedbackDao;
import com.training.modules.ec.entity.MtmyFeedback;

/**
 * 反馈管理service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyFeedbackService extends CrudService<MtmyFeedbackDao,MtmyFeedback>{
	/**
	 * 查询所有反馈问题
	 */
	public Page<MtmyFeedback> findPage(Page<MtmyFeedback> page,MtmyFeedback feedback){
		feedback.setPage(page);
		page.setList(dao.findList(feedback));
		return page;
	}
	/**
	 * 修改反馈状态（修改为-1）
	 * @param feedback
	 */
	public void updateFbStatus(MtmyFeedback feedback){
		dao.updateFbStatus(feedback);
	}
	/**
	 * 查询单个问题反馈状态
	 */
	public MtmyFeedback get(MtmyFeedback feedback){
		return dao.get(feedback);
	}
}
