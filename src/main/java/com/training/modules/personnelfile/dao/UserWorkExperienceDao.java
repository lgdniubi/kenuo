package com.training.modules.personnelfile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserWorkExperience;

@MyBatisDao
public interface UserWorkExperienceDao extends CrudDao<UserWorkExperience> {
	
    int insert(UserWorkExperience record);
    int insertSelective(UserWorkExperience record);

    /**
     * 批量保存
     * @param userWorkExperienceList
     */
    public void saveUserWorkExperienceList(List<UserWorkExperience> userWorkExperienceList);
	void deleteByUserId(@Param("userId")String id);
}