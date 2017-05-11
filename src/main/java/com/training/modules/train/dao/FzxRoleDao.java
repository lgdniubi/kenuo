package com.training.modules.train.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.User;
import com.training.modules.train.entity.FzxRole;

/**
 * 妃子校角色Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface FzxRoleDao extends CrudDao<FzxRole>{
	
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public FzxRole findRoleMenu(FzxRole fzxRole);
	
	/**
	 * 删除角色关联的菜单
	 * @param fzxRole
	 */
	public void deleteRoleMenu(FzxRole fzxRole);
	
	/**
	 * 保存角色菜单
	 * @param fzxRole
	 */
	public void insertRoleMenu(FzxRole fzxRole);
	
	/**
	 * 获取用户所有妃子校菜单
	 * @param id
	 * @return
	 */
	public FzxRole findUserFzxRole(String id);
	
	/**
	 * 删除用户所有角色
	 * @param id
	 */
	public void deleteUserRole(String id);
	
	/**
	 * 保存用户角色 
	 * @param fzxRole
	 */
	public void insertUserRole(FzxRole fzxRole);
	
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteUserRoleForRoleId(int roleId);
	
	/**
	 * 分配用户
	 * @param fzxRole
	 * @return
	 */
	public List<User> findRoleUser(User user);
	
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param fzxRoleId
	 */
	public void outUserInRole(@Param(value="userId")String userId,@Param(value="fzxRoleId")int fzxRoleId);
}
