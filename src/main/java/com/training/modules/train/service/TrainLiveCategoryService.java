package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.IdGen;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainLiveCategoryDao;
import com.training.modules.train.entity.TrainLiveCategory;

/**
 * 直播分类管理Service
 * @author 小叶  2017年2月22日
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainLiveCategoryService extends CrudService<TrainLiveCategoryDao,TrainLiveCategory>{
	@Autowired
	private TrainLiveCategoryDao liveCategoryDao;
	
	/**
	 *  根据parentsId查找相应的子类
	 * @param id
	 * @return
	 */
	public List<TrainLiveCategory> findByPidforChild(String id){
		return liveCategoryDao.findByPidforChild(id);
	}
	
	/**
	 * 新增直播分类
	 * @param trainLiveCategory
	 */
	public void insertCategory(TrainLiveCategory trainLiveCategory){
		User user = UserUtils.getUser();
		trainLiveCategory.setCreateBy(user);
		trainLiveCategory.setTrainLiveCategoryId(IdGen.uuid());
		//若为一级目录，则将其paentId和parentIds设置成0
		if("".equals(trainLiveCategory.getParentId()) || trainLiveCategory.getParentId() == null){
			trainLiveCategory.setParentId("0");
			trainLiveCategory.setParentIds("0");
			trainLiveCategory.setType(1);
		}else{
			trainLiveCategory.setParentIds(liveCategoryDao.get(trainLiveCategory.getParentId()).getParentIds()+","+ trainLiveCategory.getParentId()); 
			trainLiveCategory.setType(liveCategoryDao.get(trainLiveCategory.getParentId()).getType() + 1);
		}
		liveCategoryDao.insertCategory(trainLiveCategory);
	}
	
	/**
	 * 修改直播分类
	 */
	public void updateCategory(TrainLiveCategory trainLiveCategory){
		User user = UserUtils.getUser();
		trainLiveCategory.setUpdateBy(user);
		liveCategoryDao.updateCategory(trainLiveCategory);
	}
	
	/**
	 * 删除直播分类
	 * @param trainLiveCategory
	 */
	public void deleteCategory(TrainLiveCategory trainLiveCategory){
		liveCategoryDao.deleteCategory(trainLiveCategory);
	}
	
}
