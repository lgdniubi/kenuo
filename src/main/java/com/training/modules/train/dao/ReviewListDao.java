package com.training.modules.train.dao;
import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.LessonAskComments;

/**
 * 评论详情列表Dao接口
 * @author coffee
 *
 */

@MyBatisDao
public interface ReviewListDao extends CrudDao<LessonAskComments>{
		/**
		 * 删除单个问题评论
		 * @param lessonAskComments
		 * @return
		 */
		public int deleteOneComment(LessonAskComments lessonAskComments);
}
