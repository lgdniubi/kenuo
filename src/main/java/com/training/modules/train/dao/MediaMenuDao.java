/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.MediaMenu;
import com.training.modules.train.entity.MediaRole;

/**
 * 菜单DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface MediaMenuDao extends CrudDao<MediaMenu> {

	public List<MediaMenu> findByParentIdsLike(MediaMenu menu);

	public List<MediaMenu> findByUserId(MediaMenu menu);
	
	public int updateParentIds(MediaMenu menu);
	
	public int updateSort(MediaMenu menu);
	
	public List<MediaMenu> findByPidforChild(String id);

	public List<MediaMenu> findAllMenuByModid(MediaRole mediaRole);
	
}
