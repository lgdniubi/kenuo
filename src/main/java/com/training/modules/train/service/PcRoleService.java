package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.PcRoleDao;
import com.training.modules.train.entity.PcRole;


/**
 * 妃子校角色service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class PcRoleService extends CrudService<PcRoleDao,PcRole>{
	
	@Autowired
	private PcRoleDao pcRoleDao;
	
	/**
	 * 妃子校角色list
	 * @param page
	 * @param pcRole
	 * @return
	 */
	public Page<PcRole> findList(Page<PcRole> page, PcRole pcRole) {
		// 设置分页参数
		pcRole.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(pcRole));
		return page;
	}
	
	/**
	 * 保存妃子校角色
	 * @param pcRole
	 */
	public void savepcRole(PcRole pcRole){
		if(pcRole.getRoleId() == 0){
			pcRole.preInsert();
			pcRole.setFranchiseeid(1);	//默认商家id是平台公共的角色
			pcRole.setOfficeid("1");	//默认商家id是平台公共的角色
			dao.insert(pcRole);
		}else{
			pcRole.preUpdate();
			dao.update(pcRole);
		}
	}
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public PcRole findRoleMenu(PcRole pcRole){
		return dao.findRoleMenu(pcRole);
	}
	
	/**
	 * 保存角色菜单
	 * @param pcRole
	 * @return
	 */
	public void saveRoleMenu(PcRole pcRole){
		PcRole newpcRole = new PcRole();
		int roleId = pcRole.getRoleId();
		dao.deleteRoleMenu(pcRole);
		if(!pcRole.getMenuIds().isEmpty()){
	        String[] ids = pcRole.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	            newpcRole.setRoleId(roleId);
	            newpcRole.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertRoleMenu(newpcRole);
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
	 * 
	 * @Title: findpcRoleByUserId
	 * @Description: TODO 获取用户的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<PcRole> findpcRoleByUserId(User user) {
		return pcRoleDao.findpcRoleByUserId(user);
	}

	/**
	 * 验证英文名称是否有效
	 * @param enname
	 * @return
	 */
	public int checkEnname(Integer modeid){
		return dao.checkEnname(modeid);
	}

	/**
	 * 验证 角色名称是否重复
	 * @param name
	 * @return
	 */
	public int checkRoleName(String name, Integer modeid) {
		return dao.checkRoleName(name, modeid);
	}

	/**
	 * 根据版本id和ename=sjgly查找超级管理员角色
	 * @param id
	 * @return
	 */
	public PcRole getPcRoleByModAndEname(String id) {
		return dao.getPcRoleByModAndEname(id);
	}
}
