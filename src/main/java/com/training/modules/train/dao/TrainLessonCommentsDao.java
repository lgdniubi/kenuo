package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLessonComments;

/**
 * 课程评论管理DAO
 * @author kele
 * @version 2016年3月17日
 */
@MyBatisDao
public interface TrainLessonCommentsDao extends CrudDao<TrainLessonComments>{
	
	/**
	 * 删除方法 
	 * 只是表中修改status状态
	 * @param trainLessonComments
	 * @return
	 */
	public int deleteCommentsForUpdate(TrainLessonComments trainLessonComments);
}
