package com.training.modules.ec.dao;


import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.CouponUser;

/**
 * 优惠卷明细管理Dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface CouponUserDao extends CrudDao<CouponUser> {
	
	
	/**
	 * 根据订单id查询
	 * @param couponUser
	 * @return
	 */
	public CouponUser findByorderId(String orderId);
	
	/**
	 * 改变红包状态
	 * @param couponUser
	 * @return
	 */
	public int UpdateStatus(CouponUser couponUser);

	/**
	 * 推送红包 内部
	 * @param couponUser
	 * @return
	 */
	public int insertSend(CouponUser couponUser);
	/**
	 * 非内部 红包推送
	 * @param couponUser
	 * @return
	 */
	public int insertFeiSend(CouponUser couponUser);
	/**
	 * 查询数据数量 内部
	 * @param amountId
	 * @return
	 */
	public int findByAmountidNum(String amountId);
	/**
	 * 
	 * @param amountId
	 * @param userId
	 * @return
	 */
	public int findByAIdandUserId(CouponUser couponUser);
	/**
	 * 指定用户添加红包
	 * @param couponUser
	 * @return
	 */
	public int insertUserCoupon(CouponUser couponUser);
	/**
	 * 查询非内部 是否有红包发放
	 * @param amountId
	 * @return
	 */
	public int findFeiByAmountidNum(String amountId);
}
