package com.training.modules.train.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.CacheUtils;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.entity.Menu;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.LogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.FzxMenuDao;
import com.training.modules.train.entity.FzxMenu;


/**
 * 妃子校菜单service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class FzxMenuService extends CrudService<FzxMenuDao,FzxMenu>{
	
	@Autowired
	private FzxMenuDao fzxMenuDao;
	
	/**
	 * 妃子校菜单list
	 * @param page
	 * @param fzxMenu
	 * @return
	 */
	public Page<FzxMenu> findList(Page<FzxMenu> page, FzxMenu fzxMenu) {
		// 设置分页参数
		fzxMenu.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(fzxMenu));
		return page;
	}
	/**
	 * 查询所有菜单(未删除、显示)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<FzxMenu> findAllList(){
		return dao.findAllList();
	}
	/**
	 * 保存妃子校菜单
	 * @param fzxMenu
	 */
	@Transactional(readOnly = false)
	public void saveFzxMenu(FzxMenu fzxMenu){
		// 获取父节点实体,通过父id查询数据库得到父节点
		String fzxParenId = fzxMenu.getParent().getMenuId().toString();
		if (fzxMenuDao.get(fzxParenId) != null) {
			fzxMenu.setParent(fzxMenuDao.get(fzxParenId));
		}
		// 获取修改前的parentIds，用于更新子节点的parentIds
		//String oldParentIds = fzxMenu.getParentIds();

		// 设置新的父节点串
		fzxMenu.setParentIds(fzxMenu.getParent().getParentIds() + "," +fzxMenu.getParent().getMenuId());

		// 保存或更新实体
		if (fzxMenu.getMenuId() == null) {
			//没有菜单id说明为添加功能，id为自动增长，给出添加级修改人和时间
			//fzxMenu.preInsert();
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				fzxMenu.setUpdateBy(user);
				fzxMenu.setCreateBy(user);
				fzxMenu.setCreateDate(new Date());
			}
			fzxMenuDao.insert(fzxMenu);
		} else {
			fzxMenu.preUpdate();
			//更新时在此处做出判断，根据要更行的数据id查询数据库，比较这条数据
			FzxMenu m = fzxMenuDao.get(fzxMenu.getMenuId().toString());
			if (fzxMenu.getEnname().equals(m.getEnname())) {
				//不修改enname
				fzxMenuDao.updateNotEnname(fzxMenu);
			}else{
				fzxMenuDao.update(fzxMenu);
			}
		}

		// 更新子节点 parentIds
		/*FzxMenu m = new FzxMenu();
		m.setParentIds("%," + fzxMenu.getMenuId() + ",%");
		List<FzxMenu> list = fzxMenuDao.findByParentIdsLike(m);
		for (FzxMenu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, fzxMenu.getParentIds()));
			fzxMenuDao.updateParentIds(e);
		}*/
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
	/**
	 * 删除角色时 关联删除角色用户表
	 * @param roleId
	 */
	public void deleteRoleMenu(int menuId){
		dao.deleteRoleMenu(menuId);
	}
	/**
	 * 查询所有拥有该菜单的用户
	 * @param menuId
	 * @return
	 */
	public List<User> findUserByMenu(int menuId){
		return dao.findUserByMenu(menuId);
	}
	/**
	 * 验证英文名称是否有效
	 * @param enname
	 * @return
	 */
	public int checkEnname(String enname){
		return dao.checkEnname(enname);
	}
	public List<FzxMenu> findAllMenu() {
		return dao.findAllList(new FzxMenu());
	}
	/**
	 * 
	 * @Title: getFzxMenu
	 * @Description: TODO 根据id查询菜单
	 * @param menuId
	 * @return:
	 * @return: FzxMenu
	 * @throws
	 * 2017年10月17日
	 */
	public FzxMenu getFzxMenu(Integer menuId) {
		return dao.get(menuId.toString());
	}
	
	/**
	 * 
	 * @Title: updateMenuSort
	 * @Description: TODO 批量修改菜单排序
	 * @param menuIds
	 * @param sorts:
	 * @return: void
	 * @throws
	 * 2017年10月18日
	 */
	public void updateMenuSort(Integer[] menuIds, Integer[] sorts) {
		for (int i = 0; i < menuIds.length; i++) {
			FzxMenu fzxMenu = new FzxMenu(menuIds[i]);
    		fzxMenu.setSort(sorts[i]);
    		fzxMenuDao.updateMenuSort(fzxMenu);
		}
		
	}
	
}
