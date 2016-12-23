package com.training.modules.ec.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.CourierCompany;
/**
 * 快递物流dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface CourierCompanyDao extends TreeDao<CourierCompany> {
	


}
