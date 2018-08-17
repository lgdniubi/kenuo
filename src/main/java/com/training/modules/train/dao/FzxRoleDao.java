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
	/**
	 * 查询角色下所有用户
	 * @param roleId
	 * @return
	 */
	public List<User> findRoleUserAllList(int roleId);
	
	/**
	 * 验证英文名称是否有效
	 * @param modeid
	 * @return
	 */
	public int checkEnname(Integer modeid);

	/**
	 * 
	 * @Title: findByRoleId
	 * @Description: TODO 根据角色id查询
	 * @param rId:
	 * @return: void
	 * @throws
	 * 2017年10月24日
	 */
	public FzxRole findByRoleId(Integer rId);

	/**
	 * 
	 * @Title: findFzxRoleByUserId
	 * @Description: TODO 查询当前 用户下的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<FzxRole> findFzxRoleByUserId(User user);

	public int checkName(@Param("modeid")Integer modeid, @Param("name")String name);
	//设置其他默认的为非默认
	public void setNotDefault(int modeid);
	//根据roleid设置该角色默认
	public void setDefault(int roleId);
	//根据版本id和ename=sjgly查找超级管理员角色
	public FzxRole getFzxRoleByModAndEname(@Param("modid")String modid,@Param("modType")String modType);

	public void insertUserRoleForRoleId(@Param("menuid")Integer menuid, @Param("roleids")List<Integer> roleids);
	//找出改版本的超管角色id
	public List<Integer> findFzxRoleByModId(String modId);

	public void deleteRoleMenuForRoleId(@Param("oldMenuid")Integer oldMenuid, @Param("modId")Integer modId);

	/**
	 * 根据商家id找出商家管理员角色id
	 * @param franchid
	 * @return
	 */
	public int findFzxSuperRoleId(String franchid);
}
