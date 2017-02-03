package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.ExercisesDao;
import com.training.modules.train.entity.Exercises;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;

/**
 *	试题service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = false)
public class ExercisesService extends CrudService<ExercisesDao,ExercisesCategorys>{
	/**
	 * 查询所有试题
	 * @param page
	 * @param exercises
	 * @return
	 */
	public Page<ExercisesCategorys> find(Page<ExercisesCategorys> page, ExercisesCategorys exercisesCategorys) {
		exercisesCategorys.setPage(page);
		page.setList(dao.findList(exercisesCategorys));
		return page;
	}
//	public List<String> findTypeList(){
//		return dao.findTypeList(new ExercisesCategorys());
//	} 
	/**
	 * 试题库--》删除试题（修改转态为-1）
	 * 只修改status
	 * @param exercises
	 */
	public void deleteOneExam(Exercises exercises){
		dao.deleteOneExam(exercises);
	}
	/**
	 * 试题库删除试题的同时  将相关联的课后试题、单元考试删除（修改转态为-1）
	 * @param exercises
	 */
	public void deleteOneLessonExam(Exercises exercises){
		dao.deleteOneLessonExam(exercises);
	}
	/**
	 * @param exerciseId
	 * @return
	 */
	public ExercisesCategorys get(String exerciseId){
		ExercisesCategorys exercisesCategorys =dao.get(exerciseId);
		return exercisesCategorys;
	} 
	/**
	 * 通过二级分类反查父类
	 * @param exerciseId
	 * @return
	 */
	public String getParentId(String exerciseId){
		String parentId=dao.getParentId(exerciseId);
		return parentId;
	}
	/**
	 * 
	 * @param exercises
	 */
	public void addExam(Exercises exercises){
		dao.addExam(exercises);
	}
	/**
	 * 试题列表--》修改试题
	 * @param exercises
	 * @return
	 */
	public int updateExam(Exercises exercises){
		return dao.update(exercises);
	}
	/**
	 * 通过课程名称查询课程id
	 * @param categoryName
	 * @return
	 */
	public TrainCategorys findByName(String categoryName){
		return dao.findByName(categoryName);
	}
}
