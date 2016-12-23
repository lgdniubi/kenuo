package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.ExamBankDao;
import com.training.modules.train.entity.ExamLessionMapping;
import com.training.modules.train.entity.ExercisesCategorys;
import com.training.modules.train.entity.TrainCategorys;


/**
 * 课后练习题service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ExamBankService extends CrudService<ExamBankDao, ExamLessionMapping>{
	/**
	 * 保存课后练习题和单元测试
	 * @param examLessionMapping
	 */
	public void addExamLesson(ExamLessionMapping examLessionMapping){
		dao.addExamLesson(examLessionMapping);
	}
	/**
	 * 查看所有课后练习题和单元测试
	 * @param examLession
	 * @return
	 */
	public List<ExamLessionMapping> lookAll(ExamLessionMapping examLession){
		List<ExamLessionMapping> examLessionMapping=dao.lookAll(examLession);
		return examLessionMapping;
	}
	/**
	 * 保存课后练习题和单元测试前先删除所有（修改状态为-1）--》再全部重新添加
	 * @param examLessionMapping
	 */
	public void deleteAll(ExamLessionMapping examLessionMapping){
		dao.deleteAll(examLessionMapping);
	}
	/**
	 * 通过课程id查询其所在分类
	 * @param lessonId
	 * @return
	 */
	public TrainCategorys findByLessonid(String lessonId){
		return dao.findByLessonid(lessonId);
	}
	/**
	 * 通过子分类ziCategoryId查询其所在分类
	 * @param ziCategoryId
	 * @return
	 */
	public TrainCategorys findByziCategoryId(String ziCategoryId){
		return dao.findByziCategoryId(ziCategoryId);
	}
	/**
	 * 通过试题分类查询试题
	 * @param exercisesCategorys
	 * @return
	 */
	public List<ExercisesCategorys> findByExerciseType(ExercisesCategorys exercisesCategorys){
		return dao.findByExerciseType(exercisesCategorys);
	}
	/**
	 * 异步加载当前分类下的试题数量
	 * @param exercisesCategorys
	 * @return
	 */
	public int jiaZaiNum(ExercisesCategorys exercisesCategorys){
		return dao.jiaZaiNum(exercisesCategorys);
	}
}
