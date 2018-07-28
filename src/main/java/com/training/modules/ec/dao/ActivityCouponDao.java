package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ActivityCoupon;

@MyBatisDao
public interface ActivityCouponDao extends TreeDao<ActivityCoupon> {
	
	
	/**
	 * 获取红包活动下的红包list
	 * @param id
	 * @return
	 */
	public List<ActivityCoupon> Couponlist(String id);
	
	/**
	 * 获取红包对象
	 * @param id
	 * @return
	 */
	public ActivityCoupon findByCouponId(String id);
	
	/**
	 * 红包开关状态
	 * @param activityCoupon
	 * @return
	 */
	public int updateCouponStatus(ActivityCoupon activityCoupon);
	
	
	/**
	 * 修改库存
	 * @param map
	 * @return
	 */
	public int modifyCouponNumber(Map<String, Object> map);
	
	/**
	 * 查询已开启的红包
	 * @param id
	 * @return
	 */
	public List<ActivityCoupon> findByCouIdList(int actionId);
	
	/**
	 * 删除活动对应的红包
	 * @param id
	 */
	public void delCoupon(String id);
}
