package com.training.modules.train.dao;
import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.LessonAskComments;
import com.training.modules.train.entity.LessonAskContent;
import com.training.modules.train.entity.LessonAsks;

/**
 * 问答列表Dao接口
 * @author coffee
 *
 */

@MyBatisDao
public interface FaqlistDao extends CrudDao<LessonAsks>{
	/**
	 * 保存回复
	 * @param lessonAskComments
	 */
	public void addcomment(LessonAskComments lessonAskComments);
	/**
	 * 圈子管理--》删除问题  删除TRAIN_LESSON_ASKS中的问题（修改状态为-1）
	 * @param askId
	 * @return
	 */
	public int deleteOneAsk1(String askId);
	/**
	 * 圈子管理--》删除问题   关联删除TRAIN_LESSON_ASK_CONTENTS中的文本内容（修改状态为-1）
	 * @param askId
	 * @return
	 */
	public int deleteOneAsk2(String askId);
	/**
	 * 圈子管理--》删除问题   关联删除TRAIN_LESSON_ASK_COMMENTS中的视频或图片内容	（修改状态为-1）
	 * @param askId
	 * @return
	 */
	public int deleteOneAsk3(String askId);
	/**
	 * 圈子管理--》问答详情（查询文本内容）
	 * @param lessonAskContent
	 * @return
	 */
	public LessonAskContent get(LessonAskContent lessonAskContent);
	/**
	 * 圈子管理--》问答详情（askType问题类型   为2  查询图片     为3  查询视频 ）
	 * @param lessonAskContent
	 * @return
	 */
	public List<LessonAskContent> findContentList(LessonAskContent lessonAskContent);
	/**
	 * 修改lessonAsks的istop值
	 * @param lessonAsks
	 */
	public void updateIsTop(LessonAsks lessonAsks);
}
