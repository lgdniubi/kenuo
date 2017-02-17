package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.StatisticsCollectionExport;
import com.training.modules.train.entity.StatisticsCommentExport;
import com.training.modules.train.entity.StatisticsTotalExport;
import com.training.modules.train.entity.StatisticsUnitExport;
import com.training.modules.train.entity.StatisticsUnitTotalExport;
import com.training.modules.train.entity.TrainLessons;

/**
 * 课程DAO
 * @author kele
 * @version 2016年3月10日
 */
@MyBatisDao
public interface TrainLessonsDao extends CrudDao<TrainLessons>{
	
	/**
	 * 删除方法 
	 * 只是表中修改status状态
	 * @param trainLessons
	 * @return
	 */
	public int deleteCourseForUpdate(TrainLessons trainLessons);
	
	/**
	 * 公共课程列表-查询
	 * @param trainLessons
	 * @return
	 */
	public List<TrainLessons> commoncourselist(TrainLessons trainLessons);
	
	/**
	 * 修改课程状态
	 * @param trainLessons
	 */
	public void updateIsShow(TrainLessons trainLessons);
	
	/**
	 * 统计总揽
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsTotalExport> totalExport(TrainLessons trainLessons);
	
	/**
	 * 收藏统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsCollectionExport> collectionExport(TrainLessons trainLessons);
	/**
	 * 评论统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsCommentExport> commentexport(TrainLessons trainLessons);
	/**
	 * 单元测试
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsUnitExport> unitexport(TrainLessons trainLessons);
	
	/**
	 * 单元测试统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsUnitTotalExport> unitTotalExport(TrainLessons trainLessons);
	
}
