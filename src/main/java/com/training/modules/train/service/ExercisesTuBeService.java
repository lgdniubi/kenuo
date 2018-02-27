package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.ExercisesTuBeDao;
import com.training.modules.train.entity.ExercisesCategorys;

/**
 * 
 * @className ExercisesTuBeService
 * @description TODO 试题管理service
 * @author chenbing
 * @date 2018年1月24日 兵子
 *
 *
 */
@Service
@Transactional(readOnly = false)
public class ExercisesTuBeService extends CrudService<ExercisesTuBeDao,ExercisesCategorys>{
	
	@Autowired
	private ExercisesTuBeDao eTuBeDao;
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

	/**
	 * 
	 * @Title: deleteExercises
	 * @Description: TODO 删除试题及批量删除
	 * @throws
	 * 2018年1月25日 兵子
	 */
	public int deleteExercises(String exerciseIds, ExercisesCategorys exercisesCategorys) {
		int num = 0;
		String[] exerciseId = exerciseIds.split(",");
		for (String exId : exerciseId) {
			exercisesCategorys.setExerciseId(exId);
			num = eTuBeDao.deleteExercises(exercisesCategorys);
			num += num;
		}
		return num;
	}
}
