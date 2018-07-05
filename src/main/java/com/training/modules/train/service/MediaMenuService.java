/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.BaseService;
import com.training.common.utils.CacheUtils;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.utils.LogUtils;
import com.training.modules.train.dao.MediaMenuDao;
import com.training.modules.train.entity.MediaMenu;
import com.training.modules.train.entity.MediaRole;
import com.training.modules.train.utils.MediaMenuUtils;
import com.training.modules.train.utils.TrainUserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class MediaMenuService extends BaseService implements InitializingBean {

	@Autowired
	private MediaMenuDao mediaMenuDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	// -- Menu Service --//

	public MediaMenu getMenu(String id) {
		return mediaMenuDao.get(id);
	}

	public List<MediaMenu> findAllMenu() {
		return MediaMenuUtils.getMenuList();
	}

	@Transactional(readOnly = false)
	public void saveMenu(MediaMenu menu) {

		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();

		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())) {
			menu.preInsert();
			mediaMenuDao.insert(menu);
		} else {
			menu.preUpdate();
			mediaMenuDao.update(menu);
		}

		// 更新子节点 parentIds
		MediaMenu m = new MediaMenu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<MediaMenu> list = mediaMenuDao.findByParentIdsLike(m);
		for (MediaMenu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			mediaMenuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		MediaMenuUtils.removeCache(MediaMenuUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}


	@Transactional(readOnly = false)
	public void deleteMenu(MediaMenu menu) {
		mediaMenuDao.delete(menu);
		// 清除用户菜单缓存
		MediaMenuUtils.removeCache(MediaMenuUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(MediaMenu menu) {
		mediaMenuDao.updateSort(menu);
		// 清除用户菜单缓存
		TrainUserUtils.removeCache(TrainUserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
	
	public List<MediaMenu> findAllMenuByModid(MediaRole mediaRole) {
		
		return mediaMenuDao.findAllMenuByModid(mediaRole);
	}

	/**
	 * 验证同一个父级菜单下不能有重复的名称
	 * @param name
	 * @param parentId
	 * @return
	 * @Description:
	 */
	public int checkName(String name,Integer parentId) {
		return mediaMenuDao.checkName(name,parentId);
	}
	
}