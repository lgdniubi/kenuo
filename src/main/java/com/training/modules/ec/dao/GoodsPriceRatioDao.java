package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsPriceRatio;

/**
 * 城市异价Dao
 * @author xiaoye  2017年12月27日
 *
 */
@MyBatisDao
public interface GoodsPriceRatioDao extends CrudDao<GoodsPriceRatio>{
	
	/**
	 * 根据异价比例id查看对应的城市和商品
	 * @param goodsPriceRatioId
	 * @return
	 */
	public GoodsPriceRatio queryCityAndGoods(int goodsPriceRatioId);
	
	/**
	 * 插入异价比例
	 * @param goodsPriceRatio
	 */
	public void insertRatio(GoodsPriceRatio goodsPriceRatio);
	
	/**
	 * 修改异价比例 
	 * @param goodsPriceRatio
	 */
 	public void updateRatio(GoodsPriceRatio goodsPriceRatio);
 	
 	/**
 	 * 插入异价比例对应的城市和商品
 	 * @param goodsPriceRatio
 	 */
 	public void insertCityAndGoods(GoodsPriceRatio goodsPriceRatio);
	
 	/**
 	 * 删除某一比例异价
 	 * @param goodsPriceRatioId
 	 */
 	public void deleteRatio(int goodsPriceRatioId);
 	
	/**
	 * 删除某一比例异价对应的商品和城市
	 * @param goodsPriceRatioId
	 */
	public void deleteAll(int goodsPriceRatioId);
}
