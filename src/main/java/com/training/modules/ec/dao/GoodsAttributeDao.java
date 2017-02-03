package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsAttribute;

/**
 * 商品属性-DAO层
 * @author kele
 * @version 2016-6-20
 */
@MyBatisDao
public interface GoodsAttributeDao extends CrudDao<GoodsAttribute>{

	/**
	 * 商品属性-是否检索
	 * @param goodsCategory
	 * @return
	 */
	public int updateAttrIndex(GoodsAttribute goodsAttribute);
}
