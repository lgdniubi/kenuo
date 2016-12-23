/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyFeedback;

/**
 * 留言DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface MtmyFeedbackDao extends CrudDao<MtmyFeedback> {
	/**
	 * 修改反馈状态（修改为-1）
	 * @param feedback
	 */
	public void updateFbStatus(MtmyFeedback feedback);
}
