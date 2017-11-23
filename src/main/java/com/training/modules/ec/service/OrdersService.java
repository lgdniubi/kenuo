package com.training.modules.ec.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.common.utils.BeanUtil;
import com.training.modules.crm.entity.CrmOrders;
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
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.dao.OrderPushmoneyRecordDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.PaymentDao;
import com.training.modules.ec.dao.TradingLogDao;
import com.training.modules.ec.dao.UserAccountsLogDao;
import com.training.modules.ec.entity.AcountLog;
import com.training.modules.ec.entity.CouponUser;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsDetailSum;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.IntegralLog;
import com.training.modules.ec.entity.IntegralsLog;
import com.training.modules.ec.entity.MtmyRuleParam;
import com.training.modules.ec.entity.OfficeAccount;
import com.training.modules.ec.entity.OfficeAccountLog;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsCoupon;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.OrderInvoiceRelevancy;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.OrderRechargeLog;
import com.training.modules.ec.entity.OrderRemarksLog;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Payment;
import com.training.modules.ec.entity.PushmoneyRecordLog;
import com.training.modules.ec.entity.TradingLog;
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.ec.entity.UserAccountsLog;
import com.training.modules.ec.entity.Users;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.OrderTimeOut;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.dao.AreaDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.utils.ScopeUtils;

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
	@Autowired
	private OrderGoodsDetailsService orderGoodsDetailsService;
	@Autowired
	private OrderRechargeLogService orderRechargeLogService;
	@Autowired
	private OrderPushmoneyRecordService orderPushmoneyRecordService;
	@Autowired
	private OrderInvoiceService orderInvoiceService;
	@Autowired
	private OrderGoodsDetailsDao orderGoodsDetailsDao;
	@Autowired
	private OrderPushmoneyRecordDao orderPushmoneyRecordDao;
	@Autowired
	private UserAccountsLogDao userAccountsLogDao;
	@Autowired
	private TurnOverDetailsService turnOverDetailsService;
	
	public static final String MTMY_ID = "mtmy_id_";//用户云币缓存前缀
	
	protected static RedisClientTemplate redisClientTemplate;
	static{
		redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
	}
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<Orders> findOrders(Page<Orders> page, Orders orders) {
		if(orders.getBegtime() == null && orders.getEndtime() == null){
			orders.setBegtime(new Date());
			orders.setEndtime(new Date());
		}
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//orders.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		orders.getSqlMap().put("dsf",ScopeUtils.dataScopeFilter("a", "orderOrRet"));
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
	public Page<Orders> newFindOrders(Page<Orders> page, Orders orders) {
		if(orders.getBegtime() == null && orders.getEndtime() == null){
			orders.setBegtime(new Date());
			orders.setEndtime(new Date());
		}
		// 设置分页参数
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.newFindList(orders));
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
		orders.getSqlMap().put("dsf",ScopeUtils.dataScopeFilter("a", "orderOrRet"));
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.findAlllist(orders));
		return page;
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<Orders> newFindOrdersExcal(Page<Orders> page, Orders orders) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//	orders.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		// 设置分页参数
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.newFindAlllist(orders));
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
	 * 
	 * @param orderid
	 * @return
	 */
	public Orders findselectByOrderId(String orderid){
		return dao.findselectByOrderId(orderid);
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
		String orderstatusname = null;
		switch (orders.getOrderstatus()) {
		case -2:
			orderstatusname = "取消订单";
			break;
		case -1:
			orderstatusname = "待付款";
			break;
		case 1:
			orderstatusname = "待发货";
			break;
		case 2:
			orderstatusname = "待收货";
			break;
		case 3:
			orderstatusname = "已退款";
			break;
		case 4:
			orderstatusname = "已完成";
			break;
		}
		String string = "支付方式:" + orders.getPayname() + "|订单状态:" + orderstatusname + "</br>|物流类型:"
				+ orders.getShippingtype() + "|收货人:" + orders.getConsignee() + "|联系电话:" + orders.getMobile() + "|收货地址:"
				+ orders.getAddress();
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
	 * 强制取消
	 * @return
	 */
	public int updateOrderStatut(String orderid){
		return ordersDao.updateOrderStatut(orderid);
	}
	/**
	 * 查询订单使用的红包
	 * @param orderId
	 * @return
	 */
	public List<OrderGoodsCoupon> couplist(String orderId){
		return activityCouponUserDao.findlistByOrdeid(orderId);
	}
	/**
	 * 查询用户的等级折扣
	 * @param mobile
	 * @return
	 */
	public String getUserLevel(String mobile){
		return ordersDao.getUserLevel(mobile);
	}

	/**
	 * 通过手机号码 查询用户信息
	 * @param mobile
	 * @return
	 */
	public Orders getUser(String mobile) {
		return ordersDao.getUser(mobile);
	}
	
	/**
	 * 通过手机号码 查询妃子校用户信息
	 * @param mobile
	 * @return
	 */
	public Orders getSysUser(String mobile) {
		return ordersDao.getSysUser(mobile);
	}
	
	/**
	 * 根据Orderid查找相应的商品信息
	 * @param orderId
	 * @return
	 */
	public List<OrderGoods> selectOrderGoodsByOrderid(String orderId){
		return orderGoodsDao.selectOrderGoodsByOrderid(orderId);
	}

	/**
	 * 保存虚拟订单saveVirtualOrder
	 * @param orders
	 */
	public void saveVirtualOrder(Orders orders) {
		DecimalFormat formater = new DecimalFormat("#0.##");
		User user = UserUtils.getUser(); //登陆用户
		int mtmyUserId = orders.getUserid();	//每天每耶用户id
		String orderid = createOrder(mtmyUserId, 0, 0);//订单id
		orders.setOrderid(orderid);
		orders.setCreateBy(user);
		//页面传递到后台的数据
		List<Integer> goodselectIds = orders.getGoodselectIds();	//商品id集合
		List<String> speckeys = orders.getSpeckeys();				//规格key集合
		List<Double> orderAmounts = orders.getOrderAmounts();		//成交价集合
		List<Double> actualPayments = orders.getActualPayments();	//实际付款集合
		List<Integer> remaintimeNums = orders.getRemaintimeNums();	//虚拟订单老产品-实际次数
		List<Date> realityAddTimeList = orders.getRealityAddTimeList();  //实际下单时间集合
		double orderAmountSum = 0d;  //应付总额
		double afterPaymentSum = 0d;  //实际付款总额
		double debtMoneySum = 0d;	//总欠款
		double spareMoneySum = 0d;	//总余额
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于应付时，将多的存入个人账户余额中)
		double goodsprice = 0;  //商品总价
		
		String recid = "";//保存mappingid到账户充值记录表中
		
		for (Integer i = 0; i < goodselectIds.size(); i++) {
			Integer goodselectId = goodselectIds.get(i);		//商品id
			String speckey = speckeys.get(i);					//规格key
			Double orderAmount = orderAmounts.get(i);			//应付金额
			orderAmountSum = orderAmountSum + orderAmount;		//应付总额
			Double actualPayment = actualPayments.get(i);		//实际付款(前)
			
			//通过商品id获取当前商品
			Goods goods = goodsDao.getgoods(goodselectId.toString());
			//通过规格key和商品id查询商品对应规格
			GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
			goodsSpecPrice.setGoodsId(goodselectId.toString());
			goodsSpecPrice.setSpecKey(speckey);
			GoodsSpecPrice goodspec = goodsSpecPriceDao.getSpecPrce(goodsSpecPrice);//查询当前商品对应的规格
			int serviceTimes = goodspec.getServiceTimes();	//规格服务次数
			double price = goodspec.getPrice();	//优惠价格
			double marketPrice = goodspec.getMarketPrice();	//市场单价
			double costprice = goodspec.getCostPrice(); //系统价
			goodsprice += price;
			
			double _afterPayment;//实际付款（后）
			double _spareMoney;	//订单余款
			double _debtMoney;	//订单欠款
			double _singleRealityPrice;		//实际服务单次价
			double _singleNormPrice;	//单次标价
			int _actualNum;	//剩余服务次数
			if(serviceTimes == 999){       //时限卡单独处理，当实付小于应付的时候才会用以下的字段里的值，不信看下面代码
				_afterPayment = actualPayment;//实际付款（后）
				_spareMoney = 0;	//订单余款
				_debtMoney = Double.parseDouble(formater.format(orderAmount - actualPayment));	//订单欠款
				_singleRealityPrice = 0;		//实际服务单次价
				_singleNormPrice = 0;	//单次标价
				_actualNum = 1;	//剩余服务次数
			}else{
				//获取计算后的一些费用
				Orders computingCost = computingCost(orderAmount, actualPayment, serviceTimes, price);	//计算获取所有价钱
				_afterPayment = computingCost.getAfterPayment();//实际付款（后）
				_spareMoney = computingCost.getSpareMoney();	//订单余款
				_debtMoney = computingCost.getDebtMoney();	//订单欠款
				_singleRealityPrice = computingCost.getSingleRealityPrice();		//实际服务单次价
				_singleNormPrice = computingCost.getSingleNormPrice();	//单次标价
				_actualNum = computingCost.getActualNum();	//剩余服务次数
			}
			
			//添加商品订单"mtmy_order_goods_mapping"
			double totalAmount = 0; //实付款金额
			double orderBalance = 0; //订单余款
			double orderArrearage = 0; //订单欠款
			double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
			double appTotalAmount = 0; //app实付金额
			double appArrearage = 0;   //app欠款金额
			//当应付等于实付  余额和欠款都为0
			if(orderAmount.equals(actualPayment)){													
				totalAmount = actualPayment;	//实付款金额
				afterPaymentSum = afterPaymentSum + totalAmount;	//实际付款总额
				orderBalance = 0;	//订单余款
				spareMoneySum = spareMoneySum + orderBalance;	//总余额
				orderArrearage = 0;	//订单欠款
				debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
			}else{
				if(actualPayment > orderAmount){  //实付大于应付
					totalAmount = orderAmount;	//计算后实付款金额
					afterPaymentSum = afterPaymentSum + totalAmount;  //实际付款总额
					orderBalance = Double.parseDouble(formater.format(actualPayment-orderAmount));	//订单余款
					spareMoneySum = spareMoneySum+orderBalance;	//总余额
					orderArrearage = 0;	//订单欠款
					debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
					newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
					appTotalAmount = orderAmount;                   //app实付金额
					appArrearage = 0;                                 //app欠款金额
					newSpareMoneySum = newSpareMoneySum + Double.parseDouble(formater.format(actualPayment - orderAmount));//商品总余额
				}else{
					totalAmount = _afterPayment;	//计算后实付款金额
					afterPaymentSum = afterPaymentSum + totalAmount;	////实际付款总额
					orderBalance = _spareMoney;	//订单余款
					spareMoneySum = spareMoneySum + orderBalance;	//总余额
					orderArrearage = _debtMoney;	//订单欠款
					debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
					newOrderBalance = Double.parseDouble(formater.format(actualPayment - _afterPayment));                              //商品余额（只放在details里的OrderBalance）
					appTotalAmount = actualPayment;                   //app实付金额
					appArrearage = Double.parseDouble(formater.format(orderAmount - actualPayment)); //app欠款金额
				}
			}
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderid(orderid);
			orderGoods.setUserid(orders.getUserid());
			orderGoods.setGoodsid(goods.getGoodsId());
			orderGoods.setGoodsname(goods.getGoodsName());
			orderGoods.setGoodssn(goodspec.getGoodsNo());
			orderGoods.setOriginalimg(goods.getOriginalImg());
			orderGoods.setSpeckey(goodspec.getSpecKey());
			orderGoods.setSpeckeyname(goodspec.getSpecKeyValue());
			orderGoods.setCostprice(costprice);		//成本单价
			orderGoods.setMarketprice(marketPrice);		//市场单价
			orderGoods.setGoodsprice(price);	//优惠价
			orderGoods.setGoodsnum(1);	//成交数量
			orderGoods.setOrderAmount(orderAmount);		//应付金额
			orderGoods.setTotalAmount(totalAmount);	//计算后实付款金额
			orderGoods.setOrderBalance(orderBalance);	//订单余款
			orderGoods.setOrderArrearage(orderArrearage); //订单欠款
			orderGoods.setExpiringDate(goodspec.getExpiringDate());
			orderGoods.setSingleRealityPrice(_singleRealityPrice);	//实际服务单次价
			orderGoods.setSingleNormPrice(_singleNormPrice);	//单次标价
			orderGoods.setIsreal(1);	// 是否为虚拟 0 实物 1虚拟
			orderGoods.setServicetimes(serviceTimes);	//预计服务次数
			orderGoods.setServicemin(goods.getServiceMin());//服务时长
			if(orders.getIsNeworder() == 0){
				orderGoods.setRealityAddTime(new Date());   //实际下单时间
			}else{
				orderGoods.setRealityAddTime(realityAddTimeList.get(i));   //实际下单时间
			}
			
			//保存 mtmy_order_goods_mapping
			orderGoodsDao.saveOrderGoods(orderGoods);
			
			recid += "," + orderGoods.getRecid();//保存mappingid到账户充值记录表中
			
			//订单商品详情记录表
			int _serviceTimes = 0;//剩余服务次数
			double _itemCapitalPool = 0;//项目资金池
			
			if(orderAmount.equals(actualPayment)){
				_serviceTimes = serviceTimes; //剩余服务次数等于 规格服务次数
				_itemCapitalPool = orderAmount;
			}else{
				if(actualPayment > orderAmount){  //实付大于应付
					_serviceTimes = serviceTimes; //剩余服务次数等于 规格服务次数
					_itemCapitalPool = orderAmount;
				}else{
					_serviceTimes = _actualNum; //剩余服务次数等于计算后的服务次数
					
					_itemCapitalPool = Double.parseDouble(formater.format(_singleNormPrice*_actualNum));	//项目资金池
				}
			}
			OrderGoodsDetails details = new OrderGoodsDetails();
			details.setOrderId(orderid);
			details.setGoodsMappingId(orderGoods.getRecid()+"");
			if(serviceTimes == 999){           //时限卡时，项目金额和项目资金池都为0
				details.setItemAmount(0);	//项目金额
				details.setItemCapitalPool(0); //项目资金池
			}else{
				details.setItemAmount(totalAmount);	//项目金额
				details.setItemCapitalPool(_itemCapitalPool); //项目资金池
			}
			details.setTotalAmount(totalAmount);	//实付款金额
			details.setOrderBalance(newOrderBalance);		//订单余款
			details.setOrderArrearage(orderArrearage);	//订单欠款
			details.setAppTotalAmount(appTotalAmount);    //app实付金额
			details.setAppArrearage(appArrearage);       //app欠款金额
			int isNeworder = orders.getIsNeworder();//新老订单
			if(isNeworder == 0){
				details.setServiceTimes(_serviceTimes);	//剩余服务次数
			}else{
				details.setServiceTimes(remaintimeNums.get(i));	//剩余服务次数
			}
			details.setType(0);
			details.setAdvanceFlag("0");
			details.setCreateOfficeId(user.getOffice().getId());
			details.setCreateBy(user);
			details.setBelongOfficeId(orders.getBelongOfficeId());
			//保存订单商品详情记录
			orderGoodsDetailsService.saveOrderGoodsDetails(details);
			
			if(orders.getIsNeworder() == 0){
				//同步数据到营业额明细表
				TurnOverDetails turnOverDetails = new TurnOverDetails();
				turnOverDetails.setOrderId(orderid);
				turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
				turnOverDetails.setDetailsId(details.getId());
				turnOverDetails.setType(1);
				turnOverDetails.setAmount(details.getAppTotalAmount());
				turnOverDetails.setUseBalance(details.getUseBalance());
				turnOverDetails.setStatus(0);
				turnOverDetails.setUserId(mtmyUserId);
				turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
				turnOverDetails.setCreateBy(UserUtils.getUser());
				turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
			}
			
		}
		
		/*//订单充值日志表
		OrderRechargeLog  oLog = new OrderRechargeLog();
		oLog.setOrderId(orderid);
		oLog.setMtmyUserId(mtmyUserId);		//每天每耶用户id
		oLog.setRechargeAmount(afterPaymentSum);	//充值金额
		oLog.setAccountBalance(spareMoneySum);	//账户余额
		oLog.setTotalAmount(afterPaymentSum+spareMoneySum);	//实付款金额
		oLog.setCreateBy(user);
		oLog.setCreateDate(new Date());
		orderRechargeLogService.saveOrderRechargeLog(oLog);*/
		
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		//主订单信息
		Orders _orders = new Orders();
		_orders.setOrderid(orderid);
		_orders.setParentid("0");
		_orders.setUserid(mtmyUserId);
		_orders.setOrderstatus(orders.getOrderstatus());
		_orders.setPayid(payment.getPayid());
		_orders.setPaycode(payment.getPaycode());
		_orders.setPayname(payment.getPayname());
		_orders.setGoodsprice(goodsprice);	//商品总价
		_orders.setOrderamount(orderAmountSum); //应付总金额
		_orders.setTotalamount(afterPaymentSum); //实付总额
		_orders.setOrderArrearage(debtMoneySum); //总欠款
		_orders.setOrderBalance(spareMoneySum); //总余额
		_orders.setIsReal(1);	//是否实物（0：实物商品；1：虚拟商品）
		_orders.setIsNeworder(orders.getIsNeworder()); //是否新订单（0：新订单；1：老订单）
		_orders.setDistinction(orders.getDistinction()); //订单性质
		_orders.setOffice(user.getOffice());
		_orders.setCreateBy(user);
		_orders.setDelFlag("0");
		_orders.setChannelFlag("bm");
		_orders.setShippingstatus(2);
		_orders.setShippingtype(2);
		_orders.setUsernote(orders.getUsernote());
		_orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
		_orders.setBelongOfficeId(orders.getBelongOfficeId());
		ordersDao.saveVirtualOrder(_orders);
		
		//根据用户id查询用户账户信息
		Orders account = ordersDao.getAccount(_orders);
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance);
			newAccount.setUserid(_orders.getUserid());
			ordersDao.insertAccount(newAccount);
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}else{
			/*double accountArrearage = account.getAccountArrearage()+debtMoneySum;	//账户欠款信息*/		
			double accountBalance = account.getAccountBalance()+newSpareMoneySum;	//账户余额信息
			/*account.setAccountArrearage(accountArrearage);*/
			account.setAccountBalance(accountBalance);
			ordersDao.updateAccount(account);
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}
		
		
		//保存提成人员信息 
		List<String> sysUserId = orders.getSysUserId();	//提成人员id
		List<Double> pushMoney = orders.getPushMoney();	//提成金额
		if(sysUserId!=null && sysUserId.size()>0){
			OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
			for (int i = 0; i < sysUserId.size(); i++) {
				orderPushmoneyRecord.setPushmoneyUserId(sysUserId.get(i));
				
				//通过业务员id(属于妃子校的)查询业务员归属机构
				orderPushmoneyRecord = orderPushmoneyRecordService.getOfficeIdByUserId(orderPushmoneyRecord);
				
				orderPushmoneyRecord.setOrderId(orderid);
				orderPushmoneyRecord.setPushMoney(pushMoney.get(i));
				orderPushmoneyRecord.setOfficeId(user.getOffice().getId());
				orderPushmoneyRecord.setCreateBy(user);
				orderPushmoneyRecord.setDelFlag("0");
				orderPushmoneyRecordService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
			}
		}
		
		//批量添加订单备注
		if (orders.getOrderRemarks()!=null && orders.getOrderRemarks().size()>0) {
			ordersDao.saveOrderRemarksLog(orders);
		}
		if(orders.getIchecks() == 1){
			String personheadContent = orders.getPersonheadContent();
			String companyheadContent = orders.getCompanyheadContent();//公司发票抬头
			if(!"".equals(companyheadContent)){//等于空 就是个人  不等于空就是 公司
				orders.setHeadContent(companyheadContent);
			}else{
				orders.setHeadContent(personheadContent);
			}
			//保存发票信息
			orderInvoiceService.saveOrderInvoice(orders);
			OrderInvoiceRelevancy orderInvoiceRelevancy = new OrderInvoiceRelevancy();
			orderInvoiceRelevancy.setOrderId(orderid);
			orderInvoiceRelevancy.setInvoiceId(orders.getInvoiceId());
			ordersDao.saveOrderInvoiceRelevancy(orderInvoiceRelevancy);
		}
	}
	
	/**
	 * 计算
	 * @param transactionPrice  应付价
	 * @param actualPayment		实际付款
	 * @param ServiceNum		服务次数
	 * @param favourablePrice	优惠价
	 * @return
	 */
	public static Orders computingCost(double transactionPrice,double actualPayment,int ServiceNum, double favourablePrice){
		DecimalFormat formater = new DecimalFormat("#0.##");
		Orders orders = new Orders();
		double singleRealityPrice = Double.parseDouble(formater.format(transactionPrice/ServiceNum)); //实际服务单次价
		double singleNormPrice = Double.parseDouble(formater.format(favourablePrice/ServiceNum));	//单次标价
		int actualNum = (int)Math.floor(actualPayment/singleRealityPrice);	//实际服务次数 = 剩余服务次数
		double spareMoney = Double.parseDouble(formater.format(actualPayment - singleRealityPrice*actualNum)); //余款
		double afterPayment = Double.parseDouble(formater.format(actualPayment-spareMoney));	//计算后实际付款
		double debtMoney = Double.parseDouble(formater.format(transactionPrice-afterPayment));//欠款
		orders.setActualNum(actualNum);
		orders.setSpareMoney(spareMoney);
		orders.setAfterPayment(afterPayment);
		orders.setDebtMoney(debtMoney);
		orders.setSingleNormPrice(singleNormPrice);
		orders.setSingleRealityPrice(singleRealityPrice);
		return orders;
	}

	/**
	 * 根据订单id查询基本信息
	 * @param orderid
	 * @return
	 */
	public Orders selectOrderById(String orderid) {
		DecimalFormat formater = new DecimalFormat("#0.##");   //四舍五入
		Orders orders = dao.selectOrderById(orderid);
		double goodsprice = 0;		//主订单成本总价
		//查询主订单的计费信息
		Orders _orders = orderGoodsDetailsService.getOrderGoodsDetailListByOid(orders.getOrderid());
		//通过主订单查询商品订单信息
		List<OrderGoods> orderGoodList = orderGoodsDao.getOrderGoodsMapping(orders);
		if(orderGoodList!=null && orderGoodList.size()>0){
			for (OrderGoods orderGoods : orderGoodList) {
				//计算出当前子订单成本总价
				double totalPrice = Double.parseDouble(formater.format(orderGoods.getGoodsprice()*orderGoods.getGoodsnum()));
				goodsprice = Double.parseDouble(formater.format(goodsprice + totalPrice*100)); //总订单的成本价是子订单成本总价的合
				OrderGoods _orderGoods = orderGoodsDetailsService.getOrderGoodsDetailListByMid(orderGoods.getRecid());
				if(_orderGoods!=null){
					//存在预约记录且预约状态为已完成 已评价 爽约
					if(_orderGoods.getSumAppt() != 0 && _orderGoods.getAdvanceFlag() == 1){
						if(orderGoodsDetailsService.findApptStatus(orderGoods.getRecid()) != 0){
							_orderGoods.setSumAppt(1);
						}else{
							_orderGoods.setSumAppt(0);
						}
					}
					orderGoods.setTotalAmount(_orderGoods.getTotalAmount());
					orderGoods.setOrderBalance(_orderGoods.getOrderBalance());
					orderGoods.setOrderArrearage(_orderGoods.getOrderArrearage());
					orderGoods.setTotalPrice(totalPrice);
					orderGoods.setRemaintimes(_orderGoods.getRemaintimes());
					orderGoods.setAdvanceFlag(_orderGoods.getAdvanceFlag());
					orderGoods.setSumAppt(_orderGoods.getSumAppt());
					if(orderGoods.getIsreal() == 2){
						orderGoods.setGoodsBalance(_orderGoods.getSuitCardBalance());
					}else{
						orderGoods.setGoodsBalance(_orderGoods.getGoodsBalance());
					}
				}
			}
		}
//		if(!"bm".equals(orders.getChannelFlag())){
//			orders.setTotalamount(orders.getTotalamount());
//			orders.setOrderBalance(orders.getOrderBalance());
//			orders.setOrderArrearage(orders.getOrderArrearage());
//		}else{
			if(_orders!=null){
				orders.setTotalamount(_orders.getTotalamount());
				orders.setOrderBalance(_orders.getOrderBalance());
				orders.setOrderArrearage(_orders.getOrderArrearage());
			}
//		}
		if(orders.getIsReal() == 0 || orders.getIsReal() == 1){
			orders.setGoodsprice(goodsprice/100);
		}
		orders.setOrderGoodList(orderGoodList);
		//查询订单提成人员信息
		List<OrderPushmoneyRecord> orderPushmoneyRecords = orderPushmoneyRecordService.getOrderPushmoneyRecordByOrderId(orderid);
		orders.setOrderPushmoneyRecords(orderPushmoneyRecords);
		//查询订单日志信息
		List<OrderRemarksLog> orderRemarks = dao.getOrderRemarksLog(orderid);
		orders.setOrderRemarksLog(orderRemarks);
		//查询订单发票信息
		orders.setNum(dao.selectInvoiceRelevancyNum(orderid));
//		OrderInvoice orderInvoice  = dao.getOrderInvoiceRelevancy(orderid);
//		orders.setOrderInvoice(orderInvoice);
		//查询订单下的红包
		List<OrderGoodsCoupon> orderGoodsCoupons = activityCouponUserDao.findlistByOrdeid(orderid);
		orders.setOrderGoodsCoupons(orderGoodsCoupons);
		
		return orders;
	}

	/**
	 * 根据用户id查询账户信息
	 * @param userid
	 * @return
	 */
	public double getAccount(int userid) {
		Orders _orders = new Orders();
		_orders.setUserid(userid);
		Orders account = dao.getAccount(_orders);
		Orders newAccount = new Orders();
		if(account == null){
			newAccount.setAccountBalance(0);
		}else{
			newAccount.setAccountBalance(account.getAccountBalance());
		}
		return newAccount.getAccountBalance();
	}
	
	/**
	 * 新增订单充值日志记录
	 * @param oLog
	 */
	public void addOrderRechargeLog(OrderRechargeLog oLog){
		DecimalFormat formater = new DecimalFormat("#0.##");
		//获取基本值
		User user = UserUtils.getUser(); //登陆用户
		double totalAmount = oLog.getTotalAmount(); //实付款金额
		double accountBalance = oLog.getAccountBalance(); //订单余款 //账户余额
		double singleRealityPrice = oLog.getSingleRealityPrice(); //实际服务单次价
		double singleNormPrice = oLog.getSingleNormPrice(); //单次标价
		double orderArrearage = oLog.getOrderArrearage(); //欠款
		int _servicetimes = oLog.getServicetimes(); //预计服务次数
		
		OrderGoodsDetails newDetails = orderGoodsDetailsService.selectOrderBalance(oLog.getRecid());
		double sumOrderBalance = newDetails.getOrderBalance();//该订单的该商品剩余的可用余额，充值时必须用
		double sumAppTotalAmount = newDetails.getAppTotalAmount();//该订单的已付金额
		double sumAppArrearage = newDetails.getAppArrearage();  //该订单仍欠的款
		double goodsPrice = newDetails.getGoodsPrice();   //商品的优惠价格
		double orderAmount = newDetails.getOrderAmount();//商品应付价格
		int integral = newDetails.getIntegral();        //充值完全以后送的云币
		
		double couponPrice = newDetails.getCouponPrice();      //红包面值
		double memberGoodsPrice = newDetails.getMemberGoodsPrice(); //使用了会员折扣优惠的钱 
		double advancePrice = newDetails.getAdvancePrice();        //预约金
		int goodsNum = newDetails.getGoodsNum();                  //购买的数量
		
		double newTotalAmount = Double.parseDouble(formater.format(totalAmount + sumOrderBalance));//实付款金额 =充值金额+使用的账户余额+必须使用的商品剩余可用余额
		int serviceTimes_in = 0;//剩余服务次数
		double totalAmount_in = 0;//实付款金额（入库）
		double accountBalance_in = 0;//余额
		
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
		double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
		double appTotalAmount = 0; //app实付金额
		double appArrearage = 0;   //app欠款金额
		int userIntegral = 0;   //入库赠送的云币
		
		if(1 == oLog.getIsReal()){ //虚拟
			if(singleRealityPrice <= newTotalAmount && newTotalAmount < orderArrearage){
				// 实际单次标价  < 实付款金额	< 欠款
				serviceTimes_in = (int)Math.floor(newTotalAmount/singleRealityPrice);//充值次数
				totalAmount_in = serviceTimes_in * singleRealityPrice;//实付金额
				accountBalance_in = Double.parseDouble(formater.format(totalAmount - totalAmount_in - accountBalance));
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = Double.parseDouble(formater.format(newTotalAmount - totalAmount_in - sumOrderBalance));//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额
			}else if(newTotalAmount >= orderArrearage){
				//实付款金额	>=  欠款
				serviceTimes_in = _servicetimes-oLog.getRemaintimes();//充值次数
				totalAmount_in = orderArrearage;//实付金额
				accountBalance_in = Double.parseDouble(formater.format(totalAmount - totalAmount_in - accountBalance));
				
				newSpareMoneySum = Double.parseDouble(formater.format(newTotalAmount - orderArrearage - accountBalance));//商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
				newOrderBalance = -sumOrderBalance;//商品余额（只放在details里的OrderBalance）
				
				if("bm".equals(oLog.getChannelFlag())){
					appTotalAmount =  Double.parseDouble(formater.format(orderAmount - sumAppTotalAmount));//app实付金额
				}else{
					if(couponPrice < advancePrice){
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - couponPrice - memberGoodsPrice));//app实付金额
					}else{
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - advancePrice - memberGoodsPrice));//app实付金额
					}
				}
				appArrearage = -sumAppArrearage;//app欠款金额
				
				if(!"bm".equals(oLog.getChannelFlag())){   //app或者wap下单，充值大于欠款时送云币
					//当充值全部欠款以后，对用户进行送云币
					if(integral > 0){
						boolean str = redisClientTemplate.exists(MTMY_ID+oLog.getMtmyUserId());
						if(str){   //若缓存存在，则操作缓存
							RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+oLog.getMtmyUserId());
							redisLock.lock();
							redisClientTemplate.incrBy(MTMY_ID+oLog.getMtmyUserId(),integral);
							redisLock.unlock();
						}else{         //若缓存不存在，则操作mtmy_user_accounts
							userIntegral = integral;            //赠送云币
						}
						
						//在mtmy_integrals_log表中插入日志
						IntegralsLog integralsLog = new IntegralsLog();
						integralsLog.setUserId(oLog.getMtmyUserId());
						integralsLog.setIntegralType(0);
						integralsLog.setIntegralSource(0);
						integralsLog.setActionType(21);
						integralsLog.setIntegral(integral);
						integralsLog.setOrderId(oLog.getOrderId());
						integralsLog.setRemark("商品赠送");
						ordersDao.insertIntegralLog(integralsLog);
					}
				}
				
			}else if(singleRealityPrice > newTotalAmount){//实际单次标价  > 实付款金额
				totalAmount_in = 0;
				/*accountBalance_in = oLog.getRechargeAmount();*/
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = totalAmount;//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额

			}
		}else{//实物
			if(newTotalAmount<orderArrearage){
				//实际付款 < 欠款
				totalAmount_in = totalAmount;
				accountBalance_in = Double.parseDouble(formater.format(totalAmount- totalAmount_in - accountBalance));
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = 0;//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额
			}else if(newTotalAmount >= orderArrearage){
				//实际付款 >= 欠款
				totalAmount_in = orderArrearage;
				accountBalance_in = Double.parseDouble(formater.format(totalAmount- totalAmount_in - accountBalance));
				
				newSpareMoneySum = Double.parseDouble(formater.format(newTotalAmount - orderArrearage - accountBalance));//商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
				newOrderBalance = 0;//商品余额（只放在details里的OrderBalance）
				
				if("bm".equals(oLog.getChannelFlag())){
					appTotalAmount =  Double.parseDouble(formater.format(orderAmount - sumAppTotalAmount));//app实付金额
				}else{
					if(couponPrice < advancePrice){
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice*goodsNum - sumAppTotalAmount - couponPrice - memberGoodsPrice));//app实付金额
					}else{
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice*goodsNum - sumAppTotalAmount - advancePrice - memberGoodsPrice));//app实付金额
					}
				}
				/*appTotalAmount =  Double.parseDouble(formater.format(orderAmount - sumAppTotalAmount));//app实付金额*/				
			
				appArrearage = -sumAppArrearage;//app欠款金额
			}
		}
		
		double itemAmount = serviceTimes_in * singleRealityPrice; //项目金额
		double itemCapitalPool = serviceTimes_in * singleNormPrice; //项目资金池
		
		/*oLog.setAccountBalance(accountBalance);
		oLog.setCreateBy(user);
		//保存充值日志记录
		orderRechargeLogService.saveOrderRechargeLog(oLog);*/
		//保存订单商品详情记录表
		OrderGoodsDetails details = new OrderGoodsDetails();
		details.setOrderId(oLog.getOrderId());
		details.setGoodsMappingId(oLog.getRecid()+"");
		details.setOrderBalance(newOrderBalance);	//订单余款
		details.setTotalAmount(totalAmount_in);	//实付款金额
		details.setOrderArrearage(-totalAmount_in);	//订单欠款
		details.setItemAmount(itemAmount);	//项目金额
		details.setItemCapitalPool(itemCapitalPool); //项目资金池
		details.setServiceTimes(serviceTimes_in);	//剩余服务次数
		details.setAppTotalAmount(appTotalAmount);//app实付金额
		details.setAppArrearage(appArrearage);//app欠款金额
		details.setType(0);
		details.setAdvanceFlag("3");
		details.setCreateOfficeId(user.getOffice().getId());
		details.setCreateBy(user);
		details.setBelongOfficeId(oLog.getBelongOfficeId());
		details.setUseBalance(accountBalance);
		//保存订单商品详情记录
		orderGoodsDetailsService.saveOrderGoodsDetails(details);
		
		//同步数据到营业额明细表
		TurnOverDetails turnOverDetails = new TurnOverDetails();
		turnOverDetails.setOrderId(details.getOrderId());
		turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
		turnOverDetails.setDetailsId(details.getId());
		turnOverDetails.setType(2);
		turnOverDetails.setAmount(details.getAppTotalAmount());
		turnOverDetails.setUseBalance(details.getUseBalance());
		turnOverDetails.setStatus(3);
		turnOverDetails.setUserId(oLog.getMtmyUserId());
		turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
		turnOverDetails.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
		
		//根据用户id查询用户账户信息
		Orders _orders = new Orders();
		/*double accountArrearage = 0;*/
		_orders.setUserid(oLog.getMtmyUserId());
		Orders account = ordersDao.getAccount(_orders);
		/*if(1 == oLog.getIsReal() && singleRealityPrice > totalAmount){
			accountArrearage = account.getAccountArrearage()-0;	//账户欠款信息
		}else{
			accountArrearage = account.getAccountArrearage()-totalAmount_in;	//账户欠款信息
		}
		account.setAccountArrearage(accountArrearage);*/
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance_ = newSpareMoneySum;   //账户余额信息
			newAccount.setAccountBalance(accountBalance_);
			newAccount.setUserid(_orders.getUserid());
			newAccount.setUserIntegral(userIntegral);    //要赠送的云币
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}else{
			double accountBalance_ = 0;
			accountBalance_ = Double.parseDouble(formater.format(account.getAccountBalance()+newSpareMoneySum));
			account.setAccountBalance(accountBalance_);
			account.setUserIntegral(account.getUserIntegral() + userIntegral);   //要赠送的云币
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}
		
	}
	/**
	 * 订单ID（26位）=临时订单标识（1）+退货标识（1）+yyyyMMDDHHMMSSsss(17)+用户ID（7）
	 * @param user_id 用户id
	 * @param temp 0:正式订单号，1：临时订单号
	 * @param flag 0:正常订单，1：退货，2：换货，3：仅退款
	 * @return
	 */
	public static String createOrder(Integer user_id,int temp,int flag){
		StringBuffer sb = new StringBuffer();
		sb.append(temp);
		sb.append(flag);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sb.append(sdf.format(new Date()));
		sb.append(user_id);
		return sb.toString();
	}

	/**
	 * 修改订单
	 * @param orders
	 */
	public void updateVirtualOrder(Orders orders) {
		//当订单发票的过期时间为空时
		if(orders.getInvoiceOvertime() == null){
			if(orders.getIsReal() == 0){   //app下单实物的状态改为待发货时，将发票过期时间设置为下个月月底
				if(orders.getOrderstatus() == 1){
					orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
				}
			}else if(orders.getIsReal() == 1){  //app下单虚拟的状态改为已完成时，将发票过期时间设置为下个月月底
				if(orders.getOrderstatus() == 4){
					orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
				}
			}
		}
		User user = UserUtils.getUser(); //登陆用户
		orders.setCreateBy(user);
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		orders.setPayid(payment.getPayid());
		orders.setPaycode(payment.getPaycode());
		orders.setPayname(payment.getPayname());
		Orders _orders = ordersDao.selectOrderById(orders.getOrderid());
		insertLog(_orders);
		ordersDao.updateVirtualOrder(orders);
		insertLog(orders);
	}

	/**
	 * 查看商品订单充值记录
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	public List<OrderGoodsDetails> getMappinfOrderView(int recid) {
		return orderGoodsDetailsService.getMappinfOrderView(recid);
	}
	
	/**
	 * 查看主订单充值记录
	 * @param orderid
	 * @return
	 */
	public List<OrderRechargeLog> getOrderRechargeView(String orderid) {
		return orderRechargeLogService.getOrderRechargeView(orderid);
	}
	
	/**
	 * 保存实物订单
	 * @param orders
	 */
	public void saveKindOrder(Orders orders) {
		DecimalFormat formater = new DecimalFormat("#0.##");
		User user = UserUtils.getUser(); //登陆用户
		int mtmyUserId = orders.getUserid();	//每天每耶用户id
		String orderid = createOrder(mtmyUserId, 0, 0);//订单id
		orders.setOrderid(orderid);
		orders.setCreateBy(user);
		//页面传递到后台的数据
		List<Integer> goodselectIds = orders.getGoodselectIds();	//商品id集合
		List<String> speckeys = orders.getSpeckeys();				//规格key集合
		List<Double> orderAmounts = orders.getOrderAmounts();		//成交价集合
		List<Double> actualPayments = orders.getActualPayments();	//实际付款集合
		List<Integer> kindgoodsnum = orders.getKindgoodsnum();		//实物商品购买数量集合
		double orderAmountSum = 0d;  //应付总额
		double afterPaymentSum = 0d;  //实际付款总额
		double debtMoneySum = 0d;	//总欠款
		double spareMoneySum = 0d;	//总余额
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于应付时，将多的存入个人账户余额中)
		double goodsprice = 0;  //商品总价
		
		String recid = "";//保存mappingid到账户充值记录表中
		
		for (Integer i = 0; i < goodselectIds.size(); i++) {
			Integer goodselectId = goodselectIds.get(i);		//商品id
			String speckey = speckeys.get(i);					//规格key
			//通过商品id获取当前商品
			Goods goods = goodsDao.getgoods(goodselectId.toString());
			//通过规格key和商品id查询商品对应规格
			GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
			goodsSpecPrice.setGoodsId(goodselectId.toString());
			goodsSpecPrice.setSpecKey(speckey);
			GoodsSpecPrice goodspec = goodsSpecPriceDao.getSpecPrce(goodsSpecPrice);//查询当前商品对应的规格
			Double orderAmount = orderAmounts.get(i);			//应付金额
			orderAmountSum = orderAmountSum + orderAmount;		//应付总额
			Double actualPayment = actualPayments.get(i);		//实际付款
			afterPaymentSum = afterPaymentSum + actualPayment;	//实际付款总额
			Integer goodsnum = kindgoodsnum.get(i);				//购买数量
			double price = goodspec.getPrice();	//优惠价格
			double marketPrice = goodspec.getMarketPrice();	//市场单价
			double actualPayment_on = 0;		//实付款（入库）
			double orderArrearage_on = 0;		//欠款（入库）
			double orderBalance_on = 0;			//余额（入库）
			double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
			double appTotalAmount = 0; //app实付金额
			double appArrearage = 0;   //app欠款金额
			goodsprice += price;
			if(orderAmount > actualPayment){
				//应付 > 实付
				actualPayment_on = actualPayment;
				orderArrearage_on = Double.parseDouble(formater.format(orderAmount - actualPayment_on));
				orderBalance_on = 0;
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = Double.parseDouble(formater.format(orderAmount - actualPayment)); //app欠款金额
			}else if(orderAmount.equals(actualPayment)){
				//应付 = 实付
				actualPayment_on = actualPayment;
				orderArrearage_on = 0;
				orderBalance_on = 0;
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
			}else if(orderAmount < actualPayment){
				//应付 < 实付
				actualPayment_on = orderAmount;
				orderArrearage_on = 0;
				orderBalance_on = Double.parseDouble(formater.format(actualPayment - actualPayment_on));
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = orderAmount;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
				newSpareMoneySum = newSpareMoneySum + Double.parseDouble(formater.format(actualPayment - orderAmount));//商品总余额
			}
			spareMoneySum = spareMoneySum + orderBalance_on;	//总余额
			debtMoneySum = debtMoneySum + orderArrearage_on;	//总欠款
			//添加商品订单"mtmy_order_goods_mapping"
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderid(orderid);
			orderGoods.setUserid(orders.getUserid());
			orderGoods.setGoodsid(goods.getGoodsId());
			orderGoods.setGoodsname(goods.getGoodsName());
			orderGoods.setGoodssn(goods.getGoodsSn());
			orderGoods.setOriginalimg(goods.getOriginalImg());
			orderGoods.setSpeckey(goodspec.getSpecKey());
			orderGoods.setSpeckeyname(goodspec.getSpecKeyValue());
			orderGoods.setCostprice(goodspec.getCostPrice());		//成本单价
			orderGoods.setMarketprice(marketPrice);		//市场单价
			orderGoods.setGoodsprice(price);	//优惠价
			orderGoods.setGoodsnum(goodsnum);	//购买数量
			orderGoods.setOrderAmount(orderAmount);		//应付金额
			orderGoods.setTotalAmount(actualPayment_on);	//计算后实付款金额
			orderGoods.setOrderBalance(orderBalance_on);	//订单余款
			orderGoods.setOrderArrearage(orderArrearage_on);	//订单欠款
			orderGoods.setRealityAddTime(new Date());   //实际下单时间
			orderGoods.setExpiringDate(goodspec.getExpiringDate());
			orderGoods.setIsreal(0);	// 是否为虚拟 0 实物 1虚拟
			//保存 mtmy_order_goods_mapping
			orderGoodsDao.saveOrderGoods(orderGoods);
			
			recid += "," + orderGoods.getRecid();//保存mappingid到账户充值记录表中
			
			//订单商品详情记录表
			OrderGoodsDetails details = new OrderGoodsDetails();
			details.setOrderId(orderid);
			details.setGoodsMappingId(orderGoods.getRecid()+"");
			details.setTotalAmount(actualPayment_on);	//计算后实付款金额
			details.setOrderBalance(newOrderBalance);	//订单余款
			details.setOrderArrearage(orderArrearage_on);	//订单欠款
			details.setAppTotalAmount(appTotalAmount);  //app实付金额
			details.setAppArrearage(appArrearage);   //app欠款金额
			details.setType(0);
			details.setAdvanceFlag("0");
			details.setCreateOfficeId(user.getOffice().getId());
			details.setCreateBy(user);
			details.setBelongOfficeId(orders.getBelongOfficeId());
			//保存订单商品详情记录
			orderGoodsDetailsService.saveOrderGoodsDetails(details);
			
			if(orders.getIsNeworder() == 0){
				//同步数据到营业额明细表
				TurnOverDetails turnOverDetails = new TurnOverDetails();
				turnOverDetails.setOrderId(orderid);
				turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
				turnOverDetails.setDetailsId(details.getId());
				turnOverDetails.setType(1);
				turnOverDetails.setAmount(details.getAppTotalAmount());
				turnOverDetails.setUseBalance(details.getUseBalance());
				turnOverDetails.setStatus(0);
				turnOverDetails.setUserId(mtmyUserId);
				turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
				turnOverDetails.setCreateBy(UserUtils.getUser());
				turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
			}
		}
		
		/*//订单充值日志表
		OrderRechargeLog  oLog = new OrderRechargeLog();
		oLog.setOrderId(orderid);
		oLog.setMtmyUserId(mtmyUserId);		//每天每耶用户id
		oLog.setRechargeAmount(afterPaymentSum);	//充值金额
		oLog.setAccountBalance(spareMoneySum);	//账户余额
		oLog.setTotalAmount(afterPaymentSum+spareMoneySum);	//实付款金额
		oLog.setCreateBy(user);
		oLog.setCreateDate(new Date());
		orderRechargeLogService.saveOrderRechargeLog(oLog);*/
		
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		//主订单信息
		Orders _orders = new Orders();
		_orders.setOrderid(orderid);
		_orders.setParentid("0");
		_orders.setUserid(mtmyUserId);
		_orders.setOrderstatus(orders.getOrderstatus());
		_orders.setPayid(payment.getPayid());
		_orders.setPaycode(payment.getPaycode());
		_orders.setPayname(payment.getPayname());
		_orders.setGoodsprice(goodsprice);	//商品总价
		_orders.setOrderamount(orderAmountSum); //应付总金额
		_orders.setTotalamount(afterPaymentSum); //实付总额
		_orders.setOrderArrearage(debtMoneySum); //总欠款
		_orders.setOrderBalance(spareMoneySum); //总余额
		_orders.setIsReal(0);	//是否实物（0：实物商品；1：虚拟商品）
		_orders.setIsNeworder(orders.getIsNeworder()); //是否新订单（0：新订单；1：老订单）
		_orders.setDistinction(orders.getDistinction()); //订单性质
		_orders.setOffice(user.getOffice());
		_orders.setDelFlag("0");
		_orders.setChannelFlag("bm");
		_orders.setShippingtype(orders.getShippingtype());
		_orders.setShopId(orders.getShopId());
		_orders.setConsignee(orders.getConsignee());
		_orders.setPhone(orders.getPhone());
		_orders.setAddress(orders.getAddress());
		_orders.setUsernote(orders.getUsernote());
		_orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
		_orders.setCreateBy(user);
		_orders.setBelongOfficeId(orders.getBelongOfficeId());
		ordersDao.saveKindOrder(_orders);
		
		//根据用户id查询用户账户信息
		Orders account = ordersDao.getAccount(_orders);
		/*double accountArrearage = account.getAccountArrearage()+debtMoneySum;	//账户欠款信息*/		
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance);
			newAccount.setUserid(_orders.getUserid());
			ordersDao.insertAccount(newAccount);
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}else{
			double accountBalance = account.getAccountBalance()+newSpareMoneySum;	//账户余额信息
			/*account.setAccountArrearage(accountArrearage);*/
			account.setAccountBalance(accountBalance);
			ordersDao.updateAccount(account);
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}
		
		//保存提成人员信息 
		List<String> sysUserId = orders.getSysUserId();	//提成人员id
		List<Double> pushMoney = orders.getPushMoney();	//提成金额
		if(sysUserId!=null && sysUserId.size()>0){
			OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
			for (int i = 0; i < sysUserId.size(); i++) {
				orderPushmoneyRecord.setPushmoneyUserId(sysUserId.get(i));

				//通过业务员id(属于妃子校的)查询业务员归属机构
				orderPushmoneyRecord = orderPushmoneyRecordService.getOfficeIdByUserId(orderPushmoneyRecord);
				
				orderPushmoneyRecord.setOrderId(orderid);
				orderPushmoneyRecord.setPushMoney(pushMoney.get(i));
				orderPushmoneyRecord.setOfficeId(user.getOffice().getId());
				orderPushmoneyRecord.setCreateBy(user);
				orderPushmoneyRecord.setDelFlag("0");
				orderPushmoneyRecordService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
			}
		}
		//批量添加订单备注
		if (orders.getOrderRemarks()!=null && orders.getOrderRemarks().size()>0) {
			ordersDao.saveOrderRemarksLog(orders);
		}
		if(orders.getIchecks() == 1){
			String personheadContent = orders.getPersonheadContent();
			String companyheadContent = orders.getCompanyheadContent();//公司发票抬头
			if(!"".equals(companyheadContent)){//等于空 就是个人  不等于空就是 公司
				orders.setHeadContent(companyheadContent);
			}else{
				orders.setHeadContent(personheadContent);
			}
			//保存发票信息
			orderInvoiceService.saveOrderInvoice(orders);
			OrderInvoiceRelevancy orderInvoiceRelevancy = new OrderInvoiceRelevancy();
			orderInvoiceRelevancy.setOrderId(orderid);
			orderInvoiceRelevancy.setInvoiceId(orders.getInvoiceId());
			ordersDao.saveOrderInvoiceRelevancy(orderInvoiceRelevancy);
		}
	}

	/**
	 * 查询订单
	 * @param orderId
	 * @return
	 */
	public List<Orders> selectByOrderId(String userId){
		return ordersDao.selectByOrderId(userId);
	}
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public Orders selectBydoubl(String orderId){
		return ordersDao.selectBydoubl(orderId);
	}
	

	/**
     * 获取任意时间下个月的最后一天
     * 描述:<描述函数实现的功能>.
     * @param repeatDate
     * @return
     */
	private static Date getMaxMonthDate(Date date) {
        //SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);  //dft.parse(repeatDate)
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

	/**
	 * 删除提成人员信息
	 * @param orderPushmoneyRecord
	 */
	public void deleteSysUserInfo(OrderPushmoneyRecord orderPushmoneyRecord) {
		orderPushmoneyRecord.setUpdateBy(UserUtils.getUser());
		orderPushmoneyRecordService.deleteSysUserInfo(orderPushmoneyRecord);
	}
	/**
	 * 删除订单备注
	 * 
	 * @param id
	 * @return
	 */
	public void deleteOrderRemarksLog(Integer orderRemarksId) {
		dao.deleteOrderRemarksLog(orderRemarksId);
	}

	/**
	 * 添加单条订单备注信息
	 * @param orderid
	 */
	public void saveOrderRemarksLog(Orders orders) {
		User user = UserUtils.getUser();
		orders.setCreateBy(user);
		ordersDao.saveOrderRemarks(orders);
	}

	/**
	 * 保存单条提成人员信息
	 * @param orderPushmoneyRecord
	 */
	public void saveOrderPushmoneyRecord(OrderPushmoneyRecord orderPushmoneyRecord) {
		if("edit".equals(orderPushmoneyRecord.getFlag())){
			OrderPushmoneyRecord oldOrderPushmoneyRecord = orderPushmoneyRecordService.getOrderPushmoneyRecordById(orderPushmoneyRecord.getPushmoneyRecordId());
			//保存订单提成人员修改日志
			String str = specialLog(oldOrderPushmoneyRecord,orderPushmoneyRecord);
			if(!"".equals(str) && null != str){
				PushmoneyRecordLog pushmoneyRecordLog = new PushmoneyRecordLog();
				pushmoneyRecordLog.setOrderId(orderPushmoneyRecord.getOrderId());
				pushmoneyRecordLog.setPushmoneyRecordId(orderPushmoneyRecord.getPushmoneyRecordId());
				pushmoneyRecordLog.setContent(str);
				pushmoneyRecordLog.setCreateBy(UserUtils.getUser());
				orderPushmoneyRecordService.insertPushMoneyLog(pushmoneyRecordLog);
			}
			
			User user = UserUtils.getUser();
			orderPushmoneyRecord.setUpdateBy(user);
			orderPushmoneyRecordService.updatePushMoney(orderPushmoneyRecord);
		}else if("add".equals(orderPushmoneyRecord.getFlag())){
			//通过业务员id(属于妃子校的)查询业务员归属机构
			OrderPushmoneyRecord opr = orderPushmoneyRecordService.getOfficeIdByUserId(orderPushmoneyRecord);
			orderPushmoneyRecord.setUserOfficeId(opr.getUserOfficeId());
			orderPushmoneyRecord.setUserOfficeIds(opr.getUserOfficeIds());
			
			User user = UserUtils.getUser();
			orderPushmoneyRecord.setOfficeId(user.getOffice().getId());
			orderPushmoneyRecord.setCreateBy(user);
			orderPushmoneyRecord.setDelFlag("0");
			orderPushmoneyRecordService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
		}
	}
	
	/**
	 * 订单提成人员修改日志
	 * @param oldOrderPushmoneyRecord
	 * @param orderPushmoneyRecord
	 * @return
	 */
	@Transactional(readOnly = false)
	public String specialLog(OrderPushmoneyRecord oldOrderPushmoneyRecord,OrderPushmoneyRecord orderPushmoneyRecord){
		StringBuffer str = new StringBuffer();	// 用于存储一些特殊的日志
		if(oldOrderPushmoneyRecord.getPushMoney() != orderPushmoneyRecord.getPushMoney()){
			str.append("业务员姓名："+oldOrderPushmoneyRecord.getPushmoneyUserName()+",提成金额:修改前("+oldOrderPushmoneyRecord.getPushMoney()+"),修改后("+orderPushmoneyRecord.getPushMoney()+")--");
		}
		return str.toString();
	}

	/**
	 * 计算欠款
	 * @param recId
	 * @return
	 */
	public GoodsDetailSum selectDetaiSum(String recId){
		return orderGoodsDetailsDao.selectDetaiSum(recId);
	}

	
	/**
	 * 根据退货期时间，订单为（1：待发货；2：待收货；）的订单修改状态为（4：已完成）
	 * @param map
	 * @return
	 */
	public int updateOrderFinished(Map<String, Object> map) throws Exception{
		return ordersDao.updateOrderFinished(map);
	}

	/**
	 * 取消订单
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
	public void cancellationOrder(Orders orders) {
		dao.cancellationOrder(orders);
	}
	
	/**
	 * 实物带预约金的商品确认收货更改物流状态
	 */
	public void updateOrderstatusForReal(String orderid){
		dao.updateOrderstatusForReal(orderid);
	}
	
	/**
	 * 计算订单是否欠款
	 * @param orderid
	 * @return
	 */
	public Orders selectByOrderIdSum(String orderid){
		return dao.selectByOrderIdSum(orderid);
	}
	
	/**
	 * @param 
	 * @return Page<Orders>
	 * 根据用户ID搜索订单
	 */
	public Page<CrmOrders> findByUser(Page<CrmOrders> page, CrmOrders orders){
		orders.setPage(page);
		// 执行分页查询
		page.setList(ordersDao.findByUser(orders));
		return page;
	}
		
	 /**
	  * 根据recid查找相应的商品信息
	  * @param recId
	  * @return
	  */
	public OrderGoods selectOrderGoodsByRecid(int recId){
		return orderGoodsDao.selectOrderGoodsByRecid(recId);
	}
	
	/**
	 * 查询卡项被预约的子项的信息 
	 * @param recId
	 * @return
	 */
	public OrderGoods selectCardSonReservation(int recId){
		return orderGoodsDao.selectCardSonReservation(recId);
	}
	
	/**
	 * app虚拟订单处理预约金
	 * @param oLog
	 * @param orderAmount应付款金额
	 * @param goodsPrice商品优惠单价
	 */
	public void handleAdvanceFlag(OrderRechargeLog oLog,double goodsPrice,double detailsTotalAmount,int goodsType,String officeId,double realAdvancePrice,OrderGoodsDetails oldDetails){
		//获取基本值
		User user = UserUtils.getUser(); //登陆用户
		double totalAmount = oLog.getTotalAmount(); //实付款金额
		double accountBalance = oLog.getAccountBalance(); //订单余款 //账户余额（不是账户的余额，而是用了多少账户的余额）
		double singleRealityPrice = oLog.getSingleRealityPrice(); //实际服务单次价
		double singleNormPrice = oLog.getSingleNormPrice(); //单次标价
		double advance = oLog.getAdvance();
		
		int serviceTimes_in = 0;//实际的充值次数
		int serviceTimes_in_a = 0; //充值次数
		double totalAmount_in_a = 0;//实付款金额
		double totalAmount_in = 0;//实付款金额（入库）
		double accountBalance_in = 0;//余额
		double newSpareMoneySum = 0d;  //总余额(实际单次标价  < 商品优惠单价 < 预付金时，将多的存入个人账户余额中)
		
		double appTotalAmount = 0; //app实付金额
		double appArrearage = 0;   //app欠款金额
		
		double itemAmount; //项目金额
		double itemCapitalPool; //项目资金池
		
		DecimalFormat formater = new DecimalFormat("#0.##");
		if(oLog.getServicetimes() == 999){                          //时限卡单独处理预约金
			serviceTimes_in = oLog.getServicetimes() - 1;       //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
			totalAmount_in_a = oLog.getOrderArrearage();       //实付金额
			accountBalance_in = 0;                                      //订单余款
			totalAmount_in = oLog.getOrderArrearage();          //存入库的实付款金额=欠款
			newSpareMoneySum = -accountBalance;
			appTotalAmount = oLog.getOrderArrearage();  //app实付金额
			appArrearage = -oLog.getOrderArrearage(); //app欠款金额
			
			itemAmount = 0; //项目金额
			itemCapitalPool = 0; //项目资金池
			
		}else{
			if(singleRealityPrice < advance && advance > goodsPrice){
				// 实际单次标价  < 商品优惠单价 < 预付金
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = serviceTimes_in_a * singleRealityPrice;//实付金额
				accountBalance_in = 0;                                      //订单余款
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));        //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = Double.parseDouble(formater.format(advance - goodsPrice));    //总余额(实际单次标价  < 商品优惠单价 < 预付金时，将多的存入个人账户余额中)
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(advance == goodsPrice){     //预约金=商品优惠单价
				serviceTimes_in = oLog.getServicetimes() - 1;                               //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = advance;//实付金额
				accountBalance_in =  0;     //订单余款
				totalAmount_in = 0;        //存入库的实付款金额
				newSpareMoneySum = 0;
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(singleRealityPrice < advance && advance < goodsPrice){
				// 实际单次标价  < 预付金	< 商品优惠单价
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = serviceTimes_in_a * singleRealityPrice;//实付金额
				accountBalance_in =  Double.parseDouble(formater.format(totalAmount - totalAmount_in_a));     //订单余款
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));        //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = -accountBalance;
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(singleRealityPrice >= advance){
				//实际单次标价  >= 预付金
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次,此时的其实也就是0
				totalAmount_in_a = singleRealityPrice;//实付金额
				accountBalance_in = 0;                                 //订单余款，
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));       //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = -accountBalance;
				appTotalAmount = Double.parseDouble(formater.format(singleRealityPrice - advance));  //app实付金额
				appArrearage = -Double.parseDouble(formater.format(singleRealityPrice - advance)); //app欠款金额
			}
			
			itemAmount = serviceTimes_in_a * singleRealityPrice; //项目金额
			itemCapitalPool = serviceTimes_in_a * singleNormPrice; //项目资金池
		}
		
		
		/*if(accountBalance > 0){ //若使用了账户余额，则日志中的账户余额为使用的账户相应的金额
			oLog.setAccountBalance(accountBalance);
			oLog.setRechargeAmount(advance);
			oLog.setTotalAmount(Double.parseDouble(formater.format(accountBalance+advance)));
		}else{
			oLog.setAccountBalance(0);
			oLog.setRechargeAmount(advance);
			oLog.setTotalAmount(advance);
		}
		
		oLog.setCreateBy(user);
		oLog.setRemarks("处理预约金");
		//保存充值日志记录
		orderRechargeLogService.saveOrderRechargeLog(oLog);*/
		//保存订单商品详情记录表
		OrderGoodsDetails details = new OrderGoodsDetails();
		details.setOrderId(oLog.getOrderId());
		details.setGoodsMappingId(oLog.getRecid()+"");
		details.setTotalAmount(totalAmount_in);	//实付款金额
		details.setOrderBalance(accountBalance_in);	//订单余款
		if(oLog.getServicetimes() == 999){              //时限卡单独处理
			details.setOrderArrearage(-totalAmount_in);    	//订单欠款
		}else{
			details.setOrderArrearage(Double.parseDouble(formater.format(advance - totalAmount_in_a)));	//订单欠款
		}
		details.setItemAmount(itemAmount);	//项目金额
		details.setItemCapitalPool(itemCapitalPool); //项目资金池
		details.setServiceTimes(serviceTimes_in);	//剩余服务次数
		details.setAppTotalAmount(appTotalAmount);   //app实付金额
		details.setAppArrearage(appArrearage);        //app欠款金额
		details.setType(0);
		details.setAdvanceFlag("2");
		details.setCreateOfficeId(user.getOffice().getId());
		details.setCreateBy(user);
		details.setUseBalance(accountBalance);
		//保存订单商品详情记录
		orderGoodsDetailsService.saveOrderGoodsDetails(details);
		
		//同步数据到营业额明细表
		//第一次，同步下单的那条数据
		TurnOverDetails turnOverDetails1 = new TurnOverDetails();
		turnOverDetails1.setOrderId(details.getOrderId());
		turnOverDetails1.setMappingId(Integer.valueOf(oldDetails.getGoodsMappingId()));
		turnOverDetails1.setDetailsId(oldDetails.getId());
		turnOverDetails1.setType(1);
		turnOverDetails1.setAmount(oldDetails.getAppTotalAmount());
		turnOverDetails1.setUseBalance(oldDetails.getUseBalance());
		turnOverDetails1.setStatus(1);
		turnOverDetails1.setUserId(oLog.getMtmyUserId());
		turnOverDetails1.setBelongOfficeId(officeId);
		turnOverDetails1.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails1);
		
		//第一次，同步处理预约金的那条数据
		TurnOverDetails turnOverDetails2 = new TurnOverDetails();
		turnOverDetails2.setOrderId(details.getOrderId());
		turnOverDetails2.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
		turnOverDetails2.setDetailsId(details.getId());
		turnOverDetails2.setType(2);
		turnOverDetails2.setAmount(details.getAppTotalAmount());
		turnOverDetails2.setUseBalance(details.getUseBalance());
		turnOverDetails2.setStatus(2);
		turnOverDetails2.setUserId(oLog.getMtmyUserId());
		turnOverDetails2.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails2);
		
		OrderGoodsDetails newDetails = orderGoodsDetailsService.selectOrderBalance(oLog.getRecid());
		int integral = newDetails.getIntegral();        //处理完预约金以后，待付尾款为0的时候，处理预约金以后送的云币
		int userIntegral = 0;   //入库赠送的云币
		
		if(!"bm".equals(oLog.getChannelFlag()) && newDetails.getSumOrderArrearage() == 0){   //app或者wap处理预约，待付尾款为0的时候，送云币
			//当充值全部欠款以后，对用户进行送云币
			if(integral > 0){
				boolean str = redisClientTemplate.exists(MTMY_ID+oLog.getMtmyUserId());
				if(str){   //若缓存存在，则操作缓存
					RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+oLog.getMtmyUserId());
					redisLock.lock();
					redisClientTemplate.incrBy(MTMY_ID+oLog.getMtmyUserId(),integral);
					redisLock.unlock();
				}else{         //若缓存不存在，则操作mtmy_user_accounts
					userIntegral = integral;            //赠送云币
				}
				
				//在mtmy_integrals_log表中插入日志
				IntegralsLog integralsLog = new IntegralsLog();
				integralsLog.setUserId(oLog.getMtmyUserId());
				integralsLog.setIntegralType(0);
				integralsLog.setIntegralSource(0);
				integralsLog.setActionType(21);
				integralsLog.setIntegral(integral);
				integralsLog.setOrderId(oLog.getOrderId());
				integralsLog.setRemark("商品赠送");
				ordersDao.insertIntegralLog(integralsLog);
			}
		}
		
		//对用户的账户进行操作
		Orders _orders = new Orders();//根据用户id查询用户账户信息
		_orders.setUserid(oLog.getMtmyUserId());
		Orders account = ordersDao.getAccount(_orders);
		/*double accountArrearage = account.getAccountArrearage()+Double.parseDouble(formater.format((goodsPrice - totalAmount_in_a)));	//账户欠款信息
		account.setAccountArrearage(accountArrearage);*/
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance_ = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance_);
			newAccount.setUserid(_orders.getUserid());
			newAccount.setUserIntegral(userIntegral);                 //要赠送的云币
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}else{
			double accountBalance_ = 0;
			accountBalance_ = Double.parseDouble(formater.format(account.getAccountBalance()+newSpareMoneySum));
			account.setAccountBalance(accountBalance_);
			account.setUserIntegral(account.getUserIntegral() + userIntegral);   //要赠送的云币
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}
		
		//若为老商品，则对店铺有补偿
		if(goodsType == 0){
			//若有预约金
			if(realAdvancePrice > 0){
				//对登云账户进行操作
				if(detailsTotalAmount > 0){
					double claimMoney = 0.0;   //补偿金  补助不超过20
					if(detailsTotalAmount * 0.2 >= 20){
						claimMoney = detailsTotalAmount + 20;
					}else{
						claimMoney = detailsTotalAmount * 1.2;
					}
					OfficeAccountLog officeAccountLog = new OfficeAccountLog();
					User newUser = UserUtils.getUser();
					
					double amount = orderGoodsDetailsService.selectByOfficeId("1");   //登云美业公司账户的钱
					double afterAmount = Double.parseDouble(formater.format(amount - claimMoney));
					orderGoodsDetailsService.updateByOfficeId(afterAmount, "1");   //更新登云美业的登云账户金额
					
					//登云美业的登云账户减少钱时对日志进行操作
					officeAccountLog.setOrderId(oLog.getOrderId());
					officeAccountLog.setOfficeId("1");
					officeAccountLog.setType("1");
					officeAccountLog.setOfficeFrom("1");
					officeAccountLog.setAmount(claimMoney);
					officeAccountLog.setCreateBy(newUser);
					orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
					
					String shopId = "";
					if("".equals(officeId) || officeId == null){
						shopId = orderGoodsDetailsService.selectShopId(String.valueOf(oLog.getRecid())); //获取当前用户预约对应的店铺id
					}else{
						shopId = officeId;          //获取当前用户的归属店铺（机构）
					}
					if(orderGoodsDetailsService.selectShopByOfficeId(shopId) == 0){    //若登云账户中无该店铺的账户
						OfficeAccount officeAccount = new OfficeAccount();
						officeAccount.setAmount(claimMoney);
						officeAccount.setOfficeId(shopId);
						orderGoodsDetailsService.insertByOfficeId(officeAccount);
					}else{         
						double shopAmount = orderGoodsDetailsService.selectByOfficeId(shopId);   //登云账户中店铺的钱
						double afterShopAmount =  Double.parseDouble(formater.format(shopAmount + claimMoney));
						orderGoodsDetailsService.updateByOfficeId(afterShopAmount, shopId);
					}
					//店铺的登云账户减少钱时对日志进行操作
					officeAccountLog.setOrderId(oLog.getOrderId());
					officeAccountLog.setOfficeId(shopId);
					officeAccountLog.setType("0");
					officeAccountLog.setOfficeFrom("1");
					officeAccountLog.setAmount(claimMoney);
					officeAccountLog.setCreateBy(newUser);
					orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
				}
			}
		}
	}
	
	/**
	 *根据订单号查询其订单状态以及是否为虚拟订单 
	 */
	public Orders selectOrdersStatus(String orderId){
		return ordersDao.selectOrdersStatus(orderId);
	}
	
	/**
	 * 查看订单号是否存在
	 */
	public int selectOrdersId(String orderId){
		return ordersDao.selectOrdersId(orderId);
	}
	
	/**
	 * 根据订单里的user_id找到对应的cilent_id
	 * @param orders
	 * @return
	 */
	public List<String> selectCidByUserId(Orders orders){
		return ordersDao.selectCidByUserId(orders);
	}
	
	/**
	 * 根据订单id查找对应的信息，供订单发货后推送给用户 
	 * @param orders
	 * @return
	 */
	public List<OrderGoods> selectOrdersToUser(Orders orders){
		return ordersDao.selectOrdersToUser(orders);
	}
	
	/**
	 * 查找卡项的子项
	 * @param cardId
	 * @return
	 */
	public List<Goods> selectCardSon(int cardId){
		return ordersDao.selectCardSon(cardId);
	}
	
	/**
	 * 保存通用卡订单saveCommonCardlOrder
	 * @param orders
	 */
	public void saveCommonCardlOrder(Orders orders) {
		DecimalFormat formater = new DecimalFormat("#0.##");
		User user = UserUtils.getUser(); //登陆用户
		int mtmyUserId = orders.getUserid();	//每天每耶用户id
		String orderid = createOrder(mtmyUserId, 0, 0);//订单id
		orders.setOrderid(orderid);
		orders.setCreateBy(user);
		
		//页面传递到后台的数据
		List<Integer> goodselectIds = orders.getGoodselectIds();	//商品id集合
		List<String> speckeys = orders.getSpeckeys();				//规格key集合
		List<Double> orderAmounts = orders.getOrderAmounts();		//成交价集合
		List<Double> actualPayments = orders.getActualPayments();	//实际付款集合
		List<Integer> remaintimeNums = orders.getRemaintimeNums();	//虚拟订单老产品-实际次数
		List<Date> realityAddTimeList = orders.getRealityAddTimeList();  //实际下单时间集合
		
		double orderAmountSum = 0d;  //应付总额
		double afterPaymentSum = 0d;  //实际付款总额
		double debtMoneySum = 0d;	//总欠款
		double spareMoneySum = 0d;	//总余额
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于应付时，将多的存入个人账户余额中)
		double goodsprice = 0;  //商品总价
		
		String recid = "";//保存mappingid到账户充值记录表中
		
		for (Integer i = 0; i < goodselectIds.size(); i++) {
			Integer goodselectId = goodselectIds.get(i);		//商品id
			String speckey = speckeys.get(i);					//规格key
			Double orderAmount = orderAmounts.get(i);			//应付金额
			orderAmountSum = orderAmountSum + orderAmount;		//应付总额
			Double actualPayment = actualPayments.get(i);		//实际付款(前)
			
			//通过商品id获取当前商品
			Goods goods = goodsDao.getgoods(goodselectId.toString());
			//通过规格key和商品id查询商品对应规格
			GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
			goodsSpecPrice.setGoodsId(goodselectId.toString());
			goodsSpecPrice.setSpecKey(speckey);
			GoodsSpecPrice goodspec = goodsSpecPriceDao.getSpecPrce(goodsSpecPrice);//查询当前商品对应的规格
			int serviceTimes = goodspec.getServiceTimes();	//规格服务次数
			double price = goodspec.getPrice();	//优惠价格
			double marketPrice = goodspec.getMarketPrice();	//市场单价
			double costprice = goodspec.getCostPrice(); //系统价
			goodsprice += price;
			
			double _afterPayment;//实际付款（后）
			double _spareMoney;	//订单余款
			double _debtMoney;	//订单欠款
			double _singleRealityPrice;		//实际服务单次价
			double _singleNormPrice;	//单次标价
			int _actualNum;	//剩余服务次数
			
			//获取计算后的一些费用
			Orders computingCost = computingCost(orderAmount, actualPayment, serviceTimes, price);	//计算获取所有价钱
			_afterPayment = computingCost.getAfterPayment();//实际付款（后）
			_spareMoney = computingCost.getSpareMoney();	//订单余款
			_debtMoney = computingCost.getDebtMoney();	//订单欠款
			_singleRealityPrice = computingCost.getSingleRealityPrice();		//实际服务单次价
			_singleNormPrice = computingCost.getSingleNormPrice();	//单次标价
			_actualNum = computingCost.getActualNum();	//剩余服务次数
			
			//添加商品订单"mtmy_order_goods_mapping"
			double totalAmount = 0; //实付款金额
			double orderBalance = 0; //订单余款
			double orderArrearage = 0; //订单欠款
			double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
			double appTotalAmount = 0; //app实付金额
			double appArrearage = 0;   //app欠款金额
			//当应付等于实付  余额和欠款都为0
			if(orderAmount.equals(actualPayment)){													
				totalAmount = actualPayment;	//实付款金额
				afterPaymentSum = afterPaymentSum + totalAmount;	//实际付款总额
				orderBalance = 0;	//订单余款
				spareMoneySum = spareMoneySum + orderBalance;	//总余额
				orderArrearage = 0;	//订单欠款
				debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
			}else{
				if(actualPayment > orderAmount){  //实付大于应付
					totalAmount = orderAmount;	//计算后实付款金额
					afterPaymentSum = afterPaymentSum + totalAmount;  //实际付款总额
					orderBalance = Double.parseDouble(formater.format(actualPayment-orderAmount));	//订单余款
					spareMoneySum = spareMoneySum+orderBalance;	//总余额
					orderArrearage = 0;	//订单欠款
					debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
					newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
					appTotalAmount = orderAmount;                   //app实付金额
					appArrearage = 0;                                 //app欠款金额
					newSpareMoneySum = newSpareMoneySum + Double.parseDouble(formater.format(actualPayment - orderAmount));//商品总余额
				}else{
					totalAmount = _afterPayment;	//计算后实付款金额
					afterPaymentSum = afterPaymentSum + totalAmount;	////实际付款总额
					orderBalance = _spareMoney;	//订单余款
					spareMoneySum = spareMoneySum + orderBalance;	//总余额
					orderArrearage = _debtMoney;	//订单欠款
					debtMoneySum = debtMoneySum + orderArrearage;		//总欠款
					newOrderBalance = Double.parseDouble(formater.format(actualPayment - _afterPayment));     //商品余额（只放在details里的OrderBalance）
					appTotalAmount = actualPayment;                   //app实付金额
					appArrearage = Double.parseDouble(formater.format(orderAmount - actualPayment)); //app欠款金额
				}
			}
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderid(orderid);
			orderGoods.setUserid(orders.getUserid());
			orderGoods.setGoodsid(goods.getGoodsId());
			orderGoods.setGoodsname(goods.getGoodsName());
			orderGoods.setGoodssn(goodspec.getGoodsNo());
			orderGoods.setOriginalimg(goods.getOriginalImg());
			orderGoods.setSpeckey(goodspec.getSpecKey());
			orderGoods.setSpeckeyname(goodspec.getSpecKeyValue());
			orderGoods.setCostprice(costprice);		//成本单价
			orderGoods.setMarketprice(marketPrice);		//市场单价
			orderGoods.setGoodsprice(price);	//优惠价
			orderGoods.setGoodsnum(1);	//成交数量
			orderGoods.setOrderAmount(orderAmount);		//应付金额
			orderGoods.setTotalAmount(totalAmount);	//计算后实付款金额
			orderGoods.setOrderBalance(orderBalance);	//订单余款
			orderGoods.setOrderArrearage(orderArrearage); //订单欠款
			orderGoods.setExpiringDate(goodspec.getExpiringDate());
			orderGoods.setSingleRealityPrice(_singleRealityPrice);	//实际服务单次价
			orderGoods.setSingleNormPrice(_singleNormPrice);	//单次标价
			orderGoods.setIsreal(3);	//  2套卡 3通用卡
			orderGoods.setServicetimes(serviceTimes);	//预计服务次数
			if(orders.getIsNeworder() == 0){
				orderGoods.setRealityAddTime(new Date());   //实际下单时间
			}else{
				orderGoods.setRealityAddTime(realityAddTimeList.get(i));   //实际下单时间
			}
			
			//保存 mtmy_order_goods_mapping
			orderGoodsDao.saveOrderGoods(orderGoods);
			
			recid += "," + orderGoods.getRecid();//保存mappingid到账户充值记录表中
			
			int groupId = orderGoods.getRecid();
			List<Goods> goodsListSon = ordersDao.selectCardSon(goods.getGoodsId());
			if(goodsListSon.size() > 0){
				//添加套卡商品的子项商品的
				/*double realityOrderAmountSum = 0d;*/
				for(int j=0;j<goodsListSon.size();j++){
					OrderGoods orderGoodsSon = new OrderGoods();
					orderGoodsSon.setOrderid(orderid);
					orderGoodsSon.setGroupId(groupId);
					orderGoodsSon.setUserid(orders.getUserid());
					orderGoodsSon.setGoodsid(goodsListSon.get(j).getGoodsId());
					orderGoodsSon.setGoodsname(goodsListSon.get(j).getGoodsName());
					orderGoodsSon.setOriginalimg(goodsListSon.get(j).getOriginalImg());
					orderGoodsSon.setMarketprice(goodsListSon.get(j).getMarketPrice());		//市场单价
					orderGoodsSon.setGoodsprice(goodsListSon.get(j).getShopPrice());	//优惠价
					orderGoodsSon.setRealityAddTime(new Date());   //实际下单时间
					orderGoodsSon.setExpiringDate(goodspec.getExpiringDate());
					if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 0){
						orderGoodsSon.setGoodsnum(goodsListSon.get(j).getGoodsNum());	//购买数量
					}else{
						orderGoodsSon.setGoodsnum(1);	//购买数量
					}
					orderGoodsSon.setIsreal(Integer.valueOf(goodsListSon.get(j).getIsReal()));	// 是否为虚拟 0 实物 1虚拟
					orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
					
					/*if(j < (goodsListSon.size()-1)){   //父类下的（前n-1个）子项商品平分父类的应付价格，
						double sonShopPrice = Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice()*goodsListSon.get(j).getGoodsNum()));
						double ratio = Double.parseDouble(formater.format(sonShopPrice/price));          //子类占父类的比例
						double realityOrderAmount = Double.parseDouble(formater.format(ratio*orderAmount)); //子类平分父类的应付价
						realityOrderAmountSum = realityOrderAmountSum + realityOrderAmount;     //子类平分父类的应付价总和      
						orderGoodsSon.setOrderAmount(realityOrderAmount);		//应付金额
						if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 1){
							orderGoodsSon.setSingleNormPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice())));	//单次标价
							orderGoodsSon.setSingleRealityPrice(Double.parseDouble(formater.format(realityOrderAmount)));	//实际服务单次价
							orderGoodsSon.setServicetimes(goodsListSon.get(j).getGoodsNum());	//预计服务次数
							orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
						}
					}else{        //最后一个子项的应付价格单独算（用父类的应付-前n个子项平分价格的总和）
						double realityOrderAmount = Double.parseDouble(formater.format(orderAmount - realityOrderAmountSum)); //子类平分父类的应付价
						orderGoodsSon.setOrderAmount(realityOrderAmount);		//应付金额
						if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 1){
							orderGoodsSon.setSingleNormPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice())));	//单次标价
							orderGoodsSon.setSingleRealityPrice(Double.parseDouble(formater.format(realityOrderAmount)));	//实际服务单次价
							orderGoodsSon.setServicetimes(goodsListSon.get(j).getGoodsNum());	//预计服务次数
							orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
						}
					}*/
					
					//保存 mtmy_order_goods_mapping
					orderGoodsDao.saveOrderGoods(orderGoodsSon);
				}
			}
			
			//订单商品详情记录表
			int _serviceTimes = 0;//剩余服务次数
			
			if(orderAmount.equals(actualPayment)){
				_serviceTimes = serviceTimes; //剩余服务次数等于 规格服务次数
			}else{
				if(actualPayment > orderAmount){  //实付大于应付
					_serviceTimes = serviceTimes; //剩余服务次数等于 规格服务次数
				}else{
					_serviceTimes = _actualNum; //剩余服务次数等于计算后的服务次数
				}
			}
			OrderGoodsDetails details = new OrderGoodsDetails();
			details.setOrderId(orderid);
			details.setGoodsMappingId(orderGoods.getRecid()+"");
			details.setItemAmount(0);	//项目金额
			details.setItemCapitalPool(0); //项目资金池
			details.setTotalAmount(totalAmount);	//实付款金额
			details.setOrderBalance(newOrderBalance);		//订单余款
			details.setOrderArrearage(orderArrearage);	//订单欠款
			details.setAppTotalAmount(appTotalAmount);    //app实付金额
			details.setAppArrearage(appArrearage);       //app欠款金额
			int isNeworder = orders.getIsNeworder();//新老订单
			if(isNeworder == 0){
				details.setServiceTimes(_serviceTimes);	//剩余服务次数
			}else{
				details.setServiceTimes(remaintimeNums.get(i));	//剩余服务次数
			}
			details.setType(0);
			details.setAdvanceFlag("0");
			details.setCreateOfficeId(user.getOffice().getId());
			details.setCreateBy(user);
			details.setBelongOfficeId(orders.getBelongOfficeId());
			//保存订单商品详情记录
			orderGoodsDetailsService.saveOrderGoodsDetails(details);
			
			if(orders.getIsNeworder() == 0){
				//同步数据到营业额明细表
				TurnOverDetails turnOverDetails = new TurnOverDetails();
				turnOverDetails.setOrderId(orderid);
				turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
				turnOverDetails.setDetailsId(details.getId());
				turnOverDetails.setType(1);
				turnOverDetails.setAmount(details.getAppTotalAmount());
				turnOverDetails.setUseBalance(details.getUseBalance());
				turnOverDetails.setStatus(0);
				turnOverDetails.setUserId(mtmyUserId);
				turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
				turnOverDetails.setCreateBy(UserUtils.getUser());
				turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
			}
		}
		
		
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		//主订单信息
		Orders _orders = new Orders();
		_orders.setOrderid(orderid);
		_orders.setParentid("0");
		_orders.setUserid(mtmyUserId);
		_orders.setOrderstatus(orders.getOrderstatus());
		_orders.setPayid(payment.getPayid());
		_orders.setPaycode(payment.getPaycode());
		_orders.setPayname(payment.getPayname());
		_orders.setGoodsprice(goodsprice);	//商品总价
		_orders.setOrderamount(orderAmountSum); //应付总金额
		_orders.setTotalamount(afterPaymentSum); //实付总额
		_orders.setOrderArrearage(debtMoneySum); //总欠款
		_orders.setOrderBalance(spareMoneySum); //总余额
		_orders.setIsReal(3);	//是否实物（0：实物商品；1：虚拟商品；2：套卡；3：通用卡）
		_orders.setIsNeworder(orders.getIsNeworder()); //是否新订单（0：新订单；1：老订单）
		_orders.setDistinction(orders.getDistinction()); //订单性质
		_orders.setOffice(user.getOffice());
		_orders.setCreateBy(user);
		_orders.setDelFlag("0");
		_orders.setChannelFlag("bm");
		_orders.setShippingstatus(2);
		_orders.setShippingtype(2);
		_orders.setUsernote(orders.getUsernote());
		_orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
		_orders.setBelongOfficeId(orders.getBelongOfficeId());
		ordersDao.saveVirtualOrder(_orders);
		
		//根据用户id查询用户账户信息
		Orders account = ordersDao.getAccount(_orders);
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance);
			newAccount.setUserid(_orders.getUserid());
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}else{
			double accountBalance = account.getAccountBalance()+newSpareMoneySum;	//账户余额信息
			account.setAccountBalance(accountBalance);
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}
		
		
		//保存提成人员信息 
		List<String> sysUserId = orders.getSysUserId();	//提成人员id
		List<Double> pushMoney = orders.getPushMoney();	//提成金额
		if(sysUserId!=null && sysUserId.size()>0){
			OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
			for (int i = 0; i < sysUserId.size(); i++) {
				orderPushmoneyRecord.setPushmoneyUserId(sysUserId.get(i));

				//通过业务员id(属于妃子校的)查询业务员归属机构
				orderPushmoneyRecord = orderPushmoneyRecordService.getOfficeIdByUserId(orderPushmoneyRecord);				
				
				orderPushmoneyRecord.setOrderId(orderid);
				orderPushmoneyRecord.setPushMoney(pushMoney.get(i));
				orderPushmoneyRecord.setOfficeId(user.getOffice().getId());
				orderPushmoneyRecord.setCreateBy(user);
				orderPushmoneyRecord.setDelFlag("0");
				orderPushmoneyRecordService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
			}
		}
		
		//批量添加订单备注
		if (orders.getOrderRemarks()!=null && orders.getOrderRemarks().size()>0) {
			ordersDao.saveOrderRemarksLog(orders);
		}
		if(orders.getIchecks() == 1){
			String personheadContent = orders.getPersonheadContent();
			String companyheadContent = orders.getCompanyheadContent();//公司发票抬头
			if(!"".equals(companyheadContent)){//等于空 就是个人  不等于空就是 公司
				orders.setHeadContent(companyheadContent);
			}else{
				orders.setHeadContent(personheadContent);
			}
			//保存发票信息
			orderInvoiceService.saveOrderInvoice(orders);
			OrderInvoiceRelevancy orderInvoiceRelevancy = new OrderInvoiceRelevancy();
			orderInvoiceRelevancy.setOrderId(orderid);
			orderInvoiceRelevancy.setInvoiceId(orders.getInvoiceId());
			ordersDao.saveOrderInvoiceRelevancy(orderInvoiceRelevancy);
		}
	}
	
	/**
	 * 保存套卡订单
	 * @param orders
	 */
	public void saveSuitCardOrder(Orders orders) {
		DecimalFormat formater = new DecimalFormat("#0.##");   //四舍五入
		int numSum = 0;
		User user = UserUtils.getUser(); //登陆用户
		int mtmyUserId = orders.getUserid();	//每天每耶用户id
		String orderid = createOrder(mtmyUserId, 0, 0);//订单id
		orders.setOrderid(orderid);
		orders.setCreateBy(user);
		//页面传递到后台的数据
		List<Integer> goodselectIds = orders.getGoodselectIds();	//商品id集合
		List<Double> orderAmounts = orders.getOrderAmounts();		//成交价集合
		List<Double> actualPayments = orders.getActualPayments();	//实际付款集合
		List<Integer> remaintimeNums = orders.getRemaintimeNums();	//套卡订单老产品-实际次数
		
		double orderAmountSum = 0d;  //应付总额
		double afterPaymentSum = 0d;  //实际付款总额
		double debtMoneySum = 0d;	//总欠款
		double spareMoneySum = 0d;	//总余额
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于应付时，将多的存入个人账户余额中)
		double goodsprice = 0;  //商品总价
		
		String recid = "";//保存mappingid到账户充值记录表中
		
		double usedSurplusAmount = 0;
		for (Integer i = 0; i < goodselectIds.size(); i++) {
			Integer goodselectId = goodselectIds.get(i);		//商品id
			//通过商品id获取当前商品
			Goods goods = goodsDao.getgoods(goodselectId.toString());
			
			GoodsSpecPrice goodspec = goodsSpecPriceDao.selectSuitCard(goodselectId);//查询当前套卡默认规格对应的数据信息
			
			Double orderAmount = orderAmounts.get(i);			//应付金额
			orderAmountSum = orderAmountSum + orderAmount;		//应付总额
			Double actualPayment = actualPayments.get(i);		//实际付款
			afterPaymentSum = afterPaymentSum + actualPayment;	//实际付款总额
			
			double costPrice = goods.getCostPrice();  //     系统价
			double price = goods.getShopPrice();	//优惠价格
			double marketPrice = goods.getMarketPrice();	//市场单价
			
			double actualPayment_on = 0;		//实付款（入库）
			double orderArrearage_on = 0;		//欠款（入库）
			double orderBalance_on = 0;			//余额（入库）
			double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
			double appTotalAmount = 0; //app实付金额
			double appArrearage = 0;   //app欠款金额
			goodsprice += price;
			if(orderAmount > actualPayment){
				//应付 > 实付
				actualPayment_on = actualPayment;
				orderArrearage_on = Double.parseDouble(formater.format(orderAmount - actualPayment_on));
				orderBalance_on = 0;
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = Double.parseDouble(formater.format(orderAmount - actualPayment)); //app欠款金额
			}else if(orderAmount.equals(actualPayment)){
				//应付 = 实付
				actualPayment_on = actualPayment;
				orderArrearage_on = 0;
				orderBalance_on = 0;
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = actualPayment;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
			}else if(orderAmount < actualPayment){
				//应付 < 实付
				actualPayment_on = orderAmount;
				orderArrearage_on = 0;
				orderBalance_on = Double.parseDouble(formater.format(actualPayment - actualPayment_on));
				newOrderBalance = 0;                              //商品余额（只放在details里的OrderBalance）
				appTotalAmount = orderAmount;                   //app实付金额
				appArrearage = 0;                                 //app欠款金额
				newSpareMoneySum = newSpareMoneySum + Double.parseDouble(formater.format(actualPayment - orderAmount));//商品总余额
			}
			spareMoneySum = spareMoneySum + orderBalance_on;	//总余额
			debtMoneySum = debtMoneySum + orderArrearage_on;	//总欠款
			
 			//添加商品订单"mtmy_order_goods_mapping"
			
			//添加套卡商品本身的
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setOrderid(orderid);
			orderGoods.setUserid(orders.getUserid());
			orderGoods.setGoodsid(goods.getGoodsId());
			orderGoods.setGoodsname(goods.getGoodsName());
			orderGoods.setGoodssn(goods.getGoodsSn());
			orderGoods.setOriginalimg(goods.getOriginalImg());
			orderGoods.setSpeckey(goodspec.getSpecKey());
			orderGoods.setSpeckeyname(goodspec.getSpecKeyValue());
			orderGoods.setCostprice(costPrice);		//系统价
			orderGoods.setMarketprice(marketPrice);		//市场单价
			orderGoods.setGoodsprice(price);	//优惠价
			orderGoods.setGoodsnum(1);	//购买数量
			orderGoods.setOrderAmount(orderAmount);		//应付金额
			orderGoods.setTotalAmount(actualPayment_on);	//计算后实付款金额
			orderGoods.setOrderBalance(orderBalance_on);	//订单余款
			orderGoods.setOrderArrearage(orderArrearage_on);	//订单欠款
			orderGoods.setExpiringDate(goodspec.getExpiringDate());
			orderGoods.setRealityAddTime(new Date());   //实际下单时间
			orderGoods.setIsreal(2);	// 是否为虚拟 0 实物 1虚拟 2套卡 3通用卡
			//保存 mtmy_order_goods_mapping
			orderGoodsDao.saveOrderGoods(orderGoods);
			
			recid += "," + orderGoods.getRecid();//保存mappingid到账户充值记录表中
			
			int groupId = orderGoods.getRecid();
			List<Goods> goodsListSon = ordersDao.selectCardSon(goods.getGoodsId());
			if(goodsListSon.size() > 0){
				//添加套卡商品的子项商品的
				double realityOrderAmountSum = 0d;
				for(int j=0;j<goodsListSon.size();j++){
					OrderGoods orderGoodsSon = new OrderGoods();
					orderGoodsSon.setOrderid(orderid);
					orderGoodsSon.setGroupId(groupId);
					orderGoodsSon.setUserid(orders.getUserid());
					orderGoodsSon.setGoodsid(goodsListSon.get(j).getGoodsId());
					orderGoodsSon.setGoodsname(goodsListSon.get(j).getGoodsName());
					orderGoodsSon.setOriginalimg(goodsListSon.get(j).getOriginalImg());
					orderGoodsSon.setSpeckeyname(String.valueOf(goodsListSon.get(j).getGoodsNum()));  //套卡将子项的规格放到spec_key_name
					orderGoodsSon.setMarketprice(goodsListSon.get(j).getMarketPrice());		//市场单价
					orderGoodsSon.setGoodsprice(goodsListSon.get(j).getShopPrice());	//优惠价
					orderGoodsSon.setRealityAddTime(new Date());   //实际下单时间
					orderGoodsSon.setExpiringDate(goodspec.getExpiringDate());
					
					orderGoodsSon.setIsreal(Integer.valueOf(goodsListSon.get(j).getIsReal()));	// 是否为虚拟 0 实物 1虚拟
					
					
					if(orderAmount - price != 0){  //若讨价还价
						if(j < (goodsListSon.size()-1)){   //父类下的（前n-1个）子项商品平分父类的应付价格，
							double sonShopPrice = goodsListSon.get(j).getShopPrice();
							double ratio = Double.parseDouble(formater.format(sonShopPrice/price));          //子类占父类的比例
							double realityOrderAmount = Double.parseDouble(formater.format(ratio*orderAmount)); //子类平分父类的应付价
							realityOrderAmountSum = realityOrderAmountSum + realityOrderAmount;     //子类平分父类的应付价总和      
							orderGoodsSon.setOrderAmount(realityOrderAmount);		//应付金额
							if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 1){
								//实际服务单次价，单次标价舍弃两位小数后的
								orderGoodsSon.setSingleNormPrice(((int)((goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//单次标价
								orderGoodsSon.setSingleRealityPrice(((int)((realityOrderAmount/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//实际服务单次价
								
								//实际服务单次价，单次标价四舍五入
								/*orderGoodsSon.setSingleNormPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())));	//单次标价
								orderGoodsSon.setSingleRealityPrice(Double.parseDouble(formater.format(realityOrderAmount/goodsListSon.get(j).getGoodsNum())));	//实际服务单次价*/
								orderGoodsSon.setServicetimes(goodsListSon.get(j).getGoodsNum());	//预计服务次数
								orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
								orderGoodsSon.setGoodsnum(1);	//购买数量
							}else{
								orderGoodsSon.setGoodsnum(goodsListSon.get(j).getGoodsNum());	//购买数量
							}
						}else{        //最后一个子项的应付价格单独算（用父类的应付-前n个子项平分价格的总和）
							double realityOrderAmount = Double.parseDouble(formater.format(orderAmount - realityOrderAmountSum)); //子类平分父类的应付价
							orderGoodsSon.setOrderAmount(realityOrderAmount);		//应付金额
							if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 1){
								//实际服务单次价，单次标价舍弃两位小数后的
								orderGoodsSon.setSingleNormPrice(((int)((goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//单次标价
								orderGoodsSon.setSingleRealityPrice(((int)((realityOrderAmount/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//实际服务单次价
								
								
								//实际服务单次价，单次标价四舍五入
								/*orderGoodsSon.setSingleNormPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())));	//单次标价
								orderGoodsSon.setSingleRealityPrice(Double.parseDouble(formater.format(realityOrderAmount/goodsListSon.get(j).getGoodsNum())));	//实际服务单次价*/
								orderGoodsSon.setServicetimes(goodsListSon.get(j).getGoodsNum());	//预计服务次数
								orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
								orderGoodsSon.setGoodsnum(1);	//购买数量
							}else{
								orderGoodsSon.setGoodsnum(goodsListSon.get(j).getGoodsNum());	//购买数量
							}
						}
					}else{             //若未讨价还价    ((int)(afs*100))/100.0舍弃两位小数后的
						orderGoodsSon.setOrderAmount(goodsListSon.get(j).getShopPrice());		//应付金额
						if(Integer.valueOf(goodsListSon.get(j).getIsReal()) == 1){
							//实际服务单次价，单次标价舍弃两位小数后的
							orderGoodsSon.setSingleNormPrice(((int)((goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//单次标价
							orderGoodsSon.setSingleRealityPrice(((int)((goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())*100))/100.0);	//实际服务单次价
							
							//实际服务单次价，单次标价四舍五入
							/*orderGoodsSon.setSingleNormPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())));	//单次标价
							orderGoodsSon.setSingleRealityPrice(Double.parseDouble(formater.format(goodsListSon.get(j).getShopPrice()/goodsListSon.get(j).getGoodsNum())));	//实际服务单次价*/							
							
							if(orders.getIsNeworder() == 0){//新订单
								orderGoodsSon.setServicetimes(goodsListSon.get(j).getGoodsNum());	//预计服务次数
							}else if(orders.getIsNeworder() == 1){//老订单
								orderGoodsSon.setServicetimes(remaintimeNums.get(numSum));
								usedSurplusAmount = usedSurplusAmount + Double.parseDouble(formater.format((goodsListSon.get(j).getGoodsNum()-remaintimeNums.get(numSum))*orderGoodsSon.getSingleRealityPrice()));
								numSum = numSum + 1;
							}
							orderGoodsSon.setServicemin(goodsListSon.get(j).getServiceMin());//服务时长
							orderGoodsSon.setGoodsnum(1);	//购买数量
						}else{
							if(orders.getIsNeworder() == 0){//新订单
								orderGoodsSon.setGoodsnum(goodsListSon.get(j).getGoodsNum());	//购买数量
							}else if(orders.getIsNeworder() == 1){//老订单
								orderGoodsSon.setGoodsnum(remaintimeNums.get(numSum));	//购买数量
								numSum = numSum + 1;
							}
						}
					}
					
					//保存 mtmy_order_goods_mapping
					orderGoodsDao.saveOrderGoods(orderGoodsSon);
				}
			}
			
			//订单商品详情记录表
			OrderGoodsDetails details = new OrderGoodsDetails();
			details.setOrderId(orderid);
			details.setGoodsMappingId(orderGoods.getRecid()+"");
			details.setTotalAmount(actualPayment_on);	//计算后实付款金额
			details.setOrderBalance(newOrderBalance);	//商品余额
			details.setOrderArrearage(orderArrearage_on);	//订单欠款
			details.setAppTotalAmount(appTotalAmount);  //app实付金额
			details.setAppArrearage(appArrearage);   //app欠款金额
			if(orders.getIsNeworder() == 0){
				details.setSurplusAmount(actualPayment_on); //套卡剩余金额
			}else{
				details.setSurplusAmount(Double.parseDouble(formater.format(actualPayment_on-usedSurplusAmount))); //套卡剩余金额
			}
			details.setType(0);
			details.setAdvanceFlag("0");
			details.setCreateOfficeId(user.getOffice().getId());
			details.setCreateBy(user);
			details.setBelongOfficeId(orders.getBelongOfficeId());
			//保存订单商品详情记录
			orderGoodsDetailsService.saveOrderGoodsDetails(details);
			
			if(orders.getIsNeworder() == 0){
				//同步数据到营业额明细表
				TurnOverDetails turnOverDetails = new TurnOverDetails();
				turnOverDetails.setOrderId(orderid);
				turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
				turnOverDetails.setDetailsId(details.getId());
				turnOverDetails.setType(1);
				turnOverDetails.setAmount(details.getAppTotalAmount());
				turnOverDetails.setUseBalance(details.getUseBalance());
				turnOverDetails.setStatus(0);
				turnOverDetails.setUserId(mtmyUserId);
				turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
				turnOverDetails.setCreateBy(UserUtils.getUser());
				turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
			}
			
			usedSurplusAmount = 0;
		}
		
		Payment payment = paymentDao.getByCode(orders.getPaycode());
		//主订单信息
		Orders _orders = new Orders();
		_orders.setOrderid(orderid);
		_orders.setParentid("0");
		_orders.setUserid(mtmyUserId);
		_orders.setOrderstatus(orders.getOrderstatus());
		_orders.setPayid(payment.getPayid());
		_orders.setPaycode(payment.getPaycode());
		_orders.setPayname(payment.getPayname());
		_orders.setGoodsprice(goodsprice);	//商品总价
		_orders.setOrderamount(orderAmountSum); //应付总金额
		_orders.setTotalamount(afterPaymentSum); //实付总额
		_orders.setOrderArrearage(debtMoneySum); //总欠款
		_orders.setOrderBalance(spareMoneySum); //总余额
		_orders.setIsReal(2);	//是否实物（0：实物商品；1：虚拟商品；2：套卡；3：通用卡）
		_orders.setIsNeworder(orders.getIsNeworder()); //是否新订单（0：新订单；1：老订单）
		_orders.setDistinction(orders.getDistinction()); //订单性质
		_orders.setOffice(user.getOffice());
		_orders.setDelFlag("0");
		_orders.setChannelFlag("bm");
		_orders.setShippingstatus(2);
		_orders.setShippingtype(2);
		_orders.setConsignee(orders.getConsignee());
		_orders.setPhone(orders.getPhone());
		_orders.setAddress(orders.getAddress());
		_orders.setUsernote(orders.getUsernote());
		_orders.setInvoiceOvertime(getMaxMonthDate(new Date()));
		_orders.setCreateBy(user);
		_orders.setBelongOfficeId(orders.getBelongOfficeId());
		ordersDao.saveVirtualOrder(_orders);
		
		//根据用户id查询用户账户信息
		Orders account = ordersDao.getAccount(_orders);
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance);
			newAccount.setUserid(_orders.getUserid());
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}else{
			double accountBalance = account.getAccountBalance()+newSpareMoneySum;	//账户余额信息
			account.setAccountBalance(accountBalance);
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(orderid, mtmyUserId, newSpareMoneySum, type, _orders.getChannelFlag(), user, recid);
		}
		
		//保存提成人员信息 
		List<String> sysUserId = orders.getSysUserId();	//提成人员id
		List<Double> pushMoney = orders.getPushMoney();	//提成金额
		if(sysUserId!=null && sysUserId.size()>0){
			OrderPushmoneyRecord orderPushmoneyRecord = new OrderPushmoneyRecord();
			for (int i = 0; i < sysUserId.size(); i++) {
				orderPushmoneyRecord.setPushmoneyUserId(sysUserId.get(i));

				//通过业务员id(属于妃子校的)查询业务员归属机构
				orderPushmoneyRecord = orderPushmoneyRecordService.getOfficeIdByUserId(orderPushmoneyRecord);				
				
				orderPushmoneyRecord.setOrderId(orderid);
				orderPushmoneyRecord.setPushMoney(pushMoney.get(i));
				orderPushmoneyRecord.setOfficeId(user.getOffice().getId());
				orderPushmoneyRecord.setCreateBy(user);
				orderPushmoneyRecord.setDelFlag("0");
				orderPushmoneyRecordService.saveOrderPushmoneyRecord(orderPushmoneyRecord);
			}
		}
		//批量添加订单备注
		if (orders.getOrderRemarks()!=null && orders.getOrderRemarks().size()>0) {
			ordersDao.saveOrderRemarksLog(orders);
		}
		if(orders.getIchecks() == 1){
			String personheadContent = orders.getPersonheadContent();
			String companyheadContent = orders.getCompanyheadContent();//公司发票抬头
			if(!"".equals(companyheadContent)){//等于空 就是个人  不等于空就是 公司
				orders.setHeadContent(companyheadContent);
			}else{
				orders.setHeadContent(personheadContent);
			}
			//保存发票信息
			orderInvoiceService.saveOrderInvoice(orders);
			OrderInvoiceRelevancy orderInvoiceRelevancy = new OrderInvoiceRelevancy();
			orderInvoiceRelevancy.setOrderId(orderid);
			orderInvoiceRelevancy.setInvoiceId(orders.getInvoiceId());
			ordersDao.saveOrderInvoiceRelevancy(orderInvoiceRelevancy);
		}
	}
	
	/**
	 * 新增卡项订单充值日志记录
	 * @param oLog
	 */
	public void addCardOrderRechargeLog(OrderRechargeLog oLog){
		DecimalFormat formater = new DecimalFormat("#0.##");
		//获取基本值
		User user = UserUtils.getUser(); //登陆用户
		double totalAmount = oLog.getTotalAmount(); //实付款金额(充值金额+使用的账户余额)
		double accountBalance = oLog.getAccountBalance(); //使用的账户余额
		double singleRealityPrice = oLog.getSingleRealityPrice(); //实际服务单次价
		double orderArrearage = oLog.getOrderArrearage(); //欠款
		int _servicetimes = oLog.getServicetimes(); //预计服务次数
		
		OrderGoodsDetails newDetails = orderGoodsDetailsService.selectOrderBalance(oLog.getRecid());
		double sumOrderBalance = newDetails.getOrderBalance();//该订单的该商品剩余的可用余额，充值时必须用
		double sumAppTotalAmount = newDetails.getAppTotalAmount();//该订单的已付金额
		double sumAppArrearage = newDetails.getAppArrearage();  //该订单仍欠的款
		double goodsPrice = newDetails.getGoodsPrice();   //商品的优惠价格
		double orderAmount = newDetails.getOrderAmount();//商品应付价格
		int integral = newDetails.getIntegral();        //充值完全以后送的云币
		
		double couponPrice = newDetails.getCouponPrice();      //红包面值
		double memberGoodsPrice = newDetails.getMemberGoodsPrice(); //使用了会员折扣优惠的钱 
		double advancePrice = newDetails.getAdvancePrice();        //预约金
		
		double newTotalAmount = Double.parseDouble(formater.format(totalAmount + sumOrderBalance));//实付款金额 =充值金额+使用的账户余额+必须使用的商品剩余可用余额
		int serviceTimes_in = 0;//剩余服务次数
		double totalAmount_in = 0;//实付款金额（入库）
		
		double newSpareMoneySum = 0d;  //商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
		double newOrderBalance = 0; //商品余额（只放在details里的OrderBalance）
		double appTotalAmount = 0; //app实付金额
		double appArrearage = 0;   //app欠款金额
		double surplusAmount = 0;   //套卡剩余金额
		int userIntegral = 0;   //入库赠送的云币
		
		if(3 == oLog.getIsReal()){ //通用卡
			if(singleRealityPrice <= newTotalAmount && newTotalAmount < orderArrearage){
				// 实际单次标价  < 实付款金额	< 欠款
				serviceTimes_in = (int)Math.floor(newTotalAmount/singleRealityPrice);//充值次数
				totalAmount_in = serviceTimes_in * singleRealityPrice;//实付金额
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = Double.parseDouble(formater.format(newTotalAmount - totalAmount_in - sumOrderBalance));//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额
			}else if(newTotalAmount >= orderArrearage){
				//实付款金额	>=  欠款
				serviceTimes_in = _servicetimes-oLog.getRemaintimes();//充值次数
				totalAmount_in = orderArrearage;//实付金额
				
				newSpareMoneySum = Double.parseDouble(formater.format(newTotalAmount - orderArrearage - accountBalance));//商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
				newOrderBalance = -sumOrderBalance;//商品余额（只放在details里的OrderBalance）
				if("bm".equals(oLog.getChannelFlag())){
					appTotalAmount =  Double.parseDouble(formater.format(orderAmount - sumAppTotalAmount));//app实付金额
				}else{
					if(couponPrice < advancePrice){
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - couponPrice - memberGoodsPrice));//app实付金额
					}else{
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - advancePrice - memberGoodsPrice));//app实付金额
					}
				}
				appArrearage = -sumAppArrearage;//app欠款金额
				
				if(!"bm".equals(oLog.getChannelFlag())){   //app或者wap下单送云币
					//当充值全部欠款以后，对用户进行送云币
					if(integral > 0){
						boolean str = redisClientTemplate.exists(MTMY_ID+oLog.getMtmyUserId());
						if(str){   //若缓存存在，则操作缓存
							RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+oLog.getMtmyUserId());
							redisLock.lock();
							redisClientTemplate.incrBy(MTMY_ID+oLog.getMtmyUserId(),integral);
							redisLock.unlock();
						}else{         //若缓存不存在，则操作mtmy_user_accounts
							userIntegral = integral;            //赠送云币
						}
						
						//在mtmy_integrals_log表中插入日志
						IntegralsLog integralsLog = new IntegralsLog();
						integralsLog.setUserId(oLog.getMtmyUserId());
						integralsLog.setIntegralType(0);
						integralsLog.setIntegralSource(0);
						integralsLog.setActionType(21);
						integralsLog.setIntegral(integral);
						integralsLog.setOrderId(oLog.getOrderId());
						integralsLog.setRemark("商品赠送");
						ordersDao.insertIntegralLog(integralsLog);
					}
				}
				
			}else if(singleRealityPrice > newTotalAmount){//实际单次标价  > 实付款金额
				totalAmount_in = 0;
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = totalAmount;//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额

			}
		}else{//套卡
			if(newTotalAmount<orderArrearage){
				//实际付款 < 欠款
				totalAmount_in = totalAmount;
				
				newSpareMoneySum = -accountBalance;
				newOrderBalance = 0;//商品余额（只放在details里的OrderBalance）
				appTotalAmount =  Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app实付金额
				appArrearage = -Double.parseDouble(formater.format(oLog.getRechargeAmount()+accountBalance));//app欠款金额
				surplusAmount = totalAmount;     //套卡剩余金额
			}else if(newTotalAmount >= orderArrearage){    
				//实际付款 >= 欠款
				totalAmount_in = orderArrearage;
				
				newSpareMoneySum = Double.parseDouble(formater.format(newTotalAmount - orderArrearage - accountBalance));//商品总余额(当实付大于欠款时，将多的存入个人账户余额中)
				newOrderBalance = 0;//商品余额（只放在details里的OrderBalance）
				if("bm".equals(oLog.getChannelFlag())){
					appTotalAmount =  Double.parseDouble(formater.format(orderAmount - sumAppTotalAmount));//app实付金额
				}else{
					if(couponPrice < advancePrice){
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - couponPrice - memberGoodsPrice));//app实付金额
					}else{
						appTotalAmount =  Double.parseDouble(formater.format(goodsPrice - sumAppTotalAmount - advancePrice - memberGoodsPrice));//app实付金额
					}
				}
				appArrearage = -sumAppArrearage;//app欠款金额
				surplusAmount = orderArrearage;     //套卡剩余金额
				
				if(!"bm".equals(oLog.getChannelFlag())){   //app或者wap下单送云币
					//当充值全部欠款以后，对用户进行送云币
					if(integral > 0){
						boolean str = redisClientTemplate.exists(MTMY_ID+oLog.getMtmyUserId());
						if(str){   //若缓存存在，则操作缓存
							RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+oLog.getMtmyUserId());
							redisLock.lock();
							redisClientTemplate.incrBy(MTMY_ID+oLog.getMtmyUserId(),integral);
							redisLock.unlock();
						}else{         //若缓存不存在，则操作mtmy_user_accounts
							userIntegral = integral;            //赠送云币
						}
						
						//在mtmy_integrals_log表中插入日志
						IntegralsLog integralsLog = new IntegralsLog();
						integralsLog.setUserId(oLog.getMtmyUserId());
						integralsLog.setIntegralType(0);
						integralsLog.setIntegralSource(0);
						integralsLog.setActionType(21);
						integralsLog.setIntegral(integral);
						integralsLog.setOrderId(oLog.getOrderId());
						integralsLog.setRemark("商品赠送");
						ordersDao.insertIntegralLog(integralsLog);
					}
				}
				
			}
		}
		
		//保存订单商品详情记录表
		OrderGoodsDetails details = new OrderGoodsDetails();
		details.setOrderId(oLog.getOrderId());
		details.setGoodsMappingId(oLog.getRecid()+"");
		details.setOrderBalance(newOrderBalance);	//商品余额
		details.setTotalAmount(totalAmount_in);	//实付款金额
		details.setOrderArrearage(-totalAmount_in);	//订单欠款
		details.setItemAmount(0);	//项目金额
		details.setItemCapitalPool(0); //项目资金池
		details.setServiceTimes(serviceTimes_in);	//剩余服务次数
		details.setAppTotalAmount(appTotalAmount);//app实付金额
		details.setAppArrearage(appArrearage); //app欠款金额
		details.setSurplusAmount(surplusAmount); //套卡剩余金额
		details.setType(0);
		details.setAdvanceFlag("3");
		details.setCreateOfficeId(user.getOffice().getId());
		details.setCreateBy(user);
		details.setBelongOfficeId(oLog.getBelongOfficeId());
		details.setUseBalance(accountBalance);
		//保存订单商品详情记录
		orderGoodsDetailsService.saveOrderGoodsDetails(details);
		
		//同步数据到营业额明细表
		TurnOverDetails turnOverDetails = new TurnOverDetails();
		turnOverDetails.setOrderId(details.getOrderId());
		turnOverDetails.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
		turnOverDetails.setDetailsId(details.getId());
		turnOverDetails.setType(2);
		turnOverDetails.setAmount(details.getAppTotalAmount());
		turnOverDetails.setUseBalance(details.getUseBalance());
		turnOverDetails.setStatus(3);
		turnOverDetails.setUserId(oLog.getMtmyUserId());
		turnOverDetails.setBelongOfficeId(details.getBelongOfficeId());
		turnOverDetails.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails);
		
		//根据用户id查询用户账户信息
		Orders _orders = new Orders();
		_orders.setUserid(oLog.getMtmyUserId());
		Orders account = ordersDao.getAccount(_orders);
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance_ = newSpareMoneySum;   //账户余额信息
			newAccount.setAccountBalance(accountBalance_);
			newAccount.setUserid(_orders.getUserid());
			newAccount.setUserIntegral(userIntegral);    //要赠送的云币
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}else{
			double accountBalance_ = 0;
			accountBalance_ = Double.parseDouble(formater.format(account.getAccountBalance()+newSpareMoneySum));
			account.setAccountBalance(accountBalance_);
			account.setUserIntegral(account.getUserIntegral() + userIntegral);   //要赠送的云币
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}
		
	}
	
	/**
	 * 卡项处理预约金
	 * @param oLog
	 * @param orderAmount应付款金额
	 * @param goodsPrice商品优惠单价
	 */
	public void handleCardAdvance(OrderRechargeLog oLog,double goodsPrice,double detailsTotalAmount,int goodsType,String officeId,int isReal,double realAdvancePrice,OrderGoodsDetails oldDetails){
		//获取基本值
		User user = UserUtils.getUser(); //登陆用户
		double totalAmount = oLog.getTotalAmount(); //实付款金额
		double accountBalance = oLog.getAccountBalance(); //用了多少账户的余额
		double singleRealityPrice = oLog.getSingleRealityPrice(); //实际服务单次价
		double advance = oLog.getAdvance();
		
		int serviceTimes_in = 0;//实际的充值次数
		int serviceTimes_in_a = 0; //充值次数
		double totalAmount_in_a = 0;//实付款金额
		double totalAmount_in = 0;//实付款金额（入库）
		double accountBalance_in = 0;//余额
		double newSpareMoneySum = 0d;  //总余额(实际单次标价  < 商品优惠单价 < 预付金时，将多的存入个人账户余额中)
		
		double appTotalAmount = 0; //app实付金额
		double appArrearage = 0;   //app欠款金额
		double surplusAmount = 0;   //套卡剩余金额(套卡的才存)
		
		double itemAmount; //项目金额
		double itemCapitalPool; //项目资金池
		
		DecimalFormat formater = new DecimalFormat("#0.##");
		if(isReal == 2){  //套卡
			if(singleRealityPrice > advance){   //实际服务单次价>预约金
				totalAmount_in_a = singleRealityPrice;//实付金额
				accountBalance_in = 0;                                      //订单余款
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));        //存入库的实付款金额=实付金额-订金
				newSpareMoneySum = -accountBalance;              
				appTotalAmount = Double.parseDouble(formater.format(singleRealityPrice - advance));  //app实付金额                 
				appArrearage = -Double.parseDouble(formater.format(singleRealityPrice - advance));  //app欠款金额 
				surplusAmount = Double.parseDouble(formater.format(singleRealityPrice - advance));  //套卡剩余金额(套卡的才存)
			}else{  //实际服务单次价<=预约金
				totalAmount_in_a = advance;//实付金额
				accountBalance_in = 0;                                      //订单余款
				totalAmount_in = 0;       //存入库的实付款金额
				newSpareMoneySum = -accountBalance;    
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
				surplusAmount = 0;  //套卡剩余金额(套卡的才存)
			}
		}else if(isReal == 3){   //通用卡
			if(singleRealityPrice < advance && advance > goodsPrice){
				// 实际单次标价  < 商品优惠单价 < 预付金
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = serviceTimes_in_a * singleRealityPrice;//实付金额
				accountBalance_in = 0;                                      //订单余款
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));        //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = Double.parseDouble(formater.format(advance - goodsPrice));    //总余额(实际单次标价  < 商品优惠单价 < 预付金时，将多的存入个人账户余额中)
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(advance == goodsPrice){     //预约金=商品优惠单价
				serviceTimes_in = oLog.getServicetimes() - 1;                               //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = advance;//实付金额
				accountBalance_in =  0;     //订单余款
				totalAmount_in = 0;        //存入库的实付款金额
				newSpareMoneySum = 0;
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(singleRealityPrice < advance && advance < goodsPrice){
				// 实际单次标价  < 预付金	<= 商品优惠单价
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次
				totalAmount_in_a = serviceTimes_in_a * singleRealityPrice;//实付金额
				accountBalance_in =  Double.parseDouble(formater.format(totalAmount - totalAmount_in_a));     //订单余款
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));        //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = -accountBalance;
				appTotalAmount = 0;                   //app实付金额
				appArrearage = 0; //app欠款金额
			}else if(singleRealityPrice >= advance){
				//实际单次标价  >= 预付金
				serviceTimes_in_a = (int)Math.floor(totalAmount/singleRealityPrice);//充值次数
				serviceTimes_in = serviceTimes_in_a - 1;                                //实际的充值次数，因为预约的时候给了一次，入库的时候减少一次,此时的其实也就是0
				totalAmount_in_a = singleRealityPrice;//实付金额
				accountBalance_in = 0;                                 //订单余款，
				totalAmount_in = Double.parseDouble(formater.format(totalAmount_in_a - advance));       //存入库的实付款金额=实付款金额-订金
				newSpareMoneySum = -accountBalance;
				appTotalAmount = Double.parseDouble(formater.format(singleRealityPrice - advance));  //app实付金额
				appArrearage = -Double.parseDouble(formater.format(singleRealityPrice - advance)); //app欠款金额
			}
			
		}
			
		itemAmount = 0; //项目金额
		itemCapitalPool = 0; //项目资金池
		
		//保存订单商品详情记录表
		OrderGoodsDetails details = new OrderGoodsDetails();
		details.setOrderId(oLog.getOrderId());
		details.setGoodsMappingId(oLog.getRecid()+"");
		details.setTotalAmount(totalAmount_in);	//实付款金额
		details.setOrderBalance(accountBalance_in);	//订单余款
		details.setOrderArrearage(Double.parseDouble(formater.format(advance - totalAmount_in_a)));	//订单欠款
		details.setItemAmount(itemAmount);	//项目金额
		details.setItemCapitalPool(itemCapitalPool); //项目资金池
		details.setServiceTimes(serviceTimes_in);	//剩余服务次数
		details.setAppTotalAmount(appTotalAmount);   //app实付金额
		details.setAppArrearage(appArrearage);        //app欠款金额
		details.setSurplusAmount(surplusAmount);   //套卡剩余金额(套卡的才存)
		details.setType(0);
		details.setAdvanceFlag("2");
		details.setCreateOfficeId(user.getOffice().getId());
		details.setCreateBy(user);
		details.setUseBalance(accountBalance);
		//保存订单商品详情记录
		orderGoodsDetailsService.saveOrderGoodsDetails(details);
		
		//同步数据到营业额明细表
		//第一次，同步下单的那条数据
		TurnOverDetails turnOverDetails1 = new TurnOverDetails();
		turnOverDetails1.setOrderId(details.getOrderId());
		turnOverDetails1.setMappingId(Integer.valueOf(oldDetails.getGoodsMappingId()));
		turnOverDetails1.setDetailsId(oldDetails.getId());
		turnOverDetails1.setType(1);
		turnOverDetails1.setAmount(oldDetails.getAppTotalAmount());
		turnOverDetails1.setUseBalance(oldDetails.getUseBalance());
		turnOverDetails1.setStatus(1);
		turnOverDetails1.setUserId(oLog.getMtmyUserId());
		turnOverDetails1.setBelongOfficeId(officeId);
		turnOverDetails1.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails1);
		
		//第一次，同步处理预约金的那条数据
		TurnOverDetails turnOverDetails2 = new TurnOverDetails();
		turnOverDetails2.setOrderId(details.getOrderId());
		turnOverDetails2.setMappingId(Integer.valueOf(details.getGoodsMappingId()));
		turnOverDetails2.setDetailsId(details.getId());
		turnOverDetails2.setType(2);
		turnOverDetails2.setAmount(details.getAppTotalAmount());
		turnOverDetails2.setUseBalance(details.getUseBalance());
		turnOverDetails2.setStatus(2);
		turnOverDetails2.setUserId(oLog.getMtmyUserId());
		turnOverDetails2.setCreateBy(UserUtils.getUser());
		turnOverDetailsService.saveTurnOverDetails(turnOverDetails2);
		
		OrderGoodsDetails newDetails = orderGoodsDetailsService.selectOrderBalance(oLog.getRecid());
		int integral = newDetails.getIntegral();        //处理完预约金以后，待付尾款为0的时候，处理预约金以后送的云币
		int userIntegral = 0;   //入库赠送的云币
		
		if(!"bm".equals(oLog.getChannelFlag()) && newDetails.getSumOrderArrearage() == 0){   //app或者wap处理预约，待付尾款为0的时候，送云币
			//当充值全部欠款以后，对用户进行送云币
			if(integral > 0){
				boolean str = redisClientTemplate.exists(MTMY_ID+oLog.getMtmyUserId());
				if(str){   //若缓存存在，则操作缓存
					RedisLock redisLock = new RedisLock(redisClientTemplate, MTMY_ID+oLog.getMtmyUserId());
					redisLock.lock();
					redisClientTemplate.incrBy(MTMY_ID+oLog.getMtmyUserId(),integral);
					redisLock.unlock();
				}else{         //若缓存不存在，则操作mtmy_user_accounts
					userIntegral = integral;            //赠送云币
				}
				
				//在mtmy_integrals_log表中插入日志
				IntegralsLog integralsLog = new IntegralsLog();
				integralsLog.setUserId(oLog.getMtmyUserId());
				integralsLog.setIntegralType(0);
				integralsLog.setIntegralSource(0);
				integralsLog.setActionType(21);
				integralsLog.setIntegral(integral);
				integralsLog.setOrderId(oLog.getOrderId());
				integralsLog.setRemark("商品赠送");
				ordersDao.insertIntegralLog(integralsLog);
			}
		}
		
		//对用户的账户进行操作
		Orders _orders = new Orders();//根据用户id查询用户账户信息
		_orders.setUserid(oLog.getMtmyUserId());
		Orders account = ordersDao.getAccount(_orders);
		if(account == null){
			Orders newAccount = new Orders();
			double accountBalance_ = newSpareMoneySum;	//账户余额信息
			newAccount.setAccountBalance(accountBalance_);
			newAccount.setUserid(_orders.getUserid());
			newAccount.setUserIntegral(userIntegral);                 //要赠送的云币
			ordersDao.insertAccount(newAccount);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId() ,oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}else{
			double accountBalance_ = 0;
			accountBalance_ = Double.parseDouble(formater.format(account.getAccountBalance()+newSpareMoneySum));
			account.setAccountBalance(accountBalance_);
			account.setUserIntegral(account.getUserIntegral() + userIntegral);   //要赠送的云币
			ordersDao.updateAccount(account);
			
			//插入用户账户充值记录表(属于订单)
			int type = 0;//类型是订单
			insertUserAccountsLog(oLog.getOrderId(), oLog.getMtmyUserId(), newSpareMoneySum, type, _orders.getChannelFlag(), user, oLog.getRecid()+"");
		}
		
		//若为老商品，则对店铺有补偿
		if(goodsType == 0){
			//若预约金大于0
			if(realAdvancePrice > 0){   
				//对登云账户进行操作
				if(detailsTotalAmount > 0){
					double claimMoney = 0.0;   //补偿金  补助不超过20
					if(detailsTotalAmount * 0.2 >= 20){
						claimMoney = detailsTotalAmount + 20;
					}else{
						claimMoney = detailsTotalAmount * 1.2;
					}
					OfficeAccountLog officeAccountLog = new OfficeAccountLog();
					User newUser = UserUtils.getUser();
					
					double amount = orderGoodsDetailsService.selectByOfficeId("1");   //登云美业公司账户的钱
					double afterAmount = Double.parseDouble(formater.format(amount - claimMoney));
					orderGoodsDetailsService.updateByOfficeId(afterAmount, "1");   //更新登云美业的登云账户金额
					
					//登云美业的登云账户减少钱时对日志进行操作
					officeAccountLog.setOrderId(oLog.getOrderId());
					officeAccountLog.setOfficeId("1");
					officeAccountLog.setType("1");
					officeAccountLog.setOfficeFrom("1");
					officeAccountLog.setAmount(claimMoney);
					officeAccountLog.setCreateBy(newUser);
					orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
					
					String shopId = "";
					if("".equals(officeId) || officeId == null){
						shopId = orderGoodsDetailsService.selectShopId(String.valueOf(oLog.getRecid())); //获取当前用户预约对应的店铺id
					}else{
						shopId = officeId;          //获取当前用户的归属店铺（机构）
					}
					if(orderGoodsDetailsService.selectShopByOfficeId(shopId) == 0){    //若登云账户中无该店铺的账户
						OfficeAccount officeAccount = new OfficeAccount();
						officeAccount.setAmount(claimMoney);
						officeAccount.setOfficeId(shopId);
						orderGoodsDetailsService.insertByOfficeId(officeAccount);
					}else{         
						double shopAmount = orderGoodsDetailsService.selectByOfficeId(shopId);   //登云账户中店铺的钱
						double afterShopAmount =  Double.parseDouble(formater.format(shopAmount + claimMoney));
						orderGoodsDetailsService.updateByOfficeId(afterShopAmount, shopId);
					}
					//店铺的登云账户减少钱时对日志进行操作
					officeAccountLog.setOrderId(oLog.getOrderId());
					officeAccountLog.setOfficeId(shopId);
					officeAccountLog.setType("0");
					officeAccountLog.setOfficeFrom("1");
					officeAccountLog.setAmount(claimMoney);
					officeAccountLog.setCreateBy(newUser);
					orderGoodsDetailsService.insertOfficeAccountLog(officeAccountLog);
				}
			}
		}
	}

	/**
	 * 判断修改物流之前是否存在物流信息
	 * @param orders
	 * @return
	 */
	public String getShippingcodeByid(Orders orders) {
		return ordersDao.getShippingcodeByid(orders);
	}

	/**
	 * 第一次修改物流信息时,修改订单状态
	 * @param orders
	 */
	public void updateOrdersStatus(Orders orders) {
		ordersDao.updateOrdersStatus(orders);
	}

	/**
	 * 查询提成人员日志记录
	 * @param page
	 * @param orderPushmoneyRecord
	 * @return
	 */
	public Page<OrderPushmoneyRecord> getOrderPushmoneyRecordList(Page<OrderPushmoneyRecord> page,
			OrderPushmoneyRecord orderPushmoneyRecord) {
		// 设置分页参数
		orderPushmoneyRecord.setPage(page);
		// 执行分页查询
		page.setList(orderPushmoneyRecordDao.findList(orderPushmoneyRecord));
		return page;
	}
	
	/**
	 * 插入用户账户充值记录表(type:订单)
	 * @param orderId 订单ID
	 * @param mtmyUserId 充值账户ID
	 * @param belongOfficeId 
	 * @param belongUserId 
	 * @param newSpareMoneySum 输入或支付的金额(支持负数)
	 * @param type 类型(0:订单,1:账户充值)
	 * @param channelFlag 渠道标示(wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理) 
	 * @param user 操作者
	 * @param recidList 
	 * 
	 */
	public void insertUserAccountsLog(String orderId, int mtmyUserId, double newSpareMoneySum, int type, String channelFlag, User user, String recid) {
		if(newSpareMoneySum != 0){
			UserAccountsLog userAccountsLog = new UserAccountsLog();
			userAccountsLog.setMtmyUserId(mtmyUserId);
			/*userAccountsLog.setBelongUserId(belongUserId);;
			userAccountsLog.setBelongOfficeId(belongOfficeId);*/
			userAccountsLog.setAmount(newSpareMoneySum);
			userAccountsLog.setType(type);
			if(newSpareMoneySum >= 0){//当金额大于等于0,表示账户有收入记录
				userAccountsLog.setStatus(0);
			}else{
				userAccountsLog.setStatus(1);
			}
			userAccountsLog.setRemarks("订单ID:"+orderId+",商品匹配ID:"+recid);
			userAccountsLog.setChannelFlag(channelFlag);
			userAccountsLog.setSourceFlag("mtmy");
			userAccountsLog.setCreateOfficeId(user.getOffice().getId());
			userAccountsLog.setCreateBy(user);
			userAccountsLogDao.insertUserAccountsLog(userAccountsLog);
		}
	}
	
}
