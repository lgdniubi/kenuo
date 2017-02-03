/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.personnelfile.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserEmployed;

/**
 * 人事档案DAO接口
 * @author ouwei
 *  2016-12-1
 */
@MyBatisDao
public interface UserEmployedDao extends CrudDao<UserEmployed> {
	
	public void saveUserEmployed(UserEmployed userEmployed);

	public void deleteByUserId(@Param("userId")String id);
	
}

