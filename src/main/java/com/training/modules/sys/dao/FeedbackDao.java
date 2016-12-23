/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Feedback;

/**
 * 反馈DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface FeedbackDao extends CrudDao<Feedback> {
	/**
	 * 修改反馈状态（修改为-1）
	 * @param feedback
	 */
	public void updateFbStatus(Feedback feedback);
}
