package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLessonCommentsDao;
import com.training.modules.train.entity.TrainLessonComments;

/**
 * 课程评论管理Service
 * @author kele
 * @version 2016年3月17日
 */
@Service
@Transactional(readOnly = false)
public class TrainLessonCommentsService extends CrudService<TrainLessonCommentsDao,TrainLessonComments>{
	
	/**
	 * 分页展示课程评论列表
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<TrainLessonComments> find(Page<TrainLessonComments> page, TrainLessonComments trainLessonComments) {
		trainLessonComments.setPage(page);
		page.setList(dao.findList(trainLessonComments));
		return page;
	} 
	
	/**
	 * 删除方法 
	 * 只是表中修改status状态
	 * @param trainLessonComments
	 */
	public void deleteCommentsForUpdate(TrainLessonComments trainLessonComments){
		dao.deleteCommentsForUpdate(trainLessonComments);
	}
}
