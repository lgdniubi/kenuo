package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
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
	public void saveFzxMenu(FzxMenu fzxMenu){
		if(fzxMenu.getMenuId() == 0){
			fzxMenu.preInsert();
			dao.insert(fzxMenu);
		}else{
			fzxMenu.preUpdate();
			dao.update(fzxMenu);
		}
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
}
