package com.training.modules.ec.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyGoodsSubheadDao;
import com.training.modules.ec.entity.GoodsSubhead;
import com.training.modules.ec.entity.GoodsSubheadGoods;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 商品副标题Service
 * @author xiaoye
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyGoodsSubheadService extends CrudService<MtmyGoodsSubheadDao, GoodsSubhead>{
	
	@Autowired
	private MtmyGoodsSubheadDao mtmyGoodsSubheadDao;
	
	/**
	 * 插入商品副标题
	 */
	public void insertGoodsSubhead(GoodsSubhead goodsSubhead){
		User user = UserUtils.getUser();
		goodsSubhead.setCreateBy(user);
		mtmyGoodsSubheadDao.insertGoodsSubhead(goodsSubhead);
	}
	
	/**
	 *  更新商品副标题
	 */
	public void updateGoodsSubhead(GoodsSubhead goodsSubhead){
		User user = UserUtils.getUser();
		goodsSubhead.setCreateBy(user);
		mtmyGoodsSubheadDao.updateGoodsSubhead(goodsSubhead);
	}
	
	/**
	 * 根据goodsSubheadId获取相应的商品副标题
	 * @param goodsSubheadId
	 * @return
	 */
	public GoodsSubhead getGoodsSubhead(int goodsSubheadId){
		return mtmyGoodsSubheadDao.getGoodsSubhead(goodsSubheadId);
	}
	
	
	/**
	 * 商品副标题活动的开启或者关闭
	 * @param goodsSubhead
	 */
	public void changeGoodsSubheadStatus(GoodsSubhead goodsSubhead){
		mtmyGoodsSubheadDao.changeGoodsSubheadStatus(goodsSubhead);
	}
	
	/**
	 * 根据goodsSubheadId获取商品副标题活动对应的商品列表
	 * @param goodsSubheadId
	 * @return
	 */
	public Page<GoodsSubheadGoods> selectGoodsByGoodsSubheadId(Page<GoodsSubheadGoods> page,GoodsSubheadGoods goodsSubheadGoods){
		goodsSubheadGoods.setPage(page);
		page.setList(mtmyGoodsSubheadDao.selectGoodsByGoodsSubheadId(goodsSubheadGoods));
		return page;
	}
	
	/**
	 * 验证商品是否能加到某个活动中 
	 * @param startDate
	 * @param endDate
	 * @param goodsId
	 * @return
	 */
	public int selectGoodsIsUsed(@Param(value="startDate")Date startDate,@Param(value="endDate")Date endDate,@Param(value="goodsId")int goodsId){
		return mtmyGoodsSubheadDao.selectGoodsIsUsed(startDate, endDate, goodsId);
	}
	
	/**
	 * 根据goodsSubheadId获取goodsId
	 * @param goodsSubheadId
	 * @return
	 */
	public List<Integer> selectGoodsId(int goodsSubheadId){
		return mtmyGoodsSubheadDao.selectGoodsId(goodsSubheadId);
	}
	
	/**
	 * 批量删除副标题活动对应的商品
	 * @param goodsSubheadGoodsId
	 */
	public void deleteGoods(int goodsSubheadGoodsId){
		mtmyGoodsSubheadDao.deleteGoods(goodsSubheadGoodsId);
	}
	
	/**
	 * 商品副标题添加新商品
	 * @param goodsSubheadId
	 * @param goodsId
	 */
	public void insertGoodsForGoodsSubhead(int goodsSubheadId,int goodsId){
		mtmyGoodsSubheadDao.insertGoodsForGoodsSubhead(goodsSubheadId, goodsId);
	}
	
}
