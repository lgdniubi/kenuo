package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.TabBanner;

/**
 * 
 * @author 小叶  2016.12.9
 *
 */
@MyBatisDao
public interface TabBannerDao extends CrudDao<TabBanner>{
	/**
	 * 查询一对tab_banner详情
	 * @param tabBannerId
	 * @return
	 */
	public TabBanner getTabBanner(int tabBannerId);
	
	/**
	 * 删除某一tab_banner
	 * @param tabBannerId
	 */
	public void delTabBanner(TabBanner tabBanner);
}
