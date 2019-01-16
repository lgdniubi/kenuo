/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * qjsky
 * @version 2016-01-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	/**
	 * 通过父类ID查询  子类（用于区域管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Area> findListByPID(String pid);
	
	/**
	 * 根据最小的范围id  查询省  市区  县
	 * @return
	 */
	public Area selectByXid(String id);
	
	public List<Area> findAreaByType(Area area);

	public List<Area> findAreaByParentIdsLike(Area area);
	
	/**
	 * 根据父类id查询子类数据
	 * @param area
	 * @return
	 */
	public List<Area> findByPidforChild(Area area);
} 
