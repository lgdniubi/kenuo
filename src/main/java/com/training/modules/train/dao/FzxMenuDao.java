package com.training.modules.train.dao;


import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.FzxMenu;

/**
 * 妃子校菜单Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface FzxMenuDao extends CrudDao<FzxMenu>{
	
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteRoleMenu(int menuId);
}
