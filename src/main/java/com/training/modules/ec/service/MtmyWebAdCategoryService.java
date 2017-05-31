package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyWebAdCategoryDao;
import com.training.modules.ec.entity.MtmyWebAdCategory;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 广告图分类Service
 * @author xiaoye  2017年5月4日
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyWebAdCategoryService extends CrudService<MtmyWebAdCategoryDao,MtmyWebAdCategory>{
	
	@Autowired
	private MtmyWebAdCategoryDao mtmyWebAdCategoryDao;
	
	/**
	 * 根据mtmyWebAdCategoryId获取相应的mtmyWebAdCategory
	 * @param mtmyWebAdCategoryId
	 * @return
	 */
	public MtmyWebAdCategory getMtmyWebAdCategory(int mtmyWebAdCategoryId){
		return mtmyWebAdCategoryDao.getMtmyWebAdCategory(mtmyWebAdCategoryId);
	}
	
	/**
	 * 插入广告图分类
	 * @param mtmyWebAdCategory
	 */
	public void insertMtmyWebAdCategory(MtmyWebAdCategory mtmyWebAdCategory){
		User user = UserUtils.getUser();
		mtmyWebAdCategory.setCreateBy(user);
		//若为一级目录，则将其paentId和parentIds设置成0
		if("".equals(mtmyWebAdCategory.getParentId()) || mtmyWebAdCategory.getParentId() == null){
			mtmyWebAdCategory.setParentId("0");
			mtmyWebAdCategory.setParentIds("0");
			mtmyWebAdCategory.setLevel(1);
			mtmyWebAdCategory.setPositionType("0");
		}else{
			mtmyWebAdCategory.setParentIds(mtmyWebAdCategoryDao.getMtmyWebAdCategory(Integer.valueOf(mtmyWebAdCategory.getParentId())).getParentIds()+","+ mtmyWebAdCategory.getParentId()); 
			mtmyWebAdCategory.setLevel(mtmyWebAdCategoryDao.getMtmyWebAdCategory(Integer.valueOf(mtmyWebAdCategory.getParentId())).getLevel() + 1);
		}
		mtmyWebAdCategoryDao.insertMtmyWebAdCategory(mtmyWebAdCategory);
	}
	
	/**
	 * 修改广告图分类
	 * @param mtmyWebAdCategory
	 */
	public void updateMtmyWebAdCategory(MtmyWebAdCategory mtmyWebAdCategory){
		if("0".equals(mtmyWebAdCategory.getParentId())){
			mtmyWebAdCategory.setPositionType("0");
		}
		User user = UserUtils.getUser();
		mtmyWebAdCategory.setUpdateBy(user);
		mtmyWebAdCategoryDao.updateMtmyWebAdCategory(mtmyWebAdCategory);
	}
	
	/**
	 * 逻辑删除广告图分类
	 * @param mtmyWebAdCategoryId
	 */
	public void deleteCategory(int mtmyWebAdCategoryId){
		mtmyWebAdCategoryDao.deleteCategory(mtmyWebAdCategoryId);
	}
	
	/**
	 * 根据parentId查找相应的子类
	 * @param mtmyWebAdCategoryId
	 * @return
	 */
	public List<MtmyWebAdCategory> findByPidforChild(int mtmyWebAdCategoryId){
		return mtmyWebAdCategoryDao.findByPidforChild(mtmyWebAdCategoryId);
	}
	
	/**
	 * 更改广告图分类的状态
	 * @param mtmyWebAdCategory
	 */
	public void updateIsShow(MtmyWebAdCategory mtmyWebAdCategory){
		mtmyWebAdCategoryDao.updateIsShow(mtmyWebAdCategory);
	}
}
