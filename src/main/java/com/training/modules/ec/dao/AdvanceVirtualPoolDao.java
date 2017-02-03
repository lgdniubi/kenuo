package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.AdvanceVirtualPool;

/**
 * 预付款虚拟池Dao
 * @author 小叶  2017-1-10 
 *
 */
@MyBatisDao
public interface AdvanceVirtualPoolDao extends CrudDao<AdvanceVirtualPool>{
	
	public List<AdvanceVirtualPool> newFindList(AdvanceVirtualPool advanceVirtualPool);
	
}
