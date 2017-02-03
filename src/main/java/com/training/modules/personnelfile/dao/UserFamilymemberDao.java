package com.training.modules.personnelfile.dao;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserFamilymember;
@MyBatisDao
public interface UserFamilymemberDao extends CrudDao<UserFamilymember> {
    int deleteByPrimaryKey(String id);

    int insert(UserFamilymember record);

    int insertSelective(UserFamilymember record);

    UserFamilymember selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserFamilymember record);

    int updateByPrimaryKey(UserFamilymember record);

	UserFamilymember getObject(@Param("userID")String id, @Param("nameType")int nameType);

	void deleteByUserId(@Param("userId")String id);
}