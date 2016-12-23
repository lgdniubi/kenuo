package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.FreeOrder;

/**
 * 免费体验Dao
 * @author coffee
 *
 */

@MyBatisDao
public interface FreeOrderDao extends CrudDao<FreeOrder>{

}
