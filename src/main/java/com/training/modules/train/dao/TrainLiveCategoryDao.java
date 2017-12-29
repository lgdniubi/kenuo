package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveCategory;

/**
 * 直播分类Dao
 * @author 小叶  2017年2月22日
 *
 */
@MyBatisDao
public interface TrainLiveCategoryDao extends CrudDao<TrainLiveCategory>{
	
	/**
	 * 新增直播分类
	 * @param trainLiveCategory
	 */
	public void insertCategory(TrainLiveCategory trainLiveCategory);
	
	/**
	 *  根据parentsId查找相应的子类
	 * @param id
	 * @return
	 */
	public List<TrainLiveCategory> findByPidforChild(String id);
	
	/**
	 * 修改直播分类
	 */
	public void updateCategory(TrainLiveCategory trainLiveCategory);
	
	/**
	 * 删除直播分类
	 * @param trainLiveCategory
	 */
	public void deleteCategory(TrainLiveCategory trainLiveCategory);

	/**
	 * 
	 * @Title: getCategory
	 * @Description: TODO 查询parentIds包含此id的分类
	 * @param trainLiveCategoryId
	 * @return:
	 * @return: List<TrainLiveCategory>
	 * @throws
	 * 2017年12月19日 兵子
	 */
	public List<TrainLiveCategory> getCategory(String trainLiveCategoryId);
}
