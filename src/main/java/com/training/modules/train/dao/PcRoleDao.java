package com.training.modules.train.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.User;
import com.training.modules.train.entity.PcRole;

/**
 * 妃子校角色Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface PcRoleDao extends CrudDao<PcRole>{
	
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public PcRole findRoleMenu(PcRole pcRole);
	
	/**
	 * 删除角色关联的菜单
	 * @param pcRole
	 */
	public void deleteRoleMenu(PcRole pcRole);
	
	/**
	 * 保存角色菜单
	 * @param pcRole
	 */
	public void insertRoleMenu(PcRole pcRole);
	
	/**
	 * 获取用户所有妃子校菜单
	 * @param id
	 * @return
	 */
//	public PcRole findUserpcRole(String id);
	
	/**
	 * 删除用户所有角色
	 * @param id
	 */
	public void deleteUserRole(String id);
	
	/**
	 * 保存用户角色 
	 * @param pcRole
	 */
	public void insertUserRole(PcRole pcRole);
	
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteUserRoleForRoleId(int roleId);
	
	/**
	 * 分配用户
	 * @param PcRole
	 * @return
	 */
//	public List<User> findRoleUser(User user);
	
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param pcRoleId
	 */
//	public void outUserInRole(@Param(value="userId")String userId,@Param(value="pcRoleId")int pcRoleId);
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
//	public PcRole findByRoleId(Integer rId);

	/**
	 * 
	 * @Title: findpcRoleByUserId
	 * @Description: TODO 查询当前 用户下的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<PcRole> findpcRoleByUserId(User user);
	
	/**
	 * 验证英文名称是否有效
	 * @param enname
	 * @return
	 */
	public int checkEnname(Integer modeid);

	/**
	 * 验证 角色名称是否重复
	 * @param name
	 * @return
	 */
	public int checkRoleName(@Param("name")String name, @Param("modeid")Integer modeid);
}
