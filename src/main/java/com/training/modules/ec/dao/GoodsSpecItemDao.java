package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSpecItem;

/**
 * 商品规格项-DAO层
 * @author kele
 * @version 2016-6-20
 */
@MyBatisDao
public interface GoodsSpecItemDao extends CrudDao<GoodsSpecItem>{

	/**
	 * 根据规格id，查询其所有的规格项
	 * @param goodsSpecItem
	 * @return
	 */
	public List<GoodsSpecItem> findSpecItemList(GoodsSpecItem goodsSpecItem);
	
	/**
	 * 根据拼接好的itemid，查询
	 * @param items
	 * @return
	 */
	public List<GoodsSpecItem> findSpecItems(GoodsSpecItem goodsSpecItem);
	
	/**
	 * 保存商品规格项
	 * @param goodsSpecItem
	 * @return
	 */
	public int saveItems(GoodsSpecItem goodsSpecItem);
	
	/**
	 * 删除商品规格项
	 * @param goodsSpecItem
	 * @return
	 */
	public int delteteItems(GoodsSpecItem goodsSpecItem);
	
}
