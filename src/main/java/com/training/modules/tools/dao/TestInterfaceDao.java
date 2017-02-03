/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.tools.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.tools.entity.TestInterface;

/**
 * 接口DAO接口
 * @author lgf
 * @version 2016-01-07
 */
@MyBatisDao
public interface TestInterfaceDao extends CrudDao<TestInterface> {
	
}