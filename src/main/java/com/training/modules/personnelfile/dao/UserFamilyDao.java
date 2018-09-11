package com.training.modules.personnelfile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserFamily;
@MyBatisDao
public interface UserFamilyDao extends CrudDao<UserFamily> {
    int deleteByPrimaryKey(String id);
    int insert(UserFamily record);
    int insertSelective(UserFamily record);
    UserFamily selectByPrimaryKey(String id);
    int updateByPrimaryKeySelective(UserFamily record);
    int updateByPrimaryKey(UserFamily record);
    /**
     * 批量保存用户家庭情况
     * @param userFamilies
     */
    public void saveUserFamilyList(List<UserFamily> userFamilies);
    /**
     * 获得执行类型的对象
     * @param userFamily
     * @return
     */
	UserFamily getObject(@Param("baseInfoId")Integer baseInfoId, @Param("nameType")Integer nameType);
	
	void deleteByUserId(@Param("userId")String id);
}