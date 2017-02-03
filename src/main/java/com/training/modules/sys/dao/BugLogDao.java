package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.BugLog;

/**
 * bug日志dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface BugLogDao extends CrudDao<BugLog>{

	/**
	 * 清空日志
	 */
	public void empty();
	
}
