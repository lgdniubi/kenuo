package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CouponUserDao;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.ReturnGoodsDao;
import com.training.modules.ec.dao.TradingLogDao;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnGoods;
import com.training.modules.sys.utils.UserUtils;

/**
 * 退货订单处理service
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class ReturnGoodsService extends CrudService<ReturnGoodsDao,ReturnGoods>{
	
	@Autowired
	private ReturnGoodsDao returnGoodsDao;
	@Autowired
	private OrdersDao ordersDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private TradingLogDao tradingLogDao;
	@Autowired
	private CouponUserDao couponUserDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<ReturnGoods> findReturnList(Page<ReturnGoods> page, ReturnGoods returnGoods) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		returnGoods.getSqlMap().put("dsf", dataScopeFilter(returnGoods.getCurrentUser(), "o", "a"));
		// 设置分页参数
		returnGoods.setPage(page);
		// 执行分页查询
		page.setList(returnGoodsDao.findList(returnGoods));
		return page;
	}
	
	
	
	/**
	 * 保存数据
	 * @param returnGoods
	 */
	public void saveReturn(ReturnGoods returnGoods){
		
		Date date=new Date();
		SimpleDateFormat simd=new SimpleDateFormat("YYYYMMddHHmmssSSS");
		List<Orders> orderlist=ordersDao.orderlist(returnGoods.getOrderid());
		String str=simd.format(date);
		Orders orders=new Orders();
		orders.setOrderid(str);
		orders.setParentid(returnGoods.getOrderid());
		orders.setOrderamount(-returnGoods.getReturnmoney());
		orders.setOrderstatus(returnGoods.getOrderstatus());
		if(orderlist.size()>0){
			ordersDao.UpdateOrderReturn(orders);
			//ordersDao.UpdateOrderstatus(orders);
		}else{
			
				ordersDao.insertReturn(orders);
//				
//				TradingLog trading=tradingLogDao.findByOrderId(returnGoods.getOrderid());
//				if(trading!=null){
//					TradingLog tradingLog1=new TradingLog();
//					tradingLog1.setUserId(trading.getUserId());
//					tradingLog1.setStatus(1);
//					tradingLog1.setMoney(Math.abs(trading.getMoney()));
//					tradingLog1.setType("退货返还");
//					tradingLog1.setOrderId(returnGoods.getOrderid());
//					tradingLogDao.insert(tradingLog1);			//插入余额明细
//					Users users=new Users();
//					users.setUserid(trading.getUserId());
//					users.setUsermoney(Math.abs(trading.getMoney()));
//					mtmyUsersDao.UpdateUserMoney(users);			//返还金额
//				}
//				
//				InvitationUser invitationUser=invitationUserDao.findByOrderId(returnGoods.getOrderid());		//拿去用户的邀请码 是否为首单
//				if(invitationUser!=null){
//					if(invitationUser.getStatus()==1){
//						if(invitationUser.getType()==0){						//每天每夜扣除提成
//							Users users=new Users();
//							users.setUserid(Integer.parseInt(invitationUser.getInvitationCode()));
//							users.setUsermoney(-invitationUser.getPrice());
//							mtmyUsersDao.UpdateUserMoney(users);			//赠送邀请提成
//							TradingLog tradingLog=new TradingLog();
//							tradingLog.setUserId(Integer.parseInt(invitationUser.getInvitationCode()));
//							tradingLog.setStatus(2);
//							tradingLog.setMoney(-invitationUser.getPrice());
//							tradingLog.setType("提成扣除");
//							tradingLog.setOrderId(returnGoods.getOrderid());
//							tradingLogDao.insert(tradingLog);			//插入余额明细
//						}else{												//妃子校用户提成扣除
//							User user=new User();
//							user.setId(invitationUser.getInvitationCode());
//							user.setUsermoney(-invitationUser.getPrice());
//							userDao.UpdateUserMoney(user);							//扣除余额
//							TradingFZLog tradingFZLog=new TradingFZLog();
//							tradingFZLog.setUserId(invitationUser.getInvitationCode());
//							tradingFZLog.setStatus(2);
//							tradingFZLog.setMoney(-invitationUser.getPrice());
//							tradingFZLog.setType("提成扣除");
//							tradingFZLog.setOrderId(returnGoods.getOrderid());
//							tradingLogFZDao.insert(tradingFZLog);					//插入账户日志
//						}
//					
//					
//					}
//				}
				//返红包
//				CouponUser couponUser=couponUserDao.findByorderId(returnGoods.getOrderid());
//				if(couponUser!=null){
//					CouponUser copUser=new CouponUser();
//					copUser.setStatus("0");
//					copUser.setOrderId(returnGoods.getOrderid());
//					couponUserDao.UpdateStatus(copUser);
//					
//				}
				
//				IntegralLog integralLog1=integralLogDao.findByOrderIdAdd(returnGoods.getOrderid());
//				IntegralLog integralLog=integralLogDao.findByOrderId(returnGoods.getOrderid());
//				if(integralLog1!=null){
//					Users users=new Users();
//					users.setPaypoints(-Integer.parseInt(integralLog1.getValue()));
//					users.setUserid(integralLog1.getUserId());
//					mtmyUsersDao.UpdateUserPayPost(users);				//扣除
//					IntegralLog integralLog2=new IntegralLog();
//					integralLog2.setUserId(integralLog1.getUserId());
//					integralLog2.setType(1);
//					integralLog2.setOrderId(returnGoods.getOrderid());
//					integralLog2.setRemark("退货扣除");
//					integralLog2.setValue(-Integer.parseInt(integralLog1.getValue())+"");
//					integralLogDao.insert(integralLog2);					//插入积分明细
//				}
				
				
//				if(integralLog!=null){
//					Users users=new Users();
//					users.setPaypoints(Math.abs(Integer.parseInt(integralLog.getValue())));
//					users.setUserid(integralLog.getUserId());
//					mtmyUsersDao.UpdateUserPayPost(users);				//返还积分
//					IntegralLog integralLog2=new IntegralLog();
//					integralLog2.setUserId(integralLog.getUserId());
//					integralLog2.setType(0);
//					integralLog2.setOrderId(returnGoods.getOrderid());
//					integralLog2.setRemark("退货返还");
//					integralLog2.setValue(Math.abs(Integer.parseInt(integralLog.getValue()))+"");
//					integralLogDao.insert(integralLog2);					//插入积分明细
//				}
				
			ordersDao.UpdateOrderstatus(orders);

		}
		List<ReturnGoods> retlist=returnGoodsDao.retlist(returnGoods.getOrderid());
		if(retlist.size()>0){
			returnGoodsDao.updateReturn(returnGoods);
		}else {
			
				returnGoods.setEntryoperator(UserUtils.getUser().getName());
				returnGoods.setReturnstatus(1);
				returnGoodsDao.insertReturn(returnGoods);
		
			
		}
		
	
	}
	
	
	/**
	 * 确认退款
	 * @param returnGoods
	 */
	public void updateReturnMomeny(ReturnGoods returnGoods){
		returnGoods.setReturnstatus(2);
		returnGoods.setFinancialoperator(UserUtils.getUser().getName());
		returnGoodsDao.updateReturnMomeny(returnGoods);
		Orders orders=new Orders();
		orders.setOrderid(returnGoods.getOrderid());
		orders.setParentid(returnGoods.getOrderid());
		orders.setOrderstatus(3);
		ordersDao.UpdateOrderstatus(orders);
		ordersDao.UpdateOrderReturnStatus(orders);
		
	}

}
