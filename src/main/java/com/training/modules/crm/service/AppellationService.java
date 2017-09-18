package com.training.modules.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.crm.dao.AppellationDao;
import com.training.modules.crm.entity.Appellation;

/**
 * 称谓标签Service
 * @author xiaoye   2017年9月15日
 *
 */
@Service
@Transactional(readOnly = false)
public class AppellationService extends CrudService<AppellationDao, Appellation>{
	
	@Autowired
	private AppellationDao appellationDao;
	
	/**
	 * 根据id查找称谓标签
	 * @param appellationId
	 * @return
	 */
	public Appellation getAppellation(int appellationId){
		return appellationDao.getAppellation(appellationId);
	}
	
	/**
	 * 新增称谓标签
	 * @param appellation
	 */
	public void insertAppellation(Appellation appellation){
		appellationDao.insertAppellation(appellation);
	}
	
	/**
	 * 修改称谓标签
	 * @param appellation
	 */
	public void updateAppellation(Appellation appellation){
		appellationDao.updateAppellation(appellation);
	}
	
	/**
	 * 逻辑删除称谓
	 */
	public void delAppellation(int appellationId){
		appellationDao.delAppellation(appellationId);
	}
}
