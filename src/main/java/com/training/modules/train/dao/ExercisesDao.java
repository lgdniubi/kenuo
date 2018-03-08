package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Exercises;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;
/**
 * 所有试题Dao接口
 * @author Superman
 *
 */
@MyBatisDao
public interface ExercisesDao extends CrudDao<ExercisesCategorys>{
	//	public List<String> findTypeList(ExercisesCategorys exercisesCategorys);
		/**
		 * 试题库--》删除试题（修改转态为-1）
		 * @param exercises
		 * @return
		 */
		public int deleteOneExam(Exercises exercises);
		/**
		 * 试题库删除试题的同时  将相关联的课后试题、单元考试删除（修改转态为-1）
		 * @param exercises
		 * @return
		 */
		public int deleteOneLessonExam(Exercises exercises);
	//	public void addExam1(Exercises exercises);
		/**
		 * 通过二级分类反查父类
		 * @param exerciseId
		 * @return
		 */
		public String getParentId(String exerciseId);
		/**
		 * 试题列表--》添加试题--》保存试题
		 * @param exercises
		 */
		public void addExam(Exercises exercises);
		/**
		 * 试题列表--》修改试题
		 * @param exercises
		 * @return
		 */
		public int update(Exercises exercises);
		/**
		 * 通过课程名称查询课程id
		 * @param categoryName
		 * @return
		 */
		public TrainCategorys findByName(String categoryName);
		/**
		 * 
		 * @Title: findExercisesId
		 * @Description: TODO 查询当前课程的试题id
		 * @throws
		 * 2018年2月28日 兵子
		 */
		public List<String> findExercisesId(ExercisesCategorys exercisesCategorys);
		/**
		 * 
		 * @Title: findExerPage
		 * @Description: TODO 分页查询试题（排除当前课程已有的试题）
		 * @throws
		 * 2018年2月28日 兵子
		 */
		public List<ExercisesCategorys> findExerPage(ExercisesCategorys exercisesCategorys);
}
