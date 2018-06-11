package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.MediaRoleDao;
import com.training.modules.train.entity.MediaRole;


/**
 * 自媒体角色service
 * @author jinf
 *
 */
@Service
@Transactional(readOnly = false)
public class MediaRoleService extends CrudService<MediaRoleDao,MediaRole>{
	
	@Autowired
	private MediaRoleDao mediaRoleDao;
	
	/**
	 * 角色list
	 * @param page
	 * @param MediaRole
	 * @return
	 */
	public Page<MediaRole> findList(Page<MediaRole> page, MediaRole mediaRole) {
		// 设置分页参数
		mediaRole.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(mediaRole));
		return page;
	}
	
	/**
	 * 保存角色
	 * @param mediaRole
	 */
	public void savemediaRole(MediaRole mediaRole){
		if("1".equals(mediaRole.getType())){	//如果是管理员，发布平台数据清空
			mediaRole.setPublictoArr(null);
		}
		if(mediaRole.getRoleId() == 0){
			mediaRole.preInsert();
			mediaRole.setFranchiseeid(1);	//默认商家id是平台公共的角色
			dao.insert(mediaRole);
		}else{
			mediaRole.preUpdate();
			dao.update(mediaRole);
		}
	}
	/**
	 * 查询角色下所有菜单
	 * @param role
	 * @return
	 */
	public MediaRole findRoleMenu(MediaRole mediaRole){
		return dao.findRoleMenu(mediaRole);
	}
	
	/**
	 * 保存角色菜单
	 * @param mediaRole
	 * @return
	 */
	public void saveRoleMenu(MediaRole mediaRole){
		MediaRole newmediaRole = new MediaRole();
		int roleId = mediaRole.getRoleId();
		dao.deleteRoleMenu(mediaRole);
		if(!mediaRole.getMenuIds().isEmpty()){
	        String[] ids = mediaRole.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	            newmediaRole.setRoleId(roleId);
	            newmediaRole.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertRoleMenu(newmediaRole);
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
	 * @Title: findmediaRoleByUserId
	 * @Description: TODO 获取用户的所有角色
	 * @param user
	 * @return:
	 * @return: List<User>
	 * @throws
	 * 2017年10月27日
	 */
	public List<MediaRole> findmediaRoleByUserId(User user) {
		return mediaRoleDao.findMediaRoleByUserId(user);
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
	 * @param modid
	 * @return
	 */
	public MediaRole getMediaRoleByModAndEname(String modid) {
		return dao.getMediaRoleByModAndEname(modid);
	}

	public void insertUserRole(String userid, int roleId) {
		dao.insertUserRole(userid,roleId);
	}

	public void deleteUserRole(String userid) {
		dao.deleteUserRole(userid);
	}

	public void insertUserRoleForRoleId(Integer menuid, List<Integer> mdRoleids) {
		dao.insertUserRoleForRoleId(menuid,mdRoleids);
	}

	public List<Integer> findMediaRoleByModId(String modId) {
		return dao.findMediaRoleByModId(modId);
	}

	public void deleteRoleMenuForRoleId(Integer oldMenuid) {
		dao.deleteRoleMenuForRoleId(oldMenuid);
	}
}
