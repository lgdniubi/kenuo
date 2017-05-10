package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSupplier;

/**
 * 
 * @author 土豆  2017.5.9
 *
 */
@MyBatisDao
public interface GoodsSupplierDao extends CrudDao<GoodsSupplier>{

	/**
	 * 异步 修改状态
	 * @param goodsSupplier
	 */
	public void updateStatus(GoodsSupplier goodsSupplier);


}
