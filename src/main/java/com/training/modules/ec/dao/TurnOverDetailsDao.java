package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.TurnOverDetails;

/**
 * 营业额明细表Dao
 * @author xiaoye  2017年11月16日
 *
 */
@MyBatisDao
public interface TurnOverDetailsDao extends CrudDao<TurnOverDetails>{
	
	/**
	 * 同步details表的数据到营业额明细表中
	 * @param turnOverDetails
	 */
	public void saveTurnOverDetails(TurnOverDetails turnOverDetails); 
}
