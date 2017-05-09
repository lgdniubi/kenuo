package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.WebMainmenuNavbarContent;

/**
 * 主菜单导航栏内容接口
 * @author 土豆  2017.5.5
 *
 */

@MyBatisDao
public interface WebMainmenuNavbarContentDao extends CrudDao<WebMainmenuNavbarContent>{

	/**
	 * 根据mainmenu_id物理删除数据
	 * @param webMainmenuNavbarId
	 */
	public void deleteWebMainmenuNavbarContentBymainmenuId(int webMainmenuNavbarId);

	/**
	 * 根据条件查询主菜单导航栏内容数据
	 * @param webMainmenuNavbarContent
	 * @return
	 */
	public WebMainmenuNavbarContent getWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent);

	/**
	 * 根据ID删除主菜单导航栏内容数据(物理删除)
	 * @param webMainmenuNavbarContent
	 */
	public void deleteWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent);


}
