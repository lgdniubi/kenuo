package com.training.modules.train.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainMenuDao;
import com.training.modules.train.dao.TrainModelDao;
import com.training.modules.train.entity.PCMenu;
import com.training.modules.train.entity.TrainModel;

/**
 * 版本管理Service
 * @author 
 * @version 2018年3月14日
 */
@Service
@Transactional(readOnly = false)
public class TrainModelService extends CrudService<TrainModelDao,TrainModel> {

	@Autowired
	private TrainModelDao trainModelDao;
	@Autowired
	private TrainMenuDao trainMenuDao;
	
	/**
	 * 根据版本英文名称查询是否存在
	 * @param modEname
	 * @return
	 */
	public int findByModEname(String modEname) {
		return dao.findByModEname(modEname);
	}
	
	/**
	 * 保存版本，如果新增就保存，修改就更新
	 * @param trainModel
	 */
	public void saveModel(TrainModel trainModel) {
		if (StringUtils.isEmpty(trainModel.getId())) {
			this.save(trainModel);
		}else{
			trainModel.preUpdate();
			trainModelDao.editTrainModel(trainModel);
		}
		
	}

	/**
	 * 根据id查询单条版本记录
	 * @param trainModel
	 * @return
	 */
	public TrainModel getTrainModel(TrainModel trainModel) {
		return trainModelDao.getTrainModel(trainModel);
	}

	/**
	 * 查找所有PC端的菜单
	 */
	public List<PCMenu> findAllpcMenu() {
		return trainMenuDao.findAllList(new PCMenu());
	}

	/**
	 * 查找该版本下的菜单
	 * @param trainModel
	 */
	public TrainModel findmodpcMenu(TrainModel trainModel) {
		return dao.findmodpcMenu(trainModel);
	}

	/**
	 * 保存pc版本菜单
	 * @param trainModel
	 */
	public void saveModpcMenu(TrainModel trainModel) {
		TrainModel newModel = new TrainModel();
		dao.deleteModpcMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModpcMenu(newModel);
	        }
		}
		
	}

	/**
	 * 查找该版本下的fzx菜单
	 * @param trainModel
	 */
	public TrainModel findmodfzxMenu(TrainModel trainModel) {
		return dao.findmodfzxMenu(trainModel);
	}

	/**
	 * 保存fzx版本菜单
	 * @param trainModel
	 */
	public void saveModfzxMenu(TrainModel trainModel) {
		TrainModel newModel = new TrainModel();
		dao.deleteModfzxMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModfzxMenu(newModel);
	        }
		}
	}

	/**
	 * 查找该版本下的自媒体菜单
	 * @param trainModel
	 */
	public TrainModel findmodMediaMenu(TrainModel trainModel) {
		return  dao.findmodMediaMenu(trainModel);
	}
	/**
	 * 保存自媒体版本菜单
	 * @param trainModel
	 */
	public void saveModMediaMenu(TrainModel trainModel) {
		TrainModel newModel = new TrainModel();
		dao.deleteModMediaMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModMediaMenu(newModel);
	        }
		}
	}
	
}
