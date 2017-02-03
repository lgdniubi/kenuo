package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.DateUtils;
import com.training.modules.train.dao.FaqlistDao;
import com.training.modules.train.entity.LessonAskComments;
import com.training.modules.train.entity.LessonAskContent;
import com.training.modules.train.entity.LessonAsks;


/**
 * 问答列表service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class FaqlistService extends CrudService<FaqlistDao,LessonAsks>{
	/**
	 * 问答列表查询所有问题
	 */
	public Page<LessonAsks> findPage(Page<LessonAsks> page,LessonAsks lessonAsks){
		// 设置默认时间范围，默认当前月
		if (lessonAsks.getBeginDate() == null){
			lessonAsks.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (lessonAsks.getEndDate() == null){
			lessonAsks.setEndDate(DateUtils.addMonths(lessonAsks.getBeginDate(), 1));
		}
		//lessonAsks.setPage(page);
		//page.setList(dao.findList(lessonAsks));
		return super.findPage(page, lessonAsks);
	}
	/**
	 * 保存回复
	 * @param lessonAskComments
	 */
	public void addcomment(LessonAskComments lessonAskComments){
		dao.addcomment(lessonAskComments);
	}
	/**
	 * 圈子管理--》删除问题  删除TRAIN_LESSON_ASKS中的问题（修改状态为-1）
	 * @param askId
	 */
	public void deleteOneAsk1(String askId){
		dao.deleteOneAsk1(askId);
	}
	/**
	 * 圈子管理--》删除问题   关联删除TRAIN_LESSON_ASK_CONTENTS中的文本内容（修改状态为-1）
	 * @param askId
	 */
	public void deleteOneAsk2(String askId){
		dao.deleteOneAsk2(askId);
	}
	/**
	 * 圈子管理--》删除问题   关联删除TRAIN_LESSON_ASK_COMMENTS中的视频或图片内容	（修改状态为-1）
	 * @param askId
	 */
	public void deleteOneAsk3(String askId){
		dao.deleteOneAsk3(askId);
	}
	/**
	 * 圈子管理--》问答详情（查询文本内容）
	 * @param lessonAskContent
	 * @return
	 */
	public LessonAskContent get(LessonAskContent lessonAskContent){
		LessonAskContent lessonAskContents=dao.get(lessonAskContent);
		return lessonAskContents;
	}
	/**
	 * 圈子管理--》问答详情（askType问题类型   为2  查询图片     为3  查询视频 ）
	 * @param lessonAskContent
	 * @return
	 */
	public List<LessonAskContent> findContentList(LessonAskContent lessonAskContent){
		List<LessonAskContent> a=dao.findContentList(lessonAskContent);
		return a;
	}
}
