/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * 
 * @version 2016-1-1
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

	public void empty();
}
