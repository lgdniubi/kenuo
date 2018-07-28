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
	
	/**
	 * 活动过期后将用户领取的红包修改为已过期
	 * @param id
	 */
	public void updateCouponUser(int id);
	
	/**
	 * 查询内部用户数
	 * @return
	 */
	public int queryInnerUserNum();
	
	/**
	 * 查询非内部用户数
	 * @return
	 */
	public int queryNotInnerUserNum();
	
}
