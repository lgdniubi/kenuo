package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.WebMainmenuNavbar;

/**
 * 主菜单导航栏接口
 * @author 土豆  2017.5.4
 *
 */

@MyBatisDao
public interface WebMainmenuNavbarDao extends CrudDao<WebMainmenuNavbar>{

	/**
	 * 根据主菜单导航栏Id查询数据
	 * @param webMainmenuNavbarId
	 * @return
	 */
	public WebMainmenuNavbar getWebMainmenuNavbar(int webMainmenuNavbarId);

	/**
	 * 逻辑删除
	 * @param webMainmenuNavbar
	 */
	public void updateWebMainmenuNavbarById(WebMainmenuNavbar webMainmenuNavbar);

	/**
	 * 根据当前type,修改全部为不显示
	 * @param webMainmenuNavbar
	 */
	public void changAllIsShowByType(WebMainmenuNavbar webMainmenuNavbar);

	/**
	 * 根据当前type,修改是否显示
	 * @param webMainmenuNavbar
	 */
	public void changIsShowByType(WebMainmenuNavbar webMainmenuNavbar);

	/**
	 * 根据type类型,查询出最近修改过的id
	 * @param webMainmenuNavbar
	 */
	public int selectIdByType(WebMainmenuNavbar webMainmenuNavbar);

}
