package com.training.modules.ec.dao;


import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyVersion;

/**
 * 每天美耶版本控制Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface MtmyVersionDao extends CrudDao<MtmyVersion>{
		
		
}
