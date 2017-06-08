package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ShopSpeciality;


/**
 * 店铺标签管理 Dao
 * @author 土豆
 * @version 2017-06-08
 */

@MyBatisDao
public interface ShopSpecialityDao extends CrudDao<ShopSpeciality> {
	
	/**
	 * 查询所有特长
	 */
	
	public List<ShopSpeciality> findAllList();

	/**
	 * 根据id删除数据
	 * @param shopSpeciality
	 */
	public void deleteShopSpeciality(ShopSpeciality shopSpeciality);
	
}
