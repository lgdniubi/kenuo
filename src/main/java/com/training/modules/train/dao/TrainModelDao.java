package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.train.entity.CategoryLesson;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainModel;

/**
 * 版本管理
 * @author 
 * @version 2018年3月14日
 */
@MyBatisDao
public interface TrainModelDao extends CrudDao<TrainModel>{

	/**
	 * 根据版本英文名称查询是否存在
	 * @param modEname
	 * @return
	 */
	public int findByModEname(String modEname);

	public void editTrainModel(TrainModel trainModel);

	public TrainModel getTrainModel(TrainModel trainModel);
	
	/**
	 * 
	 * @param 
	 * @return
	 */
//	public List<TrainCategorys> findcategoryslist(TrainCategorys trainCategorys);
	
	
	
}
