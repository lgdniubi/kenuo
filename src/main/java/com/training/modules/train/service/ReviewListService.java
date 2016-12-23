package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.ReviewListDao;
import com.training.modules.train.entity.LessonAskComments;


/**
 * 评论详情service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ReviewListService extends CrudService<ReviewListDao,LessonAskComments>{
	/**
	 * 查询问题对应的所有评论
	 * @param page
	 * @param lessonAskComments
	 * @return
	 */
	public Page<LessonAskComments> find(Page<LessonAskComments> page,LessonAskComments lessonAskComments){
		lessonAskComments.setPage(page);
		page.setList(dao.findList(lessonAskComments));
		return page;
	}
	/**
	 * 删除单个问题评论
	 * @param lessonAskComments
	 */
	public void deleteOneComment(LessonAskComments lessonAskComments){
		dao.deleteOneComment(lessonAskComments);
	}
	/**
	 * 重定向给LessonAskComments赋值   获取问题ID
	 */
	public LessonAskComments get(String commentId){
		LessonAskComments lessonAskComments=dao.get(commentId);
		return lessonAskComments;
	}
}
