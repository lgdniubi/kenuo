/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.sys.dao.AreaDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	public List<Area> findListByPID(String pid){
		return dao.findListByPID(pid);
	}
	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	/**
	 * 通过条件查询地市
	 */
	public List<Area> findList(Area area){
		return dao.findAreaByType(area);
	}
	public List<Area> findAreaByParentIdsLike(Area area){
		area.setParentIds("%"+area.getId()+"%");
		return dao.findAreaByParentIdsLike(area);
	}
	
	/**
	 * 根据父类id查询子类数据
	 * 用于异步加载树形table
	 * @param area
	 * @return
	 */
	public List<Area> findByPidforChild(Area area){
		return dao.findByPidforChild(area);
	}
}
