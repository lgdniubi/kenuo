package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.common.utils.IdGen;
import com.training.modules.ec.dao.MtmyGroupActivityDao;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.MtmyGroupActivity;
import com.training.modules.ec.entity.MtmyGroupActivityGoods;
import com.training.modules.sys.utils.UserUtils;

/**
 * 
 * @author coffee
 * @date 2018年3月30日
 */
@Service
@Transactional(readOnly = false)
public class MtmyGroupActivityService extends TreeService<MtmyGroupActivityDao,MtmyGroupActivity> {
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<MtmyGroupActivity> findList(Page<MtmyGroupActivity> page, MtmyGroupActivity mtmyGroupActivity) {
		// 设置分页参数
		mtmyGroupActivity.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(mtmyGroupActivity));
		return page;
	}
	/**
	 * 保存修改团购活动
	 * @param mtmyGroupActivity
	 */
	public void saveMtmyGroupActivity(MtmyGroupActivity mtmyGroupActivity){
		if(mtmyGroupActivity.getId() != null &&  !"".equals(mtmyGroupActivity.getId())){
			dao.update(mtmyGroupActivity);
		}else{
			mtmyGroupActivity.setCreateBy(UserUtils.getUser());
			mtmyGroupActivity.setId(IdGen.uuid());
			dao.insert(mtmyGroupActivity);
		}
	}
	
	/**
	 * 删除团购项目内的商品
	 * @param goodsSpecPrice
	 */
	public void deleteActivityGoodsByActivity(MtmyGroupActivity mtmyGroupActivity){
		dao.deleteActivityGoodsByActivity(mtmyGroupActivity);
	}
	
	/**
	 * 分页查询查询活动下所有商品
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<MtmyGroupActivityGoods> findGoodsList(Page<MtmyGroupActivityGoods> page, MtmyGroupActivityGoods mtmyGroupActivityGoods) {
		// 设置分页参数
		mtmyGroupActivityGoods.setPage(page);
		// 执行分页查询
		page.setList(dao.findGoodsList(mtmyGroupActivityGoods));
		return page;
	}
	
	/**
	 * 获取活动下商品详情
	 * @param mtmyGroupActivityGoods
	 * @return
	 */
	public MtmyGroupActivityGoods findGoodsForm(MtmyGroupActivityGoods mtmyGroupActivityGoods){
		return dao.findGoodsForm(mtmyGroupActivityGoods);
	}
	
	/**
	 * 新增/修改团购内商品
	 * @param mtmyGroupActivityGoods
	 */
	public void groupActivityGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods){
		if(mtmyGroupActivityGoods.getgId() != 0){
			dao.updateGroupActivityGoods(mtmyGroupActivityGoods);
		}else{
			dao.insterGroupActivityGoods(mtmyGroupActivityGoods);
		}
	}
	/**
	 * 修改规格团购价格
	 * @param goodsSpecPriceList
	 */
	public void updateActivityGoodsSpec(List<GoodsSpecPrice> goodsSpecPriceList){
		for (int i = 0; i < goodsSpecPriceList.size(); i++) {
			dao.updateActivityGoodsSpec(goodsSpecPriceList.get(i));
		}
	}
	
	/**
	 * 删除团购项目内的商品
	 * @param mtmyGroupActivityGoods
	 */
	public void deleteActivityGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods){
		dao.deleteActivityGoods(mtmyGroupActivityGoods);
	}
}
