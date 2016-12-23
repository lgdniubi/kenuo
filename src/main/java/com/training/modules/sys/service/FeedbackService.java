package com.training.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.DateUtils;
import com.training.modules.sys.dao.FeedbackDao;
import com.training.modules.sys.entity.Feedback;

/**
 * 反馈管理service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class FeedbackService extends CrudService<FeedbackDao,Feedback>{
	/**
	 * 查询所有反馈问题
	 */
	public Page<Feedback> findPage(Page<Feedback> page,Feedback feedback){
		// 设置默认时间范围，默认当前月
		if (feedback.getBeginDate() == null){
			feedback.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (feedback.getEndDate() == null){
			feedback.setEndDate(DateUtils.addMonths(feedback.getBeginDate(), 1));
		}
		return super.findPage(page, feedback);
	}
	/**
	 * 修改反馈状态（修改为-1）
	 * @param feedback
	 */
	public void updateFbStatus(Feedback feedback){
		dao.updateFbStatus(feedback);
	}
	/**
	 * 查询单个问题反馈状态
	 */
	public Feedback get(Feedback feedback){
		return dao.get(feedback);
	}
}
