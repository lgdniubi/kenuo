package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLessons;

/**
 * 课程DAO
 * @author kele
 * @version 2016年3月10日
 */
@MyBatisDao
public interface TrainLessonsDao extends CrudDao<TrainLessons>{
	
	/**
	 * 删除方法 
	 * 只是表中修改status状态
	 * @param trainLessons
	 * @return
	 */
	public int deleteCourseForUpdate(TrainLessons trainLessons);
	
	/**
	 * 公共课程列表-查询
	 * @param trainLessons
	 * @return
	 */
	public List<TrainLessons> commoncourselist(TrainLessons trainLessons);
}
