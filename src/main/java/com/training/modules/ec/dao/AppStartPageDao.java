package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.AppStartPage;

/**
 * 启动页广告图接口
 * @author 土豆  2017.6.6
 *
 */

@MyBatisDao
public interface AppStartPageDao extends CrudDao<AppStartPage>{

	/**
	 * 根据ID查询数据
	 * @param appStartPage
	 * @return
	 */
	public AppStartPage getAppStartPageById(AppStartPage appStartPage);

	/**
	 * 修改全部为下架
	 */
	public void changAllIsShowByType();

	/**
	 * 根据ID修改是否上架
	 * @param appStartPage
	 */
	public void changIsShowByType(AppStartPage appStartPage);

	/**
	 * 查询最近修改的数据
	 * @return
	 */
	public AppStartPage selectIdByType();

	
}
