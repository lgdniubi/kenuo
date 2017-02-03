package com.training.modules.personnelfile.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserDepartures;

@MyBatisDao
public interface UserDeparturesDao extends CrudDao<UserDepartures> {
    int deleteByPrimaryKey(String id);

    int insert(UserDepartures record);

    int insertSelective(UserDepartures record);

    UserDepartures selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserDepartures record);

    int updateByPrimaryKey(UserDepartures record);

	void deleteByUserId(@Param("userId")String id);
}