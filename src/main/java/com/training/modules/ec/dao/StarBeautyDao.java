package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.StarBeauty;
import com.training.modules.ec.entity.StarBeautyMapping;

/**
 * 明星技师自由配置表Dao
 * @author 土豆  2018-3-7
 *
 */
@MyBatisDao
public interface StarBeautyDao extends CrudDao<StarBeauty>{

	/**
	 * 修改当前商家下所有店铺为关闭
	 * @param starBeauty 
	 */
	public void cannelAllIsShow(StarBeauty starBeauty);

	/**
	 * 根据id修改是否显示
	 * @param starBeauty
	 */
	public void changeIsShow(StarBeauty starBeauty);

	/**
	 * 根据明星技师组id查询数据
	 * @param starBeauty
	 * @return
	 */
	public StarBeauty getStarBeautyById(StarBeauty starBeauty);

	/**
	 * 添加
	 * @param starBeauty
	 */
	public void saveStarBeauty(StarBeauty starBeauty);

	/**
	 * 修改
	 * @param starBeauty
	 */
	public void updateStarBeauty(StarBeauty starBeauty);

	/**
	 * 根据明星技师组id查询明星技师内容列表
	 * @param starBeautyMapping
	 * @return
	 */
	public List<StarBeautyMapping> findMappingPage(StarBeautyMapping starBeautyMapping);

	/**
	 * 查询/跳转  明星技师列表
	 * @param starBeautyMapping
	 * @return
	 */
	public StarBeautyMapping getStarBeautyMappingBystarId(StarBeautyMapping starBeautyMapping);

	/**
	 * 保存选择的明星技师
	 * @param starBeautyMapping
	 */
	public void saveStarBeautyMapping(StarBeautyMapping starBeautyMapping);

	/**
	 * 根据mapping表的id查询明星技师
	 * @param starBeautyMapping
	 * @return
	 */
	public StarBeautyMapping getStarBeautyMappingByMappingId(StarBeautyMapping starBeautyMapping);

	/**
	 * 修改明星技师的信息
	 * @param starBeautyMapping
	 */
	public void updateStarBeautyMapping(StarBeautyMapping starBeautyMapping);

	/**
	 * 删除明星技师
	 * @param starBeautyMapping
	 */
	public void delStarBeautyMapping(StarBeautyMapping starBeautyMapping);
	
}
