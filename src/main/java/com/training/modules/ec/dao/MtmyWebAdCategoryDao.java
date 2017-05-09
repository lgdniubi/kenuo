package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyWebAdCategory;

/**
 * 广告图分类Dao
 * @author xiaoye  2017年5月4日
 *
 */
@MyBatisDao
public interface MtmyWebAdCategoryDao extends CrudDao<MtmyWebAdCategory>{
	  
	/**
	 * 根据mtmyWebAdCategoryId获取相应的mtmyWebAdCategory
	 * @param mtmyWebAdCategoryId
	 * @return
	 */
	public MtmyWebAdCategory getMtmyWebAdCategory(int mtmyWebAdCategoryId);
	
	/**
	 * 插入广告图分类
	 * @param mtmyWebAdCategory
	 */
	public void insertMtmyWebAdCategory(MtmyWebAdCategory mtmyWebAdCategory);
	
	/**
	 * 修改广告图分类
	 * @param mtmyWebAdCategory
	 */
	public void updateMtmyWebAdCategory(MtmyWebAdCategory mtmyWebAdCategory);
	
	/**
	 * 逻辑删除广告图分类
	 * @param mtmyWebAdCategoryId
	 */
	public void deleteCategory(int mtmyWebAdCategoryId);
	
	/**
	 * 根据parentId查找相应的子类
	 * @param mtmyWebAdCategoryId
	 * @return
	 */
	public List<MtmyWebAdCategory> findByPidforChild(int mtmyWebAdCategoryId);
	
	/**
	 * 更改广告图分类的状态
	 * @param mtmyWebAdCategory
	 */
	public void updateIsShow(MtmyWebAdCategory mtmyWebAdCategory);
}
