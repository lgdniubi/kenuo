package com.training.modules.ec.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CouponAcmountMappingDao;
import com.training.modules.ec.dao.CouponCategoryMappingDao;
import com.training.modules.ec.dao.CouponDao;
import com.training.modules.ec.dao.CouponGoodsMappingDao;
import com.training.modules.ec.dao.CouponUserDao;
import com.training.modules.ec.entity.Coupon;
import com.training.modules.ec.entity.CouponAcmountMapping;
import com.training.modules.ec.entity.CouponCategoryMapping;
import com.training.modules.ec.entity.CouponGoodsMapping;
import com.training.modules.ec.entity.CouponUser;

/**
 * 红包管理
 * @author yangyang
 *
 */

@Service
@Transactional(readOnly = false)
public class CouponService extends CrudService<CouponDao,Coupon>{
	
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponGoodsMappingDao couponGoodsMappingDao;
	@Autowired
	private CouponUserDao couponUserDao;
	@Autowired
	private CouponCategoryMappingDao categoryMappingDao;
	@Autowired
	private CouponAcmountMappingDao couponAcmountMappingDao;

	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<Coupon> findCoupon(Page<Coupon> page, Coupon coupon) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		coupon.setPage(page);
		// 执行分页查询
		page.setList(couponDao.findList(coupon));
		return page;
	}
	
	/**
	 * 优惠卷明细分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<CouponUser> findCouponUser(Page<CouponUser> page, CouponUser couponUser) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		couponUser.setPage(page);
		// 执行分页查询
		page.setList(couponUserDao.findList(couponUser));
		return page;
	}
	
	
	/**
	 * 改变红包状态，开启关闭
	 * @param redEnvelope
	 */
	
	public void updateStatus(Coupon coupon){
		couponDao.updateStatus(coupon);
	}
	
	/**
	 * 开启红包，设置时间
	 * @param redEnvelope
	 */
	
	public void updateTime(Coupon coupon){
		couponDao.updateTime(coupon);
	}
	/**
	 * 更新数据
	 * @param redEnvelope
	 */
	public void update(Coupon coupon){
		dao.update(coupon);
	}
	
	/**
	 * 保存数据
	 * @param coupon
	 * @return
	 */
	public int saveCoupon(Coupon coupon){
		dao.insert(coupon);
		int result = coupon.getCouponId();
		if(0 == result){
			try {
				throw new Exception("保存失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}

	/**
	 * 保存到优惠卷商品表
	 * @param list
	 */
	public void insertCouponMapping(List<CouponGoodsMapping> list){
		couponGoodsMappingDao.insertCouponMapping(list);
	}
	
	/**
	 * 保存到优惠卷分类表
	 * @param list
	 */
	public void insertCouponCategoryMapping(List<CouponCategoryMapping> list){
		categoryMappingDao.insertCouponCategoryMapping(list);
	}
	/**
	 * 根据优惠卷查询指定的商品
	 * @param couponId
	 * @return
	 */
	public List<CouponGoodsMapping> CouponGoodslist(int couponId){
		return couponGoodsMappingDao.CouponGoodslist(couponId);
	}
	
	/**
	 * 删除中间表数据
	 * @param couponId
	 * @return
	 */
	public int deleteCoupGood(int couponId){
		return couponGoodsMappingDao.deleteCoupGood(couponId);
	}
	
	/**
	 * 删除分类中间表数据
	 * @param couponId
	 * @return
	 */
	public int deleteCoupCate(int couponId){
		return categoryMappingDao.deleteCoupCate(couponId);
	}
	/**
	 * 更新过期状态
	 * @return
	 */
	public int updateOldStatus(int couponId){
		return couponDao.updateOldStatus(couponId);
	}
	
	/**
	 * 红包推送 内部
	 * @param couponUser
	 * @return
	 */
	public int insertSend(CouponUser couponUser){
		return couponUserDao.insertSend(couponUser);
	}
	/**
	 * 红包推送 非内部
	 * @param couponUser
	 * @return
	 */
	public int insertFeiSend(CouponUser couponUser){
		return couponUserDao.insertFeiSend(couponUser);
	}
	/**
	 * 更改发送状态
	 * @param couponId
	 * @return
	 */
	public int updateSendStatus(int couponId){
		return couponDao.updateSendStatus(couponId);
	}
	
	/**
	 * 红包指定分类数据
	 * @param couponId
	 * @return
	 */
	public List<CouponCategoryMapping> couponCateList(int couponId){
		return categoryMappingDao.couponCateList(couponId);
	}
	/**
	 * 红包金额数据
	 * @param couponId
	 * @return
	 */
	public List<CouponAcmountMapping> acmountList(int couponId){
		return couponAcmountMappingDao.acmountList(couponId);
	}
	/**
	 * 保存红包金额数据
	 * @param couponAcmountMapping
	 * @return
	 */
	public int saveAcmount(CouponAcmountMapping couponAcmountMapping){
		return couponAcmountMappingDao.insertCouponAcmountMapping(couponAcmountMapping);
	}
	/**
	 * 根据id查询数据
	 * @param amountId
	 * @return
	 */
	public CouponAcmountMapping findByAcomountId(int amountId){
		return couponAcmountMappingDao.findByAcomountId(amountId);
	}
	/**
	 * 更新数据
	 * @param couponAcmountMapping
	 * @return
	 */
	public int updateAcmount(CouponAcmountMapping couponAcmountMapping){
		return couponAcmountMappingDao.updateAcmount(couponAcmountMapping);
	}
	/**
	 * 更新状态
	 * @param couponAcmountMapping
	 * @return
	 */
	public int updateStatus(CouponAcmountMapping couponAcmountMapping){
		return couponAcmountMappingDao.updateStatus(couponAcmountMapping);
	}
	/**
	 * 查询数量
	 * @return
	 */
	public int findByAmountidNum(String amountId){
		return couponUserDao.findByAmountidNum(amountId);
	}
	
	/**
	 * 查询数量
	 * @return
	 */
	public int findFeiByAmountidNum(String amountId){
		return couponUserDao.findFeiByAmountidNum(amountId);
	}
	/**
	 * 
	 * @param amountId
	 * @param userId
	 * @return
	 */
	public int findByAIdandUserId(CouponUser couponUser){
		return couponUserDao.findByAIdandUserId(couponUser);
	}
	/**
	 * 指定用户添加红包
	 * @param couponUser
	 * @return
	 */
	public int insertUserCoupon(CouponUser couponUser){
		return couponUserDao.insertUserCoupon(couponUser);
	}
	
	/**
	 * 红包金额数据
	 * @param couponId
	 * @return
	 */
	public List<CouponAcmountMapping> findByCouIdList(int couponId){
		return couponAcmountMappingDao.findByCouIdList(couponId);
	}
	/**
	 * 更新库存
	 * @param map
	 * @return
	 */
	public int modifyCouponNumber(Map<String, Object> map){
		return this.couponAcmountMappingDao.modifyCouponNumber(map);
	}
	
	public List<CouponAcmountMapping> timeOutAmountList(){
		return this.couponAcmountMappingDao.timeOutAmountList();
	}
	
	public int updateTimeOStatus(int coupon_id){
		return this.couponDao.updateTimeOStatus(coupon_id);
	}
	
	public List<Coupon> selectByTime(){
		return this.couponDao.selectByTime();
	}
	
}
