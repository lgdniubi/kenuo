package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainLessonsDao;
import com.training.modules.train.entity.StatisticsCollectionExport;
import com.training.modules.train.entity.StatisticsCommentExport;
import com.training.modules.train.entity.StatisticsTotalExport;
import com.training.modules.train.entity.StatisticsUnitExport;
import com.training.modules.train.entity.StatisticsUnitTotalExport;
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
	/**
	 * 修改课程状态
	 * @param trainLessons
	 */
	public void updateIsShow(TrainLessons trainLessons){
		dao.updateIsShow(trainLessons);
	}
	
	/**
	 * 统计总揽
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsTotalExport> totalExport(TrainLessons trainLessons){
		return dao.totalExport(trainLessons);
	}
	
	/**
	 * 收藏统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsCollectionExport> collectionExport(TrainLessons trainLessons){
		return dao.collectionExport(trainLessons);
	}
	/**
	 * 评论统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsCommentExport> commentExport(TrainLessons trainLessons){
		return dao.commentexport(trainLessons);
	}
	/**
	 * 单元测试
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsUnitExport> unitExport(TrainLessons trainLessons){
		return dao.unitexport(trainLessons);
	}
	/**
	 * 单元测试统计
	 * @param trainLessons
	 * @return
	 */
	public List<StatisticsUnitTotalExport> unitTotalExport(TrainLessons trainLessons){
		return dao.unitTotalExport(trainLessons);
	}
	
}
