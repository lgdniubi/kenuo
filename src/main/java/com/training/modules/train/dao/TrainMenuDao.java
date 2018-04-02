/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.PCMenu;
import com.training.modules.train.entity.PcRole;

/**
 * 菜单DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface TrainMenuDao extends CrudDao<PCMenu> {

	public List<PCMenu> findByParentIdsLike(PCMenu menu);

	public List<PCMenu> findByUserId(PCMenu menu);
	
	public int updateParentIds(PCMenu menu);
	
	public int updateSort(PCMenu menu);
	
	public List<PCMenu> findByPidforChild(String id);

	public List<PCMenu> findAllMenuByModid(PcRole pcRole);
	
}
