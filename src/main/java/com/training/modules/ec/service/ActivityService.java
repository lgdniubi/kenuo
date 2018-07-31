package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.ActivityCouponCategoryDao;
import com.training.modules.ec.dao.ActivityCouponDao;
import com.training.modules.ec.dao.ActivityCouponGoodsDao;
import com.training.modules.ec.dao.ActivityCouponUserDao;
import com.training.modules.ec.dao.ActivityDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.entity.Activity;
import com.training.modules.ec.entity.ActivityCoupon;
import com.training.modules.ec.entity.ActivityCouponCategory;
import com.training.modules.ec.entity.ActivityCouponGoods;
import com.training.modules.ec.entity.ActivityCouponUser;
import com.training.modules.ec.entity.Users;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class ActivityService extends TreeService<ActivityDao,Activity> {
	
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ActivityCouponDao activityCouponDao;
	@Autowired
	private ActivityCouponCategoryDao activityCouponCategoryDao;
	@Autowired
	private ActivityCouponGoodsDao activityCouponGoodsDao;
	@Autowired
	private ActivityCouponUserDao activityCouponUserDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<Activity> findActivity(Page<Activity> page, Activity activity) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		activity.setPage(page);
		// 执行分页查询
		page.setList(activityDao.findList(activity));
		return page;
	}
	
	/**
	 * 获取红包对象
	 * @param id
	 * @return
	 */
	public ActivityCoupon findByCouponId(String id){
		return activityCouponDao.findByCouponId(id);
	}
	
	/**
	 * 保存数据
	 * @param actionInfo
	 * @return
	 */
	public int insertAction(Activity activity){
		return activityDao.insertAction(activity);
	}
	/**
	 * 
	 * @param activityCoupon
	 */
	public void saveCouponOrUpdate(ActivityCoupon activityCoupon){
		
		List<ActivityCouponGoods> goodslist = new ArrayList<ActivityCouponGoods>();
		List<ActivityCouponCategory> cateList=new ArrayList<ActivityCouponCategory>();
		User user = UserUtils.getUser();
		activityCoupon.setCreateBy(user);
		activityCoupon.setStatus(2);
		//增加新红包
		if (activityCoupon.getId().equals("0")) {
			activityCoupon.setCouponNumber(activityCoupon.getTotalNumber());
			saveCoupon(activityCoupon);
			
			if (activityCoupon.getUsedType() == 2) {			//指定分类
				cateList = CouponCategoryTolist(activityCoupon);
				if (cateList.size() > 0) {
					insertActivityCouponCategory(cateList);
				}

			}
			if (activityCoupon.getUsedType() == 3) {		//指定商品
				goodslist = CouponTolist(activityCoupon);
				if (goodslist.size() > 0) {
					insertActivityCouponGoods(goodslist);
				}

			}
		} else {
			//修改红包信息
			activityCoupon.setCouponNumber(activityCoupon.getTotalNumber());
			couponUpdate(activityCoupon);
			if(activityCoupon.getUsedType() == 2){
				deleteCoupCate(Integer.parseInt(activityCoupon.getId()));
				cateList = CouponCategoryTolist(activityCoupon);
				if (cateList.size() > 0) {
					insertActivityCouponCategory(cateList);
				}
			}
			
			if (activityCoupon.getUsedType() == 3) {
				deleteCoupGood(Integer.parseInt(activityCoupon.getId()));
				goodslist = CouponTolist(activityCoupon);
				if (goodslist.size() > 0) {
					insertActivityCouponGoods(goodslist);
				}

			}
		}
	}
	
	
	/**
	 * 红包明细分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<ActivityCouponUser> findCouponUser(Page<ActivityCouponUser> page, ActivityCouponUser activityCouponUser) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		if(activityCouponUser.getBegtime() == null && activityCouponUser.getEndtime() == null){
			activityCouponUser.setBegtime(new Date());
			activityCouponUser.setEndtime(new Date());
		}
		
		// 设置分页参数
		activityCouponUser.setPage(page);
		// 执行分页查询
		page.setList(activityCouponUserDao.findList(activityCouponUser));
		return page;
	}
	
	/**
	 * 红包明细分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public List<ActivityCouponUser> exportCouponUser( ActivityCouponUser activityCouponUser) {
		return activityCouponUserDao.exportfindList(activityCouponUser);
	}
	
	/**
	 * 保存红包
	 * @param activityCoupon
	 * @return
	 */
	public int saveCoupon(ActivityCoupon activityCoupon){
		activityCouponDao.insert(activityCoupon);
		int result =Integer.parseInt(activityCoupon.getId());
		if(0 == result){
			try {
				throw new Exception("保存失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	/**
	 * 保存分类
	 * @param list
	 * @return
	 */
	public int insertActivityCouponCategory(List<ActivityCouponCategory> list){
		return activityCouponCategoryDao.insertActivityCouponCategory(list);
	}
	
	/**
	 * 保存指定商品
	 * @param list
	 * @return
	 */
	public int insertActivityCouponGoods(List<ActivityCouponGoods> list){
		return activityCouponGoodsDao.insertActivityCouponGoods(list);
	}
	
	/**
	 * 红包修改
	 * @param activityCoupon
	 * @return
	 */
	public int couponUpdate(ActivityCoupon activityCoupon){
		return activityCouponDao.update(activityCoupon);
	}
	/**
	 * 删除分类
	 * @param id
	 * @return
	 */
	public int deleteCoupCate(int id){
		return activityCouponCategoryDao.deleteCoupCate(id);
	}
	/**
	 * 删除指定商品
	 * @param id
	 * @return
	 */
	public int deleteCoupGood(int id){
		return activityCouponGoodsDao.deleteCoupGood(id);
	}
	
	
	/**
	 * 查询红包下的指定分类
	 * @param id
	 * @return
	 */
	public List<ActivityCouponCategory> couponCateList(int id){
		return activityCouponCategoryDao.couponCateList(id);
	}
	
	/**
	 * 查询红包下的指定商品
	 * @param id
	 * @return
	 */
	public List<ActivityCouponGoods> CouponGoodslist(int id){
		return activityCouponGoodsDao.CouponGoodslist(id);
	}
	
	
	
	/**
	 * 开启，关闭状态
	 * @param actionInfo
	 * @return
	 */
	public int updateStatus(Activity activity){
		return activityDao.updateStatus(activity);
	}
	
	
	/**
	 * 红包开启，关闭状态
	 * @param actionInfo
	 * @return
	 */
	public int updateCouponStatus(ActivityCoupon activityCoupon){
		return activityCouponDao.updateCouponStatus(activityCoupon);
	}
	
	/**
	 * 查询红包是否有领取记录
	 * @param goodsId
	 * @return
	 */
	public int numByCouponId(int id){
		return activityDao.numByCouponId(id);
	}
	
	
	/**
	 * 更新库存
	 * @param map
	 * @return
	 */
	public int modifyCouponNumber(Map<String, Object> map){
		return activityCouponDao.modifyCouponNumber(map);
	}
	
	/**
	 * 查询活动下已开启的红包
	 * @param actionId
	 * @return
	 */
	public List<ActivityCoupon> findByCouIdList(int actionId){
		return activityCouponDao.findByCouIdList(actionId);
	}
	
	/**
	 * 推送内部红包
	 * @param activityCouponUser
	 * @return
	 */
	public int insertSend(ActivityCouponUser activityCouponUser){
		return activityCouponUserDao.insertSend(activityCouponUser);
	}
	
	/**
	 * 查询
	 * @param activityCouponUser
	 * @return
	 */
	public int findByAIdandUserId(ActivityCouponUser activityCouponUser){
		return activityCouponUserDao.findByAIdandUserId(activityCouponUser);
	}
	
	/**
	 * 指定用户插入
	 * @param activityCouponUser
	 * @return
	 */
	public int insertUserCoupon(ActivityCouponUser activityCouponUser){
		return activityCouponUserDao.insertUserCoupon(activityCouponUser);
	}
	
	/**
	 * 推送内部红包
	 * @param activityCouponUser
	 * @return
	 */
	public int insertFeiSend(ActivityCouponUser activityCouponUser){
		return activityCouponUserDao.insertFeiSend(activityCouponUser);
	}
	
	public Users findByMobile(String mobile){
		return mtmyUsersDao.getUsersBy(mobile);
	}
	
	public Users findByUserId(String userid){
		return mtmyUsersDao.get(userid);
	}
	/**
	 * 获取数据红包list
	 * @param id
	 * @return
	 */
	public List<ActivityCoupon> Couponlist(String id){
		return activityCouponDao.Couponlist(id);
	}
	/**
	 * 更新数据
	 * @param actionInfo
	 * @return
	 */
	public int update(Activity activity){
		return activityDao.update(activity);
	}

	/**
	 *定时器 过期的数据
	 * @return
	 */
	
	public List<Activity> selectActionCloseTime(){
		return activityDao.selectActionCloseTime();
	}
	/**
	 * 更改已过期活动状态
	 * @param id
	 * @return
	 */
	public int updateOutTime(int id){
		return activityDao.updateOutTime(id);
	}
	/**
	 * 优惠卷数组拼接
	 * 
	 * @param user
	 * @return
	 */
	public List<ActivityCouponGoods> CouponTolist(ActivityCoupon activityCoupon) {
		List<ActivityCouponGoods> list = new ArrayList<ActivityCouponGoods>();
			if (activityCoupon.getGoodsId().length() > 0) {
				String goodid = activityCoupon.getGoodsId();
				String goodname = activityCoupon.getGoodsName();
				String[] idarry = goodid.split(",");
				String[] namearry = goodname.split(",");
				for (int i = 0; i < idarry.length; i++) {
					ActivityCouponGoods couponegoods = new ActivityCouponGoods();
					couponegoods.setCouponId(Integer.parseInt(activityCoupon.getId()));
					couponegoods.setGoodsId(Integer.parseInt(idarry[i]));
					couponegoods.setGoodName(namearry[i]);
					list.add(couponegoods);
				}

			}
		

		return list;
	}
	/**
	 * 商品分类拼接
	 * @param coupon
	 * @return
	 */
	public List<ActivityCouponCategory> CouponCategoryTolist(ActivityCoupon activityCoupon) {
		List<ActivityCouponCategory> list = new ArrayList<ActivityCouponCategory>();
			if(activityCoupon.getCateId().length()>0){
				String[] idarry = activityCoupon.getCateId().split(",");
				String[] namearry = activityCoupon.getCateName().split(",");
				for (int i = 0; i < idarry.length; i++) {
					ActivityCouponCategory couponCategory = new ActivityCouponCategory();
					couponCategory.setCouponId(Integer.parseInt(activityCoupon.getId()));
					couponCategory.setCategoryId(Integer.parseInt(idarry[i].toString()));
					couponCategory.setCategoryName(namearry[i].toString());
					
					list.add(couponCategory);
				}
		}
		

		return list;
	}
	
	
	/**
	 * 查询内部用户数
	 * @return
	 */
	public int queryInnerUserNum(){
		return activityCouponUserDao.queryInnerUserNum();
	}
	
	/**
	 * 查询非内部用户数
	 * @return
	 */
	public int queryNotInnerUserNum(){
		return activityCouponUserDao.queryNotInnerUserNum();
	}
	
	/**
	 * 删除活动对应的红包
	 * @param id
	 */
	public void delCoupon(String id){
		activityCouponDao.delCoupon(id);
	}
}
