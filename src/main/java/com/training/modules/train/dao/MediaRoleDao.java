package com.training.modules.train.dao;


import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.User;
import com.training.modules.train.entity.MediaRole;

/**
 * 自媒体角色Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface MediaRoleDao extends CrudDao<MediaRole>{
	
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public MediaRole findRoleMenu(MediaRole MediaRole);
	
	/**
	 * 删除角色关联的菜单
	 * @param MediaRole
	 */
	public void deleteRoleMenu(MediaRole MediaRole);
	
	/**
	 * 保存角色菜单
	 * @param MediaRole
	 */
	public void insertRoleMenu(MediaRole MediaRole);
	
	/**
	 * 获取用户所有自媒体菜单
	 * @param id
	 * @return
	 */
//	public MediaRole findUserMediaRole(String id);
	
	/**
	 * 删除用户所有角色
	 * @param id
	 */
	public void deleteUserRole(String id);
	
	/**
	 * 保存用户角色 
	 * @param MediaRole
	 */
	public void insertUserRole(MediaRole MediaRole);
	
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteUserRoleForRoleId(int roleId);
	
	/**
	 * 分配用户
	 * @param MediaRole
	 * @return
	 */
//	public List<User> findRoleUser(User user);
	
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param MediaRoleId
	 */
//	public void outUserInRole(@Param(value="userId")String userId,@Param(value="MediaRoleId")int MediaRoleId);
	/**
	 * 查询角色下所有用户
	 * @param roleId
	 * @return
	 */
//	public List<User> findRoleUserAllList(int roleId);
	
//	/**
//	 * 验证英文名称是否有效
//	 * @param enname
//	 * @return
//	 */
//	public int checkEnname(String enname);

	/**
	 * 
	 * @Title: findByRoleId
	 * @Description: TODO 根据角色id查询
	 * @param rId:
	 * @return: void
	 * @throws
	 * 2017年10月24日
	 */
//	public MediaRole findByRoleId(Integer rId);

	/**
	 * 
	 * @Title: findMediaRoleByUserId
	 * @Description: TODO 查询当前 用户下的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<MediaRole> findMediaRoleByUserId(User user);
	
	/**
	 * 验证英文名称是否有效
	 * @param enname
	 * @return
	 */
	public int checkEnname(Integer modeid);
}
