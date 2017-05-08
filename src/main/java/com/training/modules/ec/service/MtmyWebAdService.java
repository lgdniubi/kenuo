package com.training.modules.ec.service;

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
			mtmyWebAdDao.delAllGoods(adId);
			String[] goodsIdArr = goodsIds.split(",");
			for(String goodsId:goodsIdArr){
				mtmyWebAdDao.insertGoods(adId, Integer.valueOf(goodsId));
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
}
