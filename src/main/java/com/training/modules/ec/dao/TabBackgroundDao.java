package com.training.modules.ec.dao;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.TabBackground;

/**
 * TabBackground背景图接口
 * @author 小叶  2016.12.8
 *
 */
@MyBatisDao
public interface TabBackgroundDao extends TreeDao<TabBackground>{
	/**
	 * 查询单个背景图详情
	 * @param tabBackgroundId
	 * @return
	 */
	public TabBackground getTabBackground(int tabBackgroundId);
	
	/**
	 * 删除整组tab_banner
	 * @param tabBackground
	 */
	public void deleteTabBackground(TabBackground tabBackground);
	
	/**
	 * 修改某一组的状态
	 * @param tabBackground
	 */
	public void changIsShow(TabBackground tabBackground);
	
	/**
	 * 修改当前页面上所有组的状态
	 */
	public void changAllIsShow();
	
	/**
	 * 查询出最近更新的那一组的id
	 */
	public int selectIdByUpdateDate();
}
