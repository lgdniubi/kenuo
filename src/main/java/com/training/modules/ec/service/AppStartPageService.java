package com.training.modules.ec.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.AppStartPageDao;
import com.training.modules.ec.entity.AppStartPage;

/**
 * 启动页广告图的Service
 * @author 土豆  2017.6.6
 *
 */
@Service
@Transactional(readOnly = false)
public class AppStartPageService extends CrudService<AppStartPageDao, AppStartPage>{
	
	@Autowired
	private AppStartPageDao appStartPageDao;
	
	/**
	 * 分页查询启动页广告图
	 */
	public Page<AppStartPage> findPage(Page<AppStartPage> page,AppStartPage appStartPage){
		appStartPage.setPage(page);
		page.setList(dao.findList(appStartPage));
		return page;
	}

	/**
	 * 根据ID查询数据
	 * @param appStartPage
	 * @return
	 */
	public AppStartPage getAppStartPageById(AppStartPage appStartPage) {
		return appStartPageDao.getAppStartPageById(appStartPage);
	}

	/**
	 * 修改
	 * @param appStartPage
	 */
	public void update(AppStartPage appStartPage){
		dao.update(appStartPage);
	}

	/**
	 * 修改状态
	 * @param appStartPage
	 * @return
	 */
	public Map<String, String> updateType(AppStartPage appStartPage) {
		Map<String, String> map = new HashMap<String, String>();
		appStartPageDao.changAllIsShowByType();//修改全部为下架
		
		if("1".equals(appStartPage.getIsOnSale())){//当前下架,要上架
			appStartPageDao.changIsShowByType(appStartPage);
		}
		map.put("STATUS", "OK");
		return map;
	}
}
