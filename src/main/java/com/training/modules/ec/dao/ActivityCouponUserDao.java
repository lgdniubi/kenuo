package com.training.modules.ec.dao;


import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ActivityCouponUser;
import com.training.modules.ec.entity.OrderGoodsCoupon;

/**
 * 优惠卷明细管理Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface ActivityCouponUserDao extends CrudDao<ActivityCouponUser> {
	
	
	/**
	 * 根据订单id查询
	 * @param couponUser
	 * @return
	 */
	public ActivityCouponUser findByorderId(String orderId);
	
	/**
	 * 改变红包状态
	 * @param couponUser
	 * @return
	 */
	public int UpdateStatus(ActivityCouponUser couponUser);

	/**
	 * 推送红包 内部
	 * @param couponUser
	 * @return
	 */
	public int insertSend(ActivityCouponUser couponUser);
	/**
	 * 非内部 红包推送
	 * @param couponUser
	 * @return
	 */
	public int insertFeiSend(ActivityCouponUser couponUser);
	/**
	 * 查询数据数量 内部
	 * @param amountId
	 * @return
	 */
	public int findByCouponNum(String couponId);
	/**
	 * 
	 * @param amountId
	 * @param userId
	 * @return
	 */
	public int findByAIdandUserId(ActivityCouponUser couponUser);
	/**
	 * 指定用户添加红包
	 * @param couponUser
	 * @return
	 */
	public int insertUserCoupon(ActivityCouponUser couponUser);
	/**
	 * 查询非内部 是否有红包发放
	 * @param amountId
	 * @return
	 */
	public int findFeiByCouponNum(String amountId);
	/**
	 * 查询订单下的红包
	 * @param orderId
	 * @return
	 */
	public List<OrderGoodsCoupon> findlistByOrdeid(String orderId);
	
	/**
	 * 导出红包明细
	 * @param activityCouponUser
	 * @return
	 */
	public List<ActivityCouponUser> exportfindList(ActivityCouponUser activityCouponUser);
	
	
}
