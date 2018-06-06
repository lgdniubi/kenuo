package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSpec;

/**
 * 商品规格-DAO层
 * @author kele
 * @version 2016-6-20
 */
@MyBatisDao
public interface GoodsSpecDao extends CrudDao<GoodsSpec>{

	/**
	 * 保存商品规格表
	 * @param goodsSpec
	 */
	public void insertGoodsSpec(GoodsSpec goodsSpec);
	
	/**
	 * 级联新增商品规格项
	 * @param goodsSpec
	 */
	public void insertSpecItems(GoodsSpec goodsSpec);
	
	/**
	 * 级联删除商品规格项
	 * @param goodsSpec
	 * @return
	 */
	public int deleteSpecItems(GoodsSpec goodsSpec);

	/**
	 * 查看规格是否有商品使用
	 * @param itemId
	 * @return
	 */
	public int findUseSpecById(Integer specItemId);
}
