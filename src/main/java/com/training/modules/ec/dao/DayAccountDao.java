package com.training.modules.ec.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.DayAccount;
/**
 * 日志dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface DayAccountDao extends TreeDao<DayAccount> {
	


}
