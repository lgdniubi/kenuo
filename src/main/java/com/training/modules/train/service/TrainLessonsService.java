package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLessonsDao;
import com.training.modules.train.entity.TrainLessons;

/**
 * 课程Service
 * @author kele
 * @version 2016年3月10日
 */
@Service
@Transactional(readOnly = false)
public class TrainLessonsService extends CrudService<TrainLessonsDao,TrainLessons> {
	
	
	/**
	 * 分页展示课程列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<TrainLessons> find(Page<TrainLessons> page, TrainLessons trainLessons) {
		trainLessons.setPage(page);
		page.setList(dao.findList(trainLessons));
		return page;
	} 
	
	/**
	 * 删除方法 
	 * 只是表中修改status状态
	 * @param trainLessons
	 */
	public void deleteCourseForUpdate(TrainLessons trainLessons){
		dao.deleteCourseForUpdate(trainLessons);
	}
	
	/**
	 * 修改方法
	 * @param trainLessons 课程对象
	 * @return
	 */
	public int updatecourse(TrainLessons trainLessons){
		return dao.update(trainLessons);
	}
	
	/**
	 * 公共课程列表-分页查询
	 * @param trainLessons
	 * @return
	 */
	public Page<TrainLessons> commoncourselist(Page<TrainLessons> page, TrainLessons trainLessons){
		trainLessons.setPage(page);
		page.setList(dao.commoncourselist(trainLessons));
		return page;
	}
}
