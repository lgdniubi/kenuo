package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.WebMainmenuNavbarContentDao;
import com.training.modules.ec.entity.WebMainmenuNavbarContent;

/**
 * web_mainmenu_navbar_content图的Service
 * @author 土豆  2017.5.4
 *
 */
@Service
@Transactional(readOnly = false)
public class WebMainmenuNavbarContentService extends CrudService<WebMainmenuNavbarContentDao, WebMainmenuNavbarContent>{

	@Autowired
	private WebMainmenuNavbarContentDao webMainmenuNavbarContentDao;
	
	/**
	 * 根据条件查询主菜单导航栏内容数据
	 * @param webMainmenuNavbarContent
	 * @return
	 */
	public WebMainmenuNavbarContent getWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		return webMainmenuNavbarContentDao.getWebMainmenuNavbarContentById(webMainmenuNavbarContent);
	}

	/**
	 * 添加
	 * @param webMainmenuNavbarContent
	 */
	public void saveWebMainmenuNavbarContent(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		webMainmenuNavbarContentDao.insert(webMainmenuNavbarContent);
	}
	
	/**
	 * 根据ID修改主菜单导航栏内容数据
	 * @param webMainmenuNavbarContent
	 * @return 
	 */
	public int update(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		return webMainmenuNavbarContentDao.update(webMainmenuNavbarContent);
	}
	
	/**
	 * 删除
	 * @param webMainmenuNavbarContent
	 */
	public void deleteWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		webMainmenuNavbarContentDao.deleteWebMainmenuNavbarContentById(webMainmenuNavbarContent);
	}

	
	
}
