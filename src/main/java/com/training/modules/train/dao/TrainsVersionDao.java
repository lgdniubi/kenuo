package com.training.modules.train.dao;


import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Version;

/**
 * 妃子校版本控制Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface TrainsVersionDao extends CrudDao<Version>{
		
		
}
