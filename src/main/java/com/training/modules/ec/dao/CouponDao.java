package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Coupon;
/**
 * 优惠券管理
 * @author yangyang
 *
 */

@MyBatisDao
public interface CouponDao extends TreeDao<Coupon> {
	
	
	/**
	 * 开启优惠卷开闭状态
	 * @param coupon
	 * @return
	 */
	public int updateStatus(Coupon coupon);
	
	/**
	 * 开启优惠卷设置时间
	 * @param coupon
	 * @return
	 */
	public int updateTime(Coupon coupon);
	/**
	 * 更新过期状态
	 * @return
	 */
	public int updateOldStatus(int couponId);
	
	/**
	 * 更改已发送状态
	 * @return
	 */
	public int updateSendStatus(int couponId);
	/**
	 * 过期状态修改
	 * @param coupon_id
	 * @return
	 */
	public int updateTimeOStatus(int coupon_id);
	
	/**
	 * couponId
	 * @return
	 */
	public List<Coupon>  selectByTime();

}
