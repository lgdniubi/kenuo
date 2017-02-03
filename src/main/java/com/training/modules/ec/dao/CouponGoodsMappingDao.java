package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.CouponGoodsMapping;


/**
 * 商品优惠价映射表Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface CouponGoodsMappingDao extends TreeDao<CouponGoodsMapping>{
	
	/**
	 * 插入优惠卷商品中间表
	 * @param list
	 * @return
	 */
	public int insertCouponMapping(List<CouponGoodsMapping> list);
	/**
	 * 根据优惠卷Id 查询指定商品
	 * @param couponId
	 * @return
	 */
	public List<CouponGoodsMapping> CouponGoodslist(int couponId);
	/**
	 * 删除中间表
	 * @param couponId
	 * @return
	 */
	public int deleteCoupGood(int couponId);
	

}
