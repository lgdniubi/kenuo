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

	/**
	 * 
	 * @Title: findByPidforChild
	 * @Description: TODO  根据id加载子类
	 * @param id
	 * @return: List<FzxMenu>
	 * @throws
	 * 2017年10月16日
	 */
	public List<FzxMenu> findByPidforChild(Integer menuId);

	/**
	 * 
	 * @Title: findByParentIdsLike
	 * @Description: TODO 根据ids模糊查询所有的数据
	 * @param m
	 * @return:
	 * @return: List<FzxMenu>
	 * @throws
	 * 2017年10月17日
	 */
	public List<FzxMenu> findByParentIdsLike(FzxMenu m);

	/**
	 * @Title: updateParentIds
	 * @Description: TODO 更新parentIds
	 * @param e:
	 * @return: void
	 * @throws
	 * 2017年10月17日
	 */
	public void updateParentIds(FzxMenu e);

	/**
	 * 
	 * @Title: updateNotEnname
	 * @Description: TODO 更新菜单数据，但是不更新enname
	 * @param fzxMenu:
	 * @return: void
	 * @throws
	 * 2017年10月18日
	 */
	public void updateNotEnname(FzxMenu fzxMenu);

	/**
	 * 
	 * @Title: updateMenuSort
	 * @Description: TODO 批量修改菜单排序
	 * @param fzxMenu:
	 * @return: void
	 * @throws
	 * 2017年10月18日
	 */
	public void updateMenuSort(FzxMenu fzxMenu);

}
