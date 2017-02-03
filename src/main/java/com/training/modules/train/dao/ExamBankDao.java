package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ExamLessionMapping;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;

/**
 * 课程练习题Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface ExamBankDao extends CrudDao<ExamLessionMapping>{
		/**
		 * 保存课后练习题和单元测试
		 * @param examLessionMapping
		 */
		public void addExamLesson(ExamLessionMapping examLessionMapping);
		/**
		 * 查看所有课后练习题和单元测试
		 * @param examLessionMapping
		 * @return
		 */
		public List<ExamLessionMapping> lookAll(ExamLessionMapping examLessionMapping);
		/**
		 * 保存课后练习题和单元测试前先删除所有（修改状态为-1）--》再全部重新添加
		 * @param examLessionMapping
		 * @return
		 */
		public int deleteAll(ExamLessionMapping examLessionMapping);
		/**
		 * 通过课程id查询其所在分类
		 * @return
		 */
		public TrainCategorys findByLessonid(String lessonId);
		/**
		 * 通过子分类ziCategoryId查询其所在分类
		 * @param ziCategoryId
		 * @return
		 */
		public TrainCategorys findByziCategoryId(String ziCategoryId);
		/**
		 *通过试题分类查询试题
		 * @return
		 */
		public List<ExercisesCategorys> findByExerciseType(ExercisesCategorys exercisesCategorys); 
		/**
		 * 异步加载当前分类下的试题数量
		 * @param exercisesCategorys
		 * @return
		 */
		public int jiaZaiNum(ExercisesCategorys exercisesCategorys);
}
