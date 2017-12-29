package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsPriceRatioDao;
import com.training.modules.ec.entity.GoodsPriceRatio;

/**
 * 城市异价Service
 * @author xiaoye   2017年12月27日
 *
 */
@Service
@Transactional(readOnly = false)
public class GoodsPriceRatioService extends CrudService<GoodsPriceRatioDao, GoodsPriceRatio>{
	
	@Autowired
	private GoodsPriceRatioDao goodsPriceRatioDao;
	
	/**
	 * 根据异价比例id查看对应的城市和商品
	 * @param goodsPriceRatioId
	 * @return
	 */
	public GoodsPriceRatio queryCityAndGoods(int goodsPriceRatioId){
		return goodsPriceRatioDao.queryCityAndGoods(goodsPriceRatioId);
	}
	
	/**
	 * 插入异价比例
	 * @param goodsPriceRatio
	 */
	public void insertRatio(GoodsPriceRatio goodsPriceRatio){
		goodsPriceRatioDao.insertRatio(goodsPriceRatio);
	}
	
	/**
	 * 修改异价比例 
	 * @param goodsPriceRatio
	 */
 	public void updateRatio(GoodsPriceRatio goodsPriceRatio){
 		goodsPriceRatioDao.updateRatio(goodsPriceRatio);
 	}
 	
 	/**
 	 * 插入异价比例对应的城市和商品
 	 * @param goodsPriceRatio
 	 */
 	public void insertCityAndGoods(GoodsPriceRatio goodsPriceRatio){
 		goodsPriceRatioDao.insertCityAndGoods(goodsPriceRatio);
 	}
	
 	/**
 	 * 删除某一比例异价
 	 * @param goodsPriceRatioId
 	 */
 	public void deleteRatio(int goodsPriceRatioId){
 		goodsPriceRatioDao.deleteRatio(goodsPriceRatioId);
 	}
 	
	/**
	 * 删除某一比例异价对应的商品和城市
	 * @param goodsPriceRatioId
	 */
	public void deleteAll(int goodsPriceRatioId){
		goodsPriceRatioDao.deleteAll(goodsPriceRatioId);
	}
}
