package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.FzxRoleDao;
import com.training.modules.train.entity.FzxRole;


/**
 * 妃子校角色service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class FzxRoleService extends CrudService<FzxRoleDao,FzxRole>{
	
	@Autowired
	private RedisClientTemplate redisClientTemplate; //redis缓存Service
	@Autowired
	private FzxRoleDao fzxRoleDao;
	
	/**
	 * 妃子校角色list
	 * @param page
	 * @param fzxRole
	 * @return
	 */
	public Page<FzxRole> findList(Page<FzxRole> page, FzxRole fzxRole) {
		// 设置分页参数
		fzxRole.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(fzxRole));
		return page;
	}
	
	/**
	 * 保存妃子校角色
	 * @param fzxRole
	 */
	public void saveFzxRole(FzxRole fzxRole){
		if(fzxRole.getRoleId() == 0){
			fzxRole.preInsert();
			fzxRole.setFranchiseeid(1);	//默认商家id是平台公共的角色
			fzxRole.setOfficeid("1");	//默认商家id是平台公共的角色
			dao.insert(fzxRole);
		}else{
			fzxRole.preUpdate();
			dao.update(fzxRole);
		}
	}
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public FzxRole findRoleMenu(FzxRole fzxRole){
		return dao.findRoleMenu(fzxRole);
	}
	
	/**
	 * 保存角色菜单
	 * @param fzxRole
	 * @return
	 */
	public void saveRoleMenu(FzxRole fzxRole){
		FzxRole newFzxRole = new FzxRole();
		dao.deleteRoleMenu(fzxRole);
		if(!fzxRole.getMenuIds().isEmpty()){
	        String[] ids = fzxRole.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	            newFzxRole.setRoleId(fzxRole.getRoleId());
	            newFzxRole.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertRoleMenu(newFzxRole);
	        }
		}
	}
	/**
	 * 获取用户所有角色
	 * @param id
	 * @return
	 */
	public FzxRole findUserFzxRole(String id){
		return dao.findUserFzxRole(id);
	}
	
	/**
	 * 修改用户所有角色
	 * @param id
	 */
	public void updateUserRole(String id,String oldFzxRoleIds,String fzxRoleIds){
		FzxRole newFzxRole = new FzxRole();
		if(oldFzxRoleIds == null){
			oldFzxRoleIds = "";
		}
		if(fzxRoleIds == null){
			fzxRoleIds = "";
		}
		if(!oldFzxRoleIds.equals(fzxRoleIds)){
			redisClientTemplate.del("UTOKEN_"+id);
			dao.deleteUserRole(id);
			if(!fzxRoleIds.isEmpty()){
				String[] ids = fzxRoleIds.split(",");
		        for (int i = 0; i < ids.length; i++) {
		        	newFzxRole.setId(id);
		        	newFzxRole.setRoleId(Integer.valueOf(ids[i]));
		            dao.insertUserRole(newFzxRole);
		        }
			}
		}
	}
	
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteUserRoleForRoleId(int roleId){
		dao.deleteUserRoleForRoleId(roleId);
	}
	
	/**
	 * 分配用户
	 * @param fzxRole
	 * @return
	 */
	public Page<User> findRoleUser(Page<User> page, User user){
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(dao.findRoleUser(user));
		return page;
	}
	/**
	 * 分配用户 无分页
	 * @param fzxRole
	 * @return
	 */
	public List<User> findRoleUserList(User user){
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		return dao.findRoleUser(user);
	}
	/**
	 * 查询角色下所有用户
	 * @param fzxRole
	 * @return
	 */
	public List<User> findRoleUserAllList(int roleId){
		return dao.findRoleUserAllList(roleId);
	}
	/**
	 * 从角色中移除用户
	 * @param userId
	 * @param fzxRoleId
	 */
	public void outUserInRole(String userId,int fzxRoleId){
		redisClientTemplate.del("UTOKEN_"+userId);
		dao.outUserInRole(userId, fzxRoleId);
	}
	/**
	 * 验证英文名称是否有效
	 * @param modeid
	 * @return
	 */
	public int checkEnname(Integer modeid){
		return dao.checkEnname(modeid);
	}

	/**
	 * 
	 * @Title: findFzxRoleByUserId
	 * @Description: TODO 获取用户的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<FzxRole> findFzxRoleByUserId(User user) {
		return fzxRoleDao.findFzxRoleByUserId(user);
	}

	/**
	 * 同版本下不能有多个相同的角色
	 * @param modeid
	 * @param name
	 * @return
	 * @Description:
	 */
	public int checkName(Integer modeid, String name) {
		return dao.checkName(modeid, name);
	}

}
