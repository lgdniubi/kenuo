/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.personnelfile.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserBaseInfo;

/**
 * 人事档案DAO接口
 * @author ouwei
 *  2016-12-1
 */
@MyBatisDao
public interface UserBaseInfoDao extends CrudDao<UserBaseInfo> {
	
	public void saveUserBaseInfo(UserBaseInfo userBaseInfo);
	/**
	 * 物理删除数据，用来做档案修改时候使用
	 * @param userId
	 */
	public void deleteByUserId(@Param("userId")String userId);
}

