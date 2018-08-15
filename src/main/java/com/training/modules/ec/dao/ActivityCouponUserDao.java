package com.training.modules.ec.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ActivityCoupon;
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
	
	/**
	 * 查询订单中某商品可用的充值红包
	 * @return
	 */
	public List<ActivityCoupon> queryRechargeCouponForGoods(@Param(value="userid")int userid,@Param(value="recid")String recid,@Param(value="goodsid")String goodsid);
	
	/**
	 * 查询订单中的某个商品是否使用过充值红包
	 * @param recid
	 * @return
	 */
	public int queryUseNum(String recid);

	/**
	 * 若使用了红包就将该红包设置为已使用
	 * @param orderId
	 * @param mappingId
	 * @param detailsId
	 * @param userId
	 * @param couponId
	 */
	public void updateUsedCoupon(@Param(value="orderId")String orderId,@Param(value="mappingId")String mappingId,@Param(value="detailsId")String detailsId,@Param(value="userId")int userId,@Param(value="couponId")String couponId,@Param(value="useCouponId")String useCouponId);
}
