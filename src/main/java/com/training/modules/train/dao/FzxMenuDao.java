package com.training.modules.train.dao;


import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.User;
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
	
	/**
	 * 查询所有拥有该菜单的用户
	 * @param menuId
	 * @return
	 */
	public List<User> findUserByMenu(int menuId);
	
	/**
	 * 验证英文名称是否有效
	 * @param enname
	 * @return
	 */
	public int checkEnname(String enname);
}
