package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.MtmyGroupActivity;
import com.training.modules.ec.entity.MtmyGroupActivityGoods;
import com.training.modules.ec.entity.MtmyGroupActivityRemind;
import com.training.modules.ec.entity.Orders;

/**
 * 团购活动dao
 * @author coffee
 * @date 2018年3月30日
 */
@MyBatisDao
public interface MtmyGroupActivityDao extends TreeDao<MtmyGroupActivity> {
	
	/**
	 * 删除团购项目内的商品
	 * @param goodsSpecPrice
	 */
	public void deleteActivityGoodsByActivity(MtmyGroupActivity mtmyGroupActivity);
	
	/**
	 * 查询活动下所有商品
	 * @param mtmyGroupActivityGoods
	 * @return
	 */
	public List<MtmyGroupActivityGoods> findGoodsList(MtmyGroupActivityGoods mtmyGroupActivityGoods);
	
	/**
	 * 获取活动下商品详情
	 * @param mtmyGroupActivityGoods
	 * @return
	 */
	public MtmyGroupActivityGoods findGoodsForm(MtmyGroupActivityGoods mtmyGroupActivityGoods);
	
	/**
	 * 插入团购项目商品
	 * @param mtmyGroupActivityGoods
	 */
	public void insterGroupActivityGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods);
	
	/**
	 * 修改团购项目商品
	 * @param mtmyGroupActivityGoods
	 */
	public void updateGroupActivityGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods);
	
	/**
	 * 修改团购项目商品的团购价格
	 * @param goodsSpecPrice
	 */
	public void updateActivityGoodsSpec(GoodsSpecPrice goodsSpecPrice);
	
	/**
	 * 删除团购项目内的商品
	 * @param goodsSpecPrice
	 */
	public void deleteActivityGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods);
	
	/**
	 * 查询5分钟内的活动
	 * @return
	 */
	public List<MtmyGroupActivity> findGroupActivity();
	
	/**
	 * 查询提醒用户
	 * @param activityId
	 * @return
	 */
	public List<MtmyGroupActivityRemind> findGroupActivityRemind(String activityId);
	
	/**
	 * 修改提醒用户
	 * @param ids
	 */
	public void updateGroupActivityRemind(List<Integer> list);
	
	/**
	 * 查询取消团购订单
	 * @return
	 */
	public List<Orders> selectCancelGroupOrder();
	
	/**
	 * 归还取消订单库存
	 * @param orderId
	 * @return
	 */
	public int returnGroupOrder(String orderId);
}
