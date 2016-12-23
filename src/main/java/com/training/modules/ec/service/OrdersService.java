package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.engine.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.AcountLogDao;
import com.training.modules.ec.dao.ActivityCouponUserDao;
import com.training.modules.ec.dao.CouponUserDao;
import com.training.modules.ec.dao.GoodsCategoryDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.dao.GoodsSpecPriceDao;
import com.training.modules.ec.dao.IntegralLogDao;
import com.training.modules.ec.dao.MtmyRuleParamDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.PaymentDao;
import com.training.modules.ec.dao.TradingLogDao;
import com.training.modules.ec.entity.AcountLog;
import com.training.modules.ec.entity.CouponUser;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.IntegralLog;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsCoupon;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Payment;
import com.training.modules.ec.entity.TradingLog;
import com.training.modules.ec.entity.Users;
import com.training.modules.quartz.tasks.OrderTimeOut;
import com.training.modules.sys.dao.AreaDao;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.utils.UserUtils;

/**
 * 订单service
 * 
 * @author yangyang
 *
 */

@Service
@Transactional(readOnly = false)
public class OrdersService extends TreeService<OrdersDao, Orders> {

	@Autowired
	private OrdersDao ordersDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private GoodsCategoryDao goodsCategoryDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsSpecPriceDao goodsSpecPriceDao;
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private MtmyRuleParamDao mtmyRuleParamDao;
	@Autowired
	private TradingLogDao tradingLogDao;
	@Autowired
	private IntegralLogDao integralLogDao;
	@Autowired
	private CouponUserDao couponUserDao;
	@Autowired
	private AcountLogDao acountLogDao;
	@Autowired
	private OrderTimeOut orderTimeOut;
	@Autowired
	private ActivityCouponUserDao activityCouponUserDao;

	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<Orders> findOrders(Page<Orders> page, Orders orders) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//	orders.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		// 设置分页参数
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.findList(orders));
		return page;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<Orders> findOrdersExcal(Page<Orders> page, Orders orders) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//	orders.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		// 设置分页参数
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.findAlllist(orders));
		return page;
	}
	
	

	/**
	 * 更新订单状态
	 * 
	 * @param orders
	 */

	public void UpdateOrders(Orders orders) {
		if (orders.getCity() != null) {

			if (orders.getCity().length() > 0) {
				Area area = areaDao.selectByXid(orders.getCity());
				String address = "" + area.getPname() + area.getCname() + area.getXname() + orders.getAddress();
				orders.setAddress(address);
			}
		}
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		if(payment!=null){
			orders.setPayid(payment.getPayid());
			orders.setPayname(payment.getPayname());
		}
		insertLog(orders); // 插入修改日志
		ordersDao.UpdateOrders(orders);
		if (orders.getOrderstatus() == -2) {
			if (orders.getOldstatus() == -1) {
				orderStatusCancel(orders); // 未支付改为取消状态 释放 红包 金币 余额
				List<OrderGoods> list=orderGoodsDao.findListByOrderid(orders.getOrderid());
				orderTimeOut.oto(list);
			}
		}

		if (orders.getOrderamount() > 0) { // 订单金额大于0
			if (orders.getOrderstatus() == 1) { // 此状态为未支付改成已支付
				if (orders.getOldstatus() == -1) {
					orderStatusQue(orders); // 未支付改成已付款 验证是否为邀请首单 增加金币 颜值
				}

			}
		}

	}
	
	/**
	 * 更新物流信息
	 * 
	 * @param orders
	 */

	public int UpdateShipping(Orders orders) {
		int num=ordersDao.UpdateShipping(orders);
		return num;
	}
	
	
	

	/**
	 * 新建订单保存
	 * 
	 * @param orders
	 */
	public void saveOrder(Orders orders) {
		Users user = mtmyUsersDao.getUsersBy(orders.getMobile());
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		Goods goods = goodsDao.getgoods("" + orders.getGoodsid());
		if (orders.getCity() != null) {
			if (orders.getCity().length() > 0) {
				Area area = areaDao.selectByXid(orders.getCity());
				String address = "" + area.getPname() + area.getCname() + area.getXname() + orders.getAddress();
				orders.setAddress(address);
			}
		}

		if (goods.getIsReal().equals("1")) {
			orders.setGoodsnum(1);
		}

		Date date = new Date();
		SimpleDateFormat simd = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String str = simd.format(date);
		String strUserid = str + user.getUserid();
		orders.setUserid(user.getUserid());
		orders.setGoodsprice(orders.getGoodsnum() * orders.getCouponprice());
		orders.setOrderid(strUserid);
		//orders.setOrderstatus(1);
		orders.setCreatelogo(2);
		orders.setMobile(orders.getMobile1());
		orders.setPayid(payment.getPayid());
		orders.setPayname(payment.getPayname());

		if (orders.getOffice() != null) {
			if (orders.getOffice().getId().length() > 0) {
				OfficeInfo officeInfo = officeDao.OfficeInfoByid(orders.getOffice().getId());
				orders.setConsignee(orders.getOffice().getName());
				orders.setAddress(officeInfo.getDetailedAddress());
				orders.setMobile(officeInfo.getStorePhone());
			}
		}

		ordersDao.saveOrder(orders); // 保存订单
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setOrderid(strUserid);
		orderGoods.setSpeckey(orders.getSpeckey());
		orderGoods.setSpeckeyname(orders.getSpeckeyname());
		orderGoods.setMarketprice(orders.getMarketprice());
		orderGoods.setGoodsnum(orders.getGoodsnum());
		orderGoods.setGoodsprice(orders.getCouponprice());
		orderGoods.setUserid(user.getUserid());
		orderGoods.setGoodsid(orders.getGoodsid());
		orderGoods.setGoodsname(orders.getGoodsname());
		orderGoods.setGoodssn(goods.getGoodsSn());
		orderGoods.setIsreal(Integer.parseInt(goods.getIsReal()));
		orderGoods.setOriginalimg(goods.getOriginalImg());
		orderGoods.setRemaintimes(orders.getRemaintimes());
		orderGoods.setServicemin(goods.getServiceMin());
		if (orders.getSpeckey() != "") {
			GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
			goodsSpecPrice.setGoodsId("" + orders.getGoodsid());
			goodsSpecPrice.setSpecKey(orders.getSpeckey());
			GoodsSpecPrice goodspec = goodsSpecPriceDao.getSpecPrce(goodsSpecPrice);
			if (goodspec != null) {
				orderGoods.setExpiringDate(goodspec.getExpiringDate());
				orderGoods.setBarcode(goodspec.getBarCode());
				orderGoods.setServicetimes(goodspec.getServiceTimes());
			}

		}

		orderGoodsDao.saveOrderGoods(orderGoods); // 保存订单商品
		newOrderReward(orders, user, strUserid); // 新订单 奖励提成

	}

	/**
	 * 插入订单修改日志
	 * 
	 * @param orders
	 */
	public void insertLog(Orders orders) {
		String currentUser = UserUtils.getUser().getName();
		AcountLog acountLog = new AcountLog();
		acountLog.setOperator(currentUser);
		acountLog.setOrderid(orders.getOrderid());
		Orders newOrders = ordersDao.get(orders.getOrderid());
		String orderstatusname = null;
		switch (newOrders.getOrderstatus()) {
		case -2:
			orderstatusname = "取消订单";
			break;
		case -1:
			orderstatusname = "未付款";
			break;
		case 1:
			orderstatusname = "已付款";
			break;
		case 2:
			orderstatusname = "已发货";
			break;
		case 3:
			orderstatusname = "已退款";
			break;
		case 4:
			orderstatusname = "已完成";
			break;
		case 5:
			orderstatusname = "申请退款";
			break;
		}
		String time = null;
		if (newOrders.getShippingtime() != null) {
			time = DateUtils.format(newOrders.getShippingtime(), "yyyy-MM-dd");
		}
		String string = "支付方式:" + orders.getPayname() + "|订单状态:" + orderstatusname + "|修改价格:"
				+ newOrders.getOrderamount() + "</br>发货时间:" + time + "|快递单号:" + newOrders.getShippingcode() + "|快递公司:"
				+ newOrders.getShippingname() + "</br>收货人姓名:" + newOrders.getConsignee() + "街道地址:"
				+ newOrders.getAddress() + "|邮政编码:" + newOrders.getPostalcode() + "</br>手机号码:" + newOrders.getMobile()
				+ "|固定电话:" + newOrders.getPhone() + "|备注:" + newOrders.getAdminnote();
		String str = string.replaceAll("null", "");
		acountLog.setLogdesc(str);
		acountLogDao.insertLog(acountLog);

	}

	/**
	 * 未支付改成取消状态
	 * 
	 * @param orders
	 */

	public void orderStatusCancel(Orders orders) {

		CouponUser couponUser = couponUserDao.findByorderId(orders.getOrderid());
		if (couponUser != null) {
			CouponUser copUser = new CouponUser();
			copUser.setStatus("0");
			copUser.setOrderId(orders.getOrderid());
			couponUserDao.UpdateStatus(copUser);

		}
		TradingLog tradingLog = tradingLogDao.findByOrderId(orders.getOrderid());
		if (tradingLog != null) {
			TradingLog tradingLog1 = new TradingLog();
			tradingLog1.setUserId(tradingLog.getUserId());
			tradingLog1.setStatus(1);
			tradingLog1.setMoney(Math.abs(tradingLog.getMoney()));
			tradingLog1.setType("取消返还");
			tradingLog1.setOrderId(orders.getOrderid());
			tradingLogDao.insert(tradingLog1); // 插入余额明细
			Users users = new Users();
			users.setUserid(tradingLog.getUserId());
			users.setUsermoney(Math.abs(tradingLog.getMoney()));
			mtmyUsersDao.UpdateUserMoney(users); // 返还金额
		}
		IntegralLog integralLog = integralLogDao.findByOrderId(orders.getOrderid());
		if (integralLog != null) {
			Users users = new Users();
			users.setPaypoints(Math.abs(Integer.parseInt(integralLog.getValue())));
			users.setUserid(integralLog.getUserId());
			mtmyUsersDao.UpdateUserPayPost(users); // 返还积分
			IntegralLog integralLog1 = new IntegralLog();
			integralLog1.setUserId(integralLog.getUserId());
			integralLog1.setType(0);
			integralLog1.setOrderId(orders.getOrderid());
			integralLog1.setRemark("取消返还");
			integralLog1.setValue(Math.abs(Integer.parseInt(integralLog.getValue())) + "");
			integralLogDao.insert(integralLog1); // 插入积分明细
		}

	}

	/**
	 * 未支付改成已付款状态
	 * 
	 * @param orders
	 */
	public void orderStatusQue(Orders orders) {
//		InvitationUser invitationUser = invitationUserDao.findByUserId(orders.getUserid()); // 拿去用户的邀请码
//																							// 是否为首单
//		if (invitationUser != null) {
//			MtmyRuleParam mtmyRuleParam = mtmyRuleParamDao.findProByKey("commission_rmb");
//			double tiPrice = orders.getOrderamount() * Double.parseDouble(mtmyRuleParam.getParamValue());
//			if (invitationUser.getStatus() == 0) {
//				if (invitationUser.getType() == 0) { // 返还给每天每夜用户提成
//					InvitationUser iuUser = new InvitationUser();
//					Users users = new Users();
//					iuUser.setUserId(orders.getUserid());
//					iuUser.setStatus(1);
//					iuUser.setOrderId(orders.getOrderid());
//					iuUser.setOrderPrice(orders.getOrderamount());
//					iuUser.setPrice(tiPrice);
//					invitationUserDao.UpdateInUser(iuUser); // 修改邀请用户 已经不是首单了
//					users.setUserid(Integer.parseInt(invitationUser.getInvitationCode()));
//					users.setUsermoney(tiPrice);
//					mtmyUsersDao.UpdateUserMoney(users); // 赠送邀请提成
//					TradingLog tradingLog = new TradingLog();
//					tradingLog.setUserId(Integer.parseInt(invitationUser.getInvitationCode()));
//					tradingLog.setStatus(1);
//					tradingLog.setMoney(tiPrice);
//					tradingLog.setType("邀请提成");
//					tradingLog.setOrderId(orders.getOrderid());
//					tradingLogDao.insert(tradingLog); // 插入余额明细
//				} else { // 返还给妃子校用户提成
//					InvitationUser iuUser = new InvitationUser();
//					iuUser.setUserId(orders.getUserid());
//					iuUser.setStatus(1);
//					iuUser.setOrderId(orders.getOrderid());
//					iuUser.setOrderPrice(orders.getOrderamount());
//					iuUser.setPrice(tiPrice);
//					invitationUserDao.UpdateInUser(iuUser); // 修改邀请用户 已经不是首单了
//					User user = new User();
//					user.setId(invitationUser.getInvitationCode());
//					user.setUsermoney(tiPrice);
//					userDao.UpdateUserMoney(user);
//					TradingFZLog tradingLog = new TradingFZLog();
//					tradingLog.setUserId(invitationUser.getInvitationCode());
//					tradingLog.setStatus(1);
//					tradingLog.setMoney(tiPrice);
//					tradingLog.setType("邀请提成");
//					tradingLog.setOrderId(orders.getOrderid());
//					tradingLogFZDao.insert(tradingLog); // 插入妃子校余额明细
//				}
//
//			}
//		}
//		MtmyRuleParam mtmyRuleParam1 = mtmyRuleParamDao.findProByKey("buy_gd");
		MtmyRuleParam mtmyRuleParam2 = mtmyRuleParamDao.findProByKey("buy_lv");
		Users users = new Users();
		double levelValue = Double.parseDouble(mtmyRuleParam2.getParamValue()) * orders.getOrderamount();
//		double payPoints = Double.parseDouble(mtmyRuleParam1.getParamValue()) * orders.getOrderamount();
		users.setPaypoints(0);
		users.setLevelValue((int) levelValue);
		users.setUserid(orders.getUserid());
		mtmyUsersDao.UpdateUserPayPost(users); // 购买成功赠送用户积分
//		IntegralLog integralLog = new IntegralLog();
//		integralLog.setUserId(orders.getUserid());
//		integralLog.setType(0);
//		integralLog.setOrderId(orders.getOrderid());
//		integralLog.setRemark("购买奖励");
//		integralLog.setValue(((int) payPoints) + "");
//		integralLogDao.insert(integralLog); // 插入积分明细

	}

	public void newOrderReward(Orders orders, Users user, String strUserid) {
//		InvitationUser invitationUser = invitationUserDao.findByUserId(user.getUserid()); // 去拿用户的邀请码
//																							// 是否为首单
//		if (invitationUser != null) {
//			if (invitationUser.getStatus() == 0) {
//				MtmyRuleParam mtmyRuleParam = mtmyRuleParamDao.findProByKey("commission_rmb");
//				double tiPrice = orders.getOrderamount() * Double.parseDouble(mtmyRuleParam.getParamValue());
//				if (invitationUser.getType() == 0) { // 每天每夜返现提成
//					InvitationUser iuUser = new InvitationUser();
//					Users users = new Users();
//					iuUser.setUserId(user.getUserid());
//					iuUser.setStatus(1);
//					iuUser.setOrderId(strUserid);
//					iuUser.setOrderPrice(orders.getOrderamount());
//					iuUser.setPrice(tiPrice);
//					invitationUserDao.UpdateInUser(iuUser); // 修改邀请用户 已经不是首单了
//					users.setUserid(Integer.parseInt(invitationUser.getInvitationCode()));
//					users.setUsermoney(tiPrice);
//					mtmyUsersDao.UpdateUserMoney(users); // 赠送邀请提成
//					TradingLog tradingLog = new TradingLog();
//					tradingLog.setUserId(Integer.parseInt(invitationUser.getInvitationCode()));
//					tradingLog.setStatus(1);
//					tradingLog.setMoney(tiPrice);
//					tradingLog.setType("邀请提成");
//					tradingLog.setOrderId(strUserid);
//					tradingLogDao.insert(tradingLog); // 插入余额明细
//				} else { // 妃子校返现提成
//					InvitationUser iuUser = new InvitationUser();
//					iuUser.setUserId(orders.getUserid());
//					iuUser.setStatus(1);
//					iuUser.setOrderId(orders.getOrderid());
//					iuUser.setOrderPrice(orders.getOrderamount());
//					iuUser.setPrice(tiPrice);
//					invitationUserDao.UpdateInUser(iuUser); // 修改邀请用户 已经不是首单了
//					User user1 = new User();
//					user1.setId(invitationUser.getInvitationCode());
//					user1.setUsermoney(tiPrice);
//					userDao.UpdateUserMoney(user1);
//					TradingFZLog tradingLog = new TradingFZLog();
//					tradingLog.setUserId(invitationUser.getInvitationCode());
//					tradingLog.setStatus(1);
//					tradingLog.setMoney(tiPrice);
//					tradingLog.setType("邀请提成");
//					tradingLog.setOrderId(orders.getOrderid());
//					tradingLogFZDao.insert(tradingLog); // 插入妃子校余额明细
//				}
//
//			}
//		}

		//MtmyRuleParam mtmyRuleParam1 = mtmyRuleParamDao.findProByKey("buy_gd");
		MtmyRuleParam mtmyRuleParam2 = mtmyRuleParamDao.findProByKey("buy_lv");
		Users users = new Users();
		double levelValue = Double.parseDouble(mtmyRuleParam2.getParamValue()) * orders.getOrderamount();
	//	double payPoints = Double.parseDouble(mtmyRuleParam1.getParamValue()) * orders.getOrderamount();
		users.setPaypoints(0);
		users.setLevelValue((int) levelValue);
		users.setUserid(user.getUserid());
		mtmyUsersDao.UpdateUserPayPost(users); // 购买成功赠送用户积分
//		IntegralLog integralLog = new IntegralLog();
//		integralLog.setUserId(user.getUserid());
//		integralLog.setType(0);
//		integralLog.setOrderId(strUserid);
//		integralLog.setRemark("购买奖励");
//		integralLog.setValue(((int) payPoints) + "");
//		integralLogDao.insert(integralLog); // 插入积分明细
	}

	/**
	 * 查询一级分类
	 * 
	 * @return
	 */
	public List<GoodsCategory> cateList() {
		return goodsCategoryDao.catelist();
	}

	/**
	 * 二级查询
	 * 
	 * @return
	 */
	public List<GoodsCategory> catetwolist(String id) {
		return goodsCategoryDao.catetwolist(id);
	}

	/**
	 * 根据分类查询商品
	 * 
	 * @param id
	 * @return
	 */
	public List<Goods> goodslist(Goods goods) {
		return goodsDao.goodslist(goods);
	}

	/**
	 * 获取商品信息
	 * 
	 * @param id
	 * @return
	 */
	public Goods getgoods(String id) {
		return goodsDao.getgoods(id);
	}

	/**
	 * 根据商品id查询商品规格
	 * 
	 * @param id
	 * @return
	 */
	public List<GoodsSpecPrice> speclist(String id) {
		return goodsSpecPriceDao.speclistBygoods(id);
	}

	/**
	 * 根据商品规格key查询商品信息
	 * 
	 * @param id
	 * @return
	 */
	public GoodsSpecPrice getSpecPrce(GoodsSpecPrice goodsSpec) {
		return goodsSpecPriceDao.getSpecPrce(goodsSpec);
	}

	/**
	 * 查询过期文支付订单
	 * 
	 * @param minute
	 *            过期时间
	 * @return
	 */
	public List<Orders> queryNotPayOrder(Map<String, Object> map) {
		return this.ordersDao.queryNotPayOrder(map);
	}

	/**
	 * 修改订单状态
	 * 
	 * @param map
	 * @return
	 */
	public int modifyOrderStatus(Map<String, Object> map) {
		return this.ordersDao.modifyOrderStatus(map);
	}
	/**
	 * 查询分销中是否有该订单
	 * @param orderId
	 * @return
	 */
	public int selectSaleByOrderid(String orderId){
		return ordersDao.selectSaleByOrderid(orderId);
	}
	
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public int updateSale(String orderId){
		return ordersDao.updateSale(orderId);
	}
	
	/**
	 * 查询订单使用的红包
	 * @param orderId
	 * @return
	 */
	public List<OrderGoodsCoupon> couplist(String orderId){
		return activityCouponUserDao.findlistByOrdeid(orderId);
	}
	
}
