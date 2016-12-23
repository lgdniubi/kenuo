package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLessonContents;

/**
 * 课程内容DAO
 * @author kele
 * @version 2016年3月10日
 */
@MyBatisDao
public interface TrainLessonContentsDao extends CrudDao<TrainLessonContents>{
	
	/**
	 * 添加课程详细表
	 * 先添加课程内容
	 * @param trainlessonContents
	 * @return
	 */
	public int saveTrainlessonContents(TrainLessonContents trainlessonContents);
	
	/**
	 * 修改课程详细表
	 * 在把课程ID修改都课程内容中
	 * @param trainlessonContents
	 * @return
	 */
	public int updateTrainlessonContents(TrainLessonContents trainlessonContents);
	
	/**
	 * 删除课程详细表
	 * 修改课程内容status为"-1"无效
	 * @param trainlessonContents
	 * @return
	 */
	public int deleteTrainlessonContents(TrainLessonContents trainlessonContents);
	
	/**
	 * 修改课程视频缩略图
	 * 在把课程ID修改都课程内容中
	 * @param trainlessonContents
	 * @return
	 */
	public int updateCoverPic(TrainLessonContents trainlessonContents);
	
	
}
