package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ExercisesCategorys;

/**
 * 
 * @className ExercisesTuBeDao
 * @description TODO 试题管理
 * @author chenbing
 * @date 2018年1月24日 兵子
 *
 *
 */
@MyBatisDao
public interface ExercisesTuBeDao extends CrudDao<ExercisesCategorys>{

	/**
	 * 
	 * @Title: deleteExercises
	 * @Description: TODO 删除试题
	 * @throws
	 * 2018年1月25日 兵子
	 */
	int deleteExercises(ExercisesCategorys exercisesCategorys);
	
}
