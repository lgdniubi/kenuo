package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsBrand;

/**
 * 商品品牌-DAO层
 * @author kele
 * @version 2016-6-4 16:17:23
 */
@MyBatisDao
public interface GoodsBrandDao extends CrudDao<GoodsBrand>{

	/**
	 * 商品品牌是否推荐
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsHot(GoodsBrand goodsBrand);
}
