package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyWebAd;

/**
 * 首页广告图Dao
 * @author xiaoye 2017年5月8日
 *
 */
@MyBatisDao
public interface MtmyWebAdDao extends CrudDao<MtmyWebAd>{
	
	/**
	 * 根据mtmyWebAdId获取相应的首页广告图 
	 * @param mtmyWebAdId
	 * @return
	 */
	public MtmyWebAd getMtmyWebAd(int mtmyWebAdId);
	
	/**
	 * 更改首页广告图的状态
	 * @param mtmyWebAdId
	 */
	public void updateIsShow(MtmyWebAd mtmyWebAd);
	
	/**
	 * 插入首页广告图
	 */
	public void insertMtmyWebAd(MtmyWebAd mtmyWebAd);
	
	/**
	 * 更新首页广告图
	 */
	public void updateMtmyWebAd(MtmyWebAd mtmyWebAd);
	
	/**
	 * 逻辑删除首页广告图 
	 * @param mtmyWebAdId
	 */
	public void delMtmyWebAd(int mtmyWebAdId);
	
	/**
	 * 查询出首页广告图 对应的商品
	 */
	public List<Goods> findGoodsList(Goods goods);
	
	/**
	 * 保存首页广告图 对应的商品 
	 * @param adId
	 * @param goodsId
	 */
	public void insertGoods(@Param("adId")int adId,@Param(value="goodsId")int goodsId);
	
	/**
	 * 删除首页广告图 对应的商品 
	 * @param adId
	 * @param goodsId
	 */
	public void delGoods(@Param("adId")int adId,@Param(value="goodsId")int goodsId);
	
	/**
	 * 删除首页广告图 对应的商品 
	 * @param adId
	 */
	public void delAllGoods(int adId);
	
	
	/**
	 * 根据categoryId逻辑删除首页广告图 ,当为非一级分类时
	 * @param categoryId
	 */
	public void delMtmyWebAdByCategoryIdForNotFirst(int categoryId);
	
	/**
	 * 根据categoryId逻辑删除首页广告图 ,当为一级分类时
	 * @param categoryId
	 */
	public void delMtmyWebAdByCategoryIdForFirst(int categoryId);
	
	/**
	 * 根据categoryId屋里删除首页广告图对应的所有商品,当为非一级分类时
	 * @param categoryId
	 */
	public void delAllGoodsByCategoryIdForNotFirst(int categoryId);
	
	/**
	 * 根据categoryId屋里删除首页广告图对应的所有商品,当为一级分类时
	 * @param categoryId
	 */
	public void delAllGoodsByCategoryIdForFirst(int categoryId);
	
	/**
	 * 修改当前页面上所有组的状态
	 * @param mtmyWebAd
	 */
	public void changAllIsShow(MtmyWebAd mtmyWebAd);
	
	/**
	 * 查询出最近创建的那一组的id
	 * @param mtmyWebAd
	 * @return
	 */
	public int selectIdByCreateDate(MtmyWebAd mtmyWebAd);
}
