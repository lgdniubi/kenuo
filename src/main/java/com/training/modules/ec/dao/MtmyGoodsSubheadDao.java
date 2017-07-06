package com.training.modules.ec.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSubhead;
import com.training.modules.ec.entity.GoodsSubheadGoods;

/**
 * 商品副标题Dao
 * @author xiaoye
 *
 */
@MyBatisDao
public interface MtmyGoodsSubheadDao extends CrudDao<GoodsSubhead>{
	
	/**
	 * 插入商品副标题
	 */
	public void insertGoodsSubhead(GoodsSubhead goodsSubhead);
	
	/**
	 *  更新商品副标题
	 */
	public void updateGoodsSubhead(GoodsSubhead goodsSubhead);
	
	/**
	 * 根据goodsSubheadId获取相应的商品副标题
	 * @param goodsSubheadId
	 * @return
	 */
	public GoodsSubhead getGoodsSubhead(int goodsSubheadId);
	
	/**
	 * 根据goodsSubheadId获取商品副标题活动对应的商品列表
	 * @param goodsSubheadId
	 * @return
	 */
	public List<GoodsSubheadGoods> selectGoodsByGoodsSubheadId(GoodsSubheadGoods goodsSubheadGoods);
	
	/**
	 * 商品副标题活动的开启或者关闭
	 * @param goodsSubhead
	 */
	public void changeGoodsSubheadStatus(GoodsSubhead goodsSubhead);
	
	/**
	 * 验证商品是否能加到某个活动中 
	 * @param startDate
	 * @param endDate
	 * @param goodsId
	 * @return
	 */
	public int selectGoodsIsUsed(@Param(value="startDate")Date startDate,@Param(value="endDate")Date endDate,@Param(value="goodsId")int goodsId);
	
	/**
	 * 根据goodsSubheadId获取goodsId
	 * @param goodsSubheadId
	 * @return
	 */
	public List<Integer> selectGoodsId(int goodsSubheadId);
	
	/**
	 * 批量删除副标题活动对应的商品
	 * @param goodsSubheadGoodsId
	 */
	public void deleteGoods(int goodsSubheadGoodsId);
	
	/**
	 * 商品副标题添加新商品
	 * @param goodsSubheadId
	 * @param goodsId
	 */
	public void insertGoodsForGoodsSubhead(@Param(value="goodsSubheadId")int goodsSubheadId,@Param(value="goodsId")int goodsId);
	
}
