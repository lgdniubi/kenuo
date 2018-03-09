package com.training.modules.ec.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.StarBeautyDao;
import com.training.modules.ec.entity.StarBeauty;
import com.training.modules.ec.entity.StarBeautyMapping;

/**
 * 明星技师自由配置表Service
 * @author 土豆  2018-3-7
 *
 */
@Service
@Transactional(readOnly = false)
public class StarBeautyService extends CrudService<StarBeautyDao, StarBeauty>{
	
	@Autowired
	private StarBeautyDao starBeautyDao;
	
	/**
	 * 分页查询
	 */
	public Page<StarBeauty> findPage(Page<StarBeauty> page,StarBeauty starBeauty){
		starBeauty.setPage(page);
		page.setList(dao.findList(starBeauty));
		return page;
	}

	/**
	 * 修改状态
	 * @param starBeauty
	 * @return
	 */
	public Map<String, String> updateType(StarBeauty starBeauty) {
		Map<String, String> map = new HashMap<String, String>();
		//必须有一个是显示的. 
		//当传递的isShow = 1,要求启用.关闭其他,打开这个~~~~~传递isShow=0,要求关闭,关闭所有的,打开最新的
		starBeautyDao.cannelAllIsShow();
		if(starBeauty.getIsShow() == 1){
			//根据id修改状态为使用
			starBeautyDao.changeIsShow(starBeauty);
		}else{
			//查询最新添加的数据,修改为显示
			starBeauty = starBeautyDao.findStarBeauty();
			starBeautyDao.changeIsShow(starBeauty);
		}
		map.put("STATUS", "OK");
		return map;
	}

	/**
	 * 根据明星技师组id查询数据
	 * @param starBeauty
	 * @return
	 */
	public StarBeauty getStarBeautyById(StarBeauty starBeauty) {
		return starBeautyDao.getStarBeautyById(starBeauty);
	}

	/**
	 * 添加明星技师组
	 * @param starBeauty
	 */
	public void saveStarBeauty(StarBeauty starBeauty) {
		starBeautyDao.saveStarBeauty(starBeauty);
	}
	
	/**
	 * 修改明星技师组
	 * @param starBeauty
	 */
	public void updateStarBeauty(StarBeauty starBeauty) {
		starBeautyDao.updateStarBeauty(starBeauty);
	}

	/**
	 * 删除明星技师组
	 * @param starBeauty
	 */
	public void delStarBeauty(StarBeauty starBeauty) {
		starBeautyDao.delete(starBeauty);
	}

	/**
	 * 根据明星技师组id查询明星技师内容列表
	 * @param page
	 * @param starBeauty
	 * @return
	 */
	public Page<StarBeautyMapping> findMappingPage(Page<StarBeautyMapping> page, StarBeautyMapping starBeautyMapping) {
		starBeautyMapping.setPage(page);
		page.setList(starBeautyDao.findMappingPage(starBeautyMapping));
		return page;
	}

	/**
	 * 查询/跳转  明星技师列表
	 * @param starBeautyMapping
	 * @return
	 */
	public StarBeautyMapping getStarBeautyMappingBystarId(StarBeautyMapping starBeautyMapping) {
		return starBeautyDao.getStarBeautyMappingBystarId(starBeautyMapping);
	}

	/**
	 * 保存选择的明星技师
	 * @param starBeautyMapping
	 */
	public void saveStarBeautyMapping(StarBeautyMapping starBeautyMapping) {
		starBeautyDao.saveStarBeautyMapping(starBeautyMapping);
	}

	/**
	 * 根据mapping表的id查询明星技师
	 * @param starBeautyMapping
	 * @return
	 */
	public StarBeautyMapping getStarBeautyMappingByMappingId(StarBeautyMapping starBeautyMapping) {
		return starBeautyDao.getStarBeautyMappingByMappingId(starBeautyMapping);
	}

	/**
	 * 修改明星技师的信息
	 * @param starBeautyMapping
	 */
	public void updateStarBeautyMapping(StarBeautyMapping starBeautyMapping) {
		starBeautyDao.updateStarBeautyMapping(starBeautyMapping);
	}

	/**
	 * 根据id删除明星技师
	 * @param starBeautyMapping
	 */
	public void delStarBeautyMapping(StarBeautyMapping starBeautyMapping) {
		starBeautyDao.delStarBeautyMapping(starBeautyMapping);
	}

}
