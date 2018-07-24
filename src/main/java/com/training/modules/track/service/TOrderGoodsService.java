package com.training.modules.track.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.modules.track.dao.ITOrderGoodsDao;
import com.training.modules.track.entity.TOrder;
import com.training.modules.track.entity.TOrderGoods;
import com.training.modules.track.entity.TOrderGoodsRecharge;

/**
 * 类名称：	TOrderGoodsService
 * 类描述：	埋点-订单商品详情Service层
 * 创建人： 	kele
 * 创建时间：    	2018年1月19日 下午3:52:53
 */
@Service
@Transactional(readOnly = false)
public class TOrderGoodsService {
	
	@Autowired
	private ITOrderGoodsDao iTOrderGoodsDao;
	
	/**
	 * 方法说明：	查询订单详情
	 * 创建时间：	2018年7月14日 下午2:20:30
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午2:20:30
	 * @param map
	 * @return
	 */
	public TOrderGoodsRecharge queryOrderDetail(Map<String, Object> map){
		return iTOrderGoodsDao.queryOrderDetail(map);
	}
	
	/**
	 * 方法说明：	查询该订单所有的商品信息
	 * 创建时间：	2018年1月19日 下午3:16:37
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年1月19日 下午3:16:37
	 * @param map
	 * @return
	 */
	public List<TOrderGoods> queryOrderGoodsList(Map<String, Object> map){
		return iTOrderGoodsDao.queryOrderGoodsList(map);
	}
	
	/**
	 * 方法说明：	查询订单信息
	 * 创建时间：	2018年1月20日 下午5:35:48
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年1月20日 下午5:35:48
	 * @param map
	 * @return
	 */
	public TOrder queryTOrder(Map<String, Object> map){
		return iTOrderGoodsDao.queryOrder(map);
	}
	
	/**
	 * 方法说明：	查询用户订单数量
	 * 创建时间：	2018年7月12日 下午5:04:02
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月12日 下午5:04:02
	 * @param map
	 * @return
	 */
	public int queryUserOrderNum(Map<String, Object> map) {
		return iTOrderGoodsDao.queryUserOrderNum(map);
	}
}
