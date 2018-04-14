package com.training.modules.train.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
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
	 * 查找该版本下的pc菜单
	 * @param trainModel
	 */
	public TrainModel findmodpcMenu(TrainModel trainModel);

	/**
	 * 删除以前的该版本下的pc菜单信息
	 * @param trainModel
	 */
	public void deleteModpcMenu(TrainModel trainModel);

	/**
	 * 插入新的该版本下的pc菜单
	 * @param newModel
	 */
	public void insertModpcMenu(TrainModel newModel);

	/**
	 * 查找该版本下的fzx菜单
	 * @param trainModel
	 */
	public TrainModel findmodfzxMenu(TrainModel trainModel);

	/**
	 * 删除以前的该版本下的fzx菜单信息
	 * @param trainModel
	 */
	public void deleteModfzxMenu(TrainModel trainModel);

	/**
	 * 插入新的该版本下的fzx菜单
	 * @param newModel
	 */
	public void insertModfzxMenu(TrainModel newModel);

	/**
	 * 查找该版本下的自媒体菜单
	 * @param trainModel
	 */
	public TrainModel findmodMediaMenu(TrainModel trainModel);

	/**
	 * 删除以前的该版本下的自媒体菜单信息
	 * @param trainModel
	 */
	public void deleteModMediaMenu(TrainModel trainModel);

	/**
	 * 插入新的该版本下的自媒体菜单
	 * @param newModel
	 */
	public void insertModMediaMenu(TrainModel newModel);

	
}
