package com.training.modules.personnelfile.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserSelfevaluation;
@MyBatisDao
public interface UserSelfevaluationDao extends CrudDao<UserSelfevaluation>{
    int deleteByPrimaryKey(String id);

    int insert(UserSelfevaluation record);

    int insertSelective(UserSelfevaluation record);

    UserSelfevaluation selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserSelfevaluation record);

    int updateByPrimaryKey(UserSelfevaluation record);

	void deleteByUserId(@Param("userId")String id);
}