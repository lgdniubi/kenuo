package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.WebMainmenuNavbarContentDao;
import com.training.modules.ec.dao.WebMainmenuNavbarDao;
import com.training.modules.ec.entity.WebMainmenuNavbar;

/**
 * 主菜单导航栏的Service
 * @author 土豆  2017.5.4
 *
 */
@Service
@Transactional(readOnly = false)
public class WebMainmenuNavbarService extends CrudService<WebMainmenuNavbarDao, WebMainmenuNavbar>{
	
	@Autowired
	private WebMainmenuNavbarDao webMainmenuNavbarDao;
	@Autowired
	private WebMainmenuNavbarContentDao webMainmenuNavbarContentDao;
	
	/**
	 * 分页查询主菜单导航栏图
	 */
	public Page<WebMainmenuNavbar> findPage(Page<WebMainmenuNavbar> page,WebMainmenuNavbar webMainmenuNavbar){
		webMainmenuNavbar.setPage(page);
		page.setList(dao.findList(webMainmenuNavbar));
		return page;
	}

	/**
	 * 根据主菜单导航栏Id查询单个背景图详情
	 * @param bannerId
	 * @return
	 */
	public WebMainmenuNavbar getWebMainmenuNavbar(int webMainmenuNavbarId){
		return webMainmenuNavbarDao.getWebMainmenuNavbar(webMainmenuNavbarId);
	}
	
	/**
	 * 保存
	 */
	public void save(WebMainmenuNavbar webMainmenuNavbar){
		dao.insert(webMainmenuNavbar);
	}
	
	/**
	 * 修改
	 * @param banner
	 */
	public void update(WebMainmenuNavbar webMainmenuNavbar){
		//根据WebMainmenuNavbarID查询数据
		WebMainmenuNavbar wMN = webMainmenuNavbarDao.getWebMainmenuNavbar(webMainmenuNavbar.getWebMainmenuNavbarId());
		//确认位置类型是否修改
		if(webMainmenuNavbar.getType().equals(wMN.getType())){
			dao.update(webMainmenuNavbar);
		}else{
			//当位置类型发生改变,判断是否为显示
			if(webMainmenuNavbar.getIsShou().equals("1")){//不显示状态
				dao.update(webMainmenuNavbar);
			}else{
				//显示状态,先修改为不显示.
				webMainmenuNavbar.setIsShou("1");
				dao.update(webMainmenuNavbar);
				//根据之前的类型,查询出该类型中最近修改过的一条数据,然后修改为显示
				int id = webMainmenuNavbarDao.selectIdByType(wMN);
				WebMainmenuNavbar webMN = webMainmenuNavbarDao.getWebMainmenuNavbar(id);
				webMN.setIsShou("0");
				webMainmenuNavbarDao.changIsShowByType(webMN);
			}
		}
		
	}

	/**
	 * 删除
	 * @param webMainmenuNavbar
	 */
	public void deleteWebMainmenuNavbarById(WebMainmenuNavbar webMainmenuNavbar) {
		webMainmenuNavbarDao.updateWebMainmenuNavbarById(webMainmenuNavbar);//逻辑删除数据库中的数据
		//物理删除mtmy_web_mainmenu_navbar_content中的数据
		webMainmenuNavbarContentDao.deleteWebMainmenuNavbarContentBymainmenuId(webMainmenuNavbar.getWebMainmenuNavbarId());
	}

	/**
	 * 根据当前type,修改全部为不显示
	 * @param webMainmenuNavbar
	 */
	public void changAllIsShowByType(WebMainmenuNavbar webMainmenuNavbar) {
		dao.changAllIsShowByType(webMainmenuNavbar);
	}
	/**
	 * 根据当前type,修改是否显示
	 * @param webMainmenuNavbar
	 */
	public void changIsShowByType(WebMainmenuNavbar webMainmenuNavbar) {
		dao.changIsShowByType(webMainmenuNavbar);
	}

	/**
	 * 根据type类型,查询出最近修改过的id
	 * @param webMainmenuNavbar
	 * @return
	 */
	public int selectIdByType(WebMainmenuNavbar webMainmenuNavbar) {
		return dao.selectIdByType(webMainmenuNavbar);
	}
	
}
