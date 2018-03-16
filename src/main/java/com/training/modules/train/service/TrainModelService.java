package com.training.modules.train.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainModelDao;
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
	
	
}
