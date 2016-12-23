package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.CouponAcmountMapping;


/**
 * 红包金额面值表Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface CouponAcmountMappingDao extends TreeDao<CouponAcmountMapping>{
	
	/**
	 * 插入优惠卷商品中间表
	 * @param list
	 * @return
	 */
	public int insertCouponAcmountMapping(CouponAcmountMapping couponAcmountMapping);
	/**
	 * 根据优惠卷Id 查询指定商品
	 * @param couponId
	 * @return
	 */
	public List<CouponAcmountMapping> acmountList(int couponId);
	/**
	 * 根据金额id 查询
	 * @param amountId
	 * @return
	 */
	public CouponAcmountMapping findByAcomountId(int amountId);
	/**
	 * 更新数据
	 * @param couponAcmountMapping
	 * @return
	 */
	public int updateAcmount(CouponAcmountMapping couponAcmountMapping);
	/**
	 * 更改状态
	 * @param couponAcmountMapping
	 * @return
	 */
	public int updateStatus(CouponAcmountMapping couponAcmountMapping);
	
	/**
	 * 
	 * @param couponId
	 * @return
	 */
	public List<CouponAcmountMapping> findByCouIdList(int couponId);
	
	/**
	 * 修改库存
	 * @param map
	 * @return
	 */
	public int modifyCouponNumber(Map<String, Object> map);
	/**
	 * 查询过期
	 * @return
	 */
	public List<CouponAcmountMapping> timeOutAmountList();
}
