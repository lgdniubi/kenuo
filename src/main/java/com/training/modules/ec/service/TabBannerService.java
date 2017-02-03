package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.TabBannerDao;
import com.training.modules.ec.entity.TabBanner;

/**
 * tab_banner图的Service
 * @author 小叶  2016.12.9
 *
 */
@Service
@Transactional(readOnly = false)
public class TabBannerService extends CrudService<TabBannerDao, TabBanner>{
	
	/**
	 * 分页查询tab_banner图
	 */
	public Page<TabBanner> findPage(Page<TabBanner> page,TabBanner tabBanner){
		tabBanner.setPage(page);
		page.setList(dao.findList(tabBanner));
		return page;
	}
	
	/**
	 * 查询一对tab_banner详情
	 * @param tabBannerId
	 * @return
	 */
	public TabBanner getTabBanner(int tabBannerId){
		return dao.getTabBanner(tabBannerId);
	}
	
	/**
	 * 保存
	 */
	public void save(TabBanner tabBanner){
		dao.insert(tabBanner);
	}
	
	/**
	 * 修改
	 * @param 
	 */
	public void update(TabBanner tabBanner){
		dao.update(tabBanner);
	}
	
	/**
	 * 删除某一tab_banner
	 * @param tabBanner
	 */
	public void delTabBanner(TabBanner tabBanner){
		dao.delTabBanner(tabBanner);
	}
}
