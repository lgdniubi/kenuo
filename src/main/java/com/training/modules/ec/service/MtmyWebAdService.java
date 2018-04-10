package com.training.modules.ec.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyWebAdDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyWebAd;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 首页广告图Service
 * @author xiaoye  2017年5月8日
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyWebAdService extends CrudService<MtmyWebAdDao,MtmyWebAd>{
	
	@Autowired
	private MtmyWebAdDao mtmyWebAdDao;
	
	/**
	 * 根据mtmyWebAdId获取相应的首页广告图 
	 * @param mtmyWebAdId
	 * @return
	 */
	public MtmyWebAd getMtmyWebAd(int mtmyWebAdId){
		return mtmyWebAdDao.getMtmyWebAd(mtmyWebAdId);
	}
	
	/**
	 * 更改首页广告图的状态
	 * @param mtmyWebAdId
	 */
	public void updateIsShow(MtmyWebAd mtmyWebAd){
		mtmyWebAdDao.updateIsShow(mtmyWebAd);
	}
	
	/**
	 * 插入首页广告图
	 */
	public void insertMtmyWebAd(MtmyWebAd mtmyWebAd){
		User user = UserUtils.getUser();
		mtmyWebAd.setCreateBy(user);
		mtmyWebAdDao.insertMtmyWebAd(mtmyWebAd);
	}
	
	/**
	 * 更新首页广告图
	 */
	public void updateMtmyWebAd(MtmyWebAd mtmyWebAd){
		User user = UserUtils.getUser();
		mtmyWebAd.setUpdateBy(user);
		mtmyWebAdDao.updateMtmyWebAd(mtmyWebAd);
	}
	
	/**
	 * 逻辑删除首页广告图 
	 * @param mtmyWebAdId
	 */
	public void delMtmyWebAd(int mtmyWebAdId){
		mtmyWebAdDao.delMtmyWebAd(mtmyWebAdId);
	}
	
	/**
	 * 分页查询出首页广告图对应的商品
	 */
	public Page<Goods> findGoodsPage(Page<Goods> page,Goods goods){
		goods.setPage(page);
		page.setList(dao.findGoodsList(goods));
		return page;
	}
	
	/**
	 * 保存首页广告图对应的商品 
	 * @param adId
	 * @param goodsId
	 */
	public void saveMtmyWebAdGoods(int adId,String goodsIds){
		if(!"".equals(goodsIds) && goodsIds != null){
			//根据adId获取原有数据
			Goods goods = new Goods();
			goods.setAdId(adId);
			List<Goods> list = dao.findGoodsList(goods);
			//删除原有的全部数据
			mtmyWebAdDao.delAllGoods(adId);
			//保存新增数据
			String[] goodsIdArr = goodsIds.split(",");
			for(String goodsId:goodsIdArr){
				mtmyWebAdDao.insertGoods(adId, Integer.valueOf(goodsId));
			}
			//循环集合,根据adId和商品id修改sort值
			for (Goods g : list) {
				mtmyWebAdDao.insertGoodsSort(g.getSort(), adId, g.getGoodsId());
			}
		}
	}
	
	/**
	 * 删除首页广告图对应的商品 
	 * @param adId
	 * @param goodsId
	 */
	public void delGoods(int adId,int goodsId){
		mtmyWebAdDao.delGoods(adId, goodsId);
	}
	
	/**
	 * 根据categoryId逻辑删除首页广告图 ,当为二级分类时
	 * @param categoryId
	 */
	public void delMtmyWebAdByCategoryIdForNotFirst(int categoryId){
		mtmyWebAdDao.delMtmyWebAdByCategoryIdForNotFirst(categoryId);
	}
	
	/**
	 * 根据categoryId逻辑删除首页广告图 ,当为二级分类时
	 * @param categoryId
	 */
	public void delMtmyWebAdByCategoryIdForFirst(int categoryId){
		mtmyWebAdDao.delMtmyWebAdByCategoryIdForFirst(categoryId);
	}
	
	/**
	 * 根据categoryId屋里删除首页广告图对应的所有商品,当为二级分类时
	 * @param categoryId
	 */
	public void delAllGoodsByCategoryIdForNotFirst(int categoryId){
		mtmyWebAdDao.delAllGoodsByCategoryIdForNotFirst(categoryId);
	}
	
	/**
	 * 根据categoryId屋里删除首页广告图对应的所有商品,当为一级分类时
	 * @param categoryId
	 */
	public void delAllGoodsByCategoryIdForFirst(int categoryId){
		mtmyWebAdDao.delAllGoodsByCategoryIdForFirst(categoryId);
	}
	
	/**
	 * 保存广告图对应商品的排序
	 * @param adId
	 * @param goodsId
	 */
	public void insertGoodsSort(@Param(value="sort")int sort,@Param("adId")int adId,@Param(value="goodsId")int goodsId){
		mtmyWebAdDao.insertGoodsSort(sort, adId, goodsId);
	}
	
}
