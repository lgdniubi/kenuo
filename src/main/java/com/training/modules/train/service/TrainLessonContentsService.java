package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLessonContentsDao;
import com.training.modules.train.entity.TrainLessonContents;

/**
 * 课程内容Service
 * @author kele
 * @version 2016年3月16日
 */
@Service
@Transactional(readOnly = false)
public class TrainLessonContentsService extends CrudService<TrainLessonContentsDao,TrainLessonContents>{

	/**
	 * 根据课程ID查询所有的课程内容
	 * @param trainLessonContents
	 * @return
	 */
	public List<TrainLessonContents> findlistcontents(TrainLessonContents trainLessonContents){
		return dao.findList(trainLessonContents);
	}
	/**
	 * 修改课程详细表
	 * 在把课程ID修改都课程内容中
	 * @param trainlessonContents
	 * @return
	 */
	public int updateTrainlessonContents(TrainLessonContents trainlessonContents){
		return dao.update(trainlessonContents);
	}
	
	/**
	 * 删除课程详细表
	 * 修改课程内容status为"-1"无效
	 * @param trainlessonContents
	 * @return
	 */
	public int deleteTrainlessonContents(TrainLessonContents trainlessonContents){
		return dao.deleteTrainlessonContents(trainlessonContents);
	}
	
	/**
	 * 根据contentId查询一个对象
	 * @param contentId
	 * @return
	 */
	public TrainLessonContents getTrainlessonContents(TrainLessonContents trainlessonContents){
		return dao.get(trainlessonContents);
	}
	
	/**
	 * 修改课程视频缩略图
	 * 在把课程ID修改都课程内容中
	 * @param trainlessonContents
	 * @return
	 */
	public int updateCoverPic(TrainLessonContents trainlessonContents){
		return dao.updateCoverPic(trainlessonContents);
	}
	/**
	 * 修改课程视频图片
	 * @param trainlessonContents
	 * @return
	 */
	public  int updateContentPic(TrainLessonContents trainlessonContents){
		return dao.updateCoverPic(trainlessonContents);
	}
	
	
}
