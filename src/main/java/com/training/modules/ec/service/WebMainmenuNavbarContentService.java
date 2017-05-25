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
	private WebMainmenuNavbarContentDao webMainmenuNavbarContentdao;
	
	/**
	 * 根据条件查询中部导航栏内容数据
	 * @param webMainmenuNavbarContent
	 * @return
	 */
	public WebMainmenuNavbarContent getWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		return webMainmenuNavbarContentdao.getWebMainmenuNavbarContentById(webMainmenuNavbarContent);
	}

	/**
	 * 添加
	 * @param webMainmenuNavbarContent
	 */
	public void saveWebMainmenuNavbarContent(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		webMainmenuNavbarContentdao.insert(webMainmenuNavbarContent);
	}
	
	/**
	 * 根据ID修改中部导航栏内容数据
	 * @param webMainmenuNavbarContent
	 * @return 
	 */
	public int update(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		return webMainmenuNavbarContentdao.update(webMainmenuNavbarContent);
	}
	
	/**
	 * 删除
	 * @param webMainmenuNavbarContent
	 */
	public void deleteWebMainmenuNavbarContentById(WebMainmenuNavbarContent webMainmenuNavbarContent) {
		webMainmenuNavbarContentdao.deleteWebMainmenuNavbarContentById(webMainmenuNavbarContent);
	}

	
	
}
