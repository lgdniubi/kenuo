package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.TabBackgroundDao;
import com.training.modules.ec.entity.TabBackground;

/**
 * 背景图Service
 * @author 小叶  2016.12.8
 *
 */
@Service
@Transactional(readOnly = false)
public class TabBackgroundService extends CrudService<TabBackgroundDao, TabBackground>{
	
	/**
	 * 分页查询TabBackground背景图
	 */
	public Page<TabBackground> findPage(Page<TabBackground> page,TabBackground tabBackground){
		tabBackground.setPage(page);
		page.setList(dao.findList(tabBackground));
		return page;
	}
	
	/**
	 * 查询单个背景图详情
	 * @param bannerId
	 * @return
	 */
	public TabBackground getTabBackground(int tabBackgroundId){
		return dao.getTabBackground(tabBackgroundId);
	}
	
	/**
	 * 保存
	 */
	public void save(TabBackground tabBackground){
		dao.insert(tabBackground);
	}
	/**
	 * 修改
	 * @param 
	 */
	public void update(TabBackground tabBackground){
		dao.update(tabBackground);
	}
	
	/**
	 * 删除
	 * @param tabBackground
	 */
	public void deleteTabBackground(TabBackground tabBackground){
		dao.deleteTabBackground(tabBackground);
	}
	
	/**
	 * 修改某一组的状态
	 * @param tabBackground
	 */

	public void changIsShow(TabBackground tabBackground){
		dao.changIsShow(tabBackground);
	}
	
	/**
	 * 修改当前页面上所有组的状态
	 */
	public void changAllIsShow(){
		dao.changAllIsShow();
	}
	
	/**
	 * 查询出最近更新的那一组的id
	 */
	public int selectIdByUpdateDate(){
		return dao.selectIdByUpdateDate();
	}
}
