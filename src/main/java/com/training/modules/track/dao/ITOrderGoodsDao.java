package com.training.modules.track.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.track.entity.TOrder;
import com.training.modules.track.entity.TOrderGoods;
import com.training.modules.track.entity.TOrderGoodsRecharge;

/**
 * 类名称：	ITOrderGoodsDao
 * 类描述：	埋点-订单商品详情Dao层
 * 创建人： 	kele
 * 创建时间：    2018年7月14日14:16:15
 */
@MyBatisDao
public interface ITOrderGoodsDao {
	
	/**
	 * 方法说明：	查询订单详情
	 * 创建时间：	2018年7月14日 下午2:17:37
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午2:17:37
	 * @param map
	 * @return
	 */
	public TOrderGoodsRecharge queryOrderDetail(Map<String, Object> map);
	
	/**
	 * 方法说明：	查询该订单所有的商品信息
	 * 创建时间：	2018年1月19日 下午3:08:32
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年1月19日 下午3:08:32
	 * @param map
	 * @return
	 */
	public List<TOrderGoods> queryOrderGoodsList(Map<String, Object> map);
	
	/**
	 * 方法说明：	查询订单信息
	 * 创建时间：	2018年1月20日 下午5:35:03
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年1月20日 下午5:35:03
	 * @param map
	 * @return
	 */
	public TOrder queryOrder(Map<String, Object> map);
	
	/**
	 * 方法说明：	查询用户订单数量
	 * 创建时间：	2018年7月12日 下午5:04:02
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月12日 下午5:04:02
	 * @param map
	 * @return
	 */
	public int queryUserOrderNum(Map<String, Object> map);
}
