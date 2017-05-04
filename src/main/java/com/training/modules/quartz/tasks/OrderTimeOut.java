package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.utils.ParametersFactory;

/**
 * 过期订单设置为取消订单状态
 * @author kele
 *
 */
@Component
public class OrderTimeOut extends CommonService{
	
	public static final String LOCK_SPECPRICE_PREFIX = "LOCK_SPECPRICE_";//规格库存锁前缀
	private Logger logger = Logger.getLogger(OrderTimeOut.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static OrdersService ordersService;
	private static OrderGoodsService orderGoodsService;
	/*
	private static MtmyUsersService mtmyUsersService;
	private static MtmyRuleParamService mtmyRuleParamService;*/
	static{
		ordersService = (OrdersService) BeanUtil.getBean("ordersService");
		orderGoodsService = (OrderGoodsService) BeanUtil.getBean("orderGoodsService");
		/*mtmyUsersService = (MtmyUsersService) BeanUtil.getBean("mtmyUsersService");
		mtmyRuleParamService = (MtmyRuleParamService) BeanUtil.getBean("mtmyRuleParamService");*/
	}
	
	/**
	 * 普通订单过期定时任务
	 */
	public void orderTimeOut(){
		logger.info("[work0],start,过期订单设置,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("orderTimeOut");
		taskLog.setStartDate(startDate);
		
		String now_order = null;//当前订单号
		
		try {
			int timeout = Integer.parseInt(ParametersFactory.getMtmyParamValues("order_timeout"));
			
			logger.info("[过期普通订单]，订单过期时间为："+timeout);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("minute", timeout);
			map.put("order_type", 0);//普通订单
			List<Orders> list = ordersService.queryNotPayOrder(map);
			
			logger.info("[过期普通订单]，扫面过期订单开始，订单个数："+list.size());
			for(Orders vo : list){
				now_order = vo.getOrderid();//设置当天订单号，若出现异常，在日志中查询
				
				logger.info("[过期普通订单]，订单id："+vo.getOrderid());
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("order_status", -2);
				m.put("cancel_type", 2);
				m.put("order_id", vo.getOrderid());
				//将过期的订单置为已作废
				ordersService.modifyOrderStatus(m);
				List<OrderGoods> vs = orderGoodsService.orderlist(vo.getOrderid());
				//归还订单商品的库存
				logger.info("[过期普通订单]，归还商品数量："+vs.size());
				for(OrderGoods v : vs){
					logger.info("[过期普通订单]，归还商品id："+v.getGoodsid());
					RedisLock lo = new RedisLock(redisClientTemplate, LOCK_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
					lo.lock();
					boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
					if(str){
						redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+v.getGoodsid(),v.getGoodsnum());
						redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey(),v.getGoodsnum());
					}
					lo.unlock();
					
					/*logger.info("归还用户限购资源,活动id："+v.getActionid()+",用户id："+v.getUserid()+",商品id："+v.getGoodsid()+",数量："+v.getGoodsnum());
					if(v.getActiontype() ==1){ //归还用户限购资源
						redisClientTemplate.hincrBy(RedisConfig.buying_limit_prefix+v.getActionid(), v.getUserid()+"_"+v.getGoodsid(), -v.getGoodsnum());
					}*/
					
				}
				//归还优惠券
				/*logger.info("归还优惠券，价格:"+vo.getCouponprice()+",订单："+vo.getOrderid());
				if(vo.getCouponprice() > 0){
					CouponUser u = new CouponUser();
					u.setOrderId(vo.getOrderid());
					u.setStatus("0");
					couponUserDao.UpdateStatus(u);
				}*/
				
			}
			
			taskLog.setJobDescription("[过期普通订单]，订单过期时间为："+timeout+"   扫面过期订单开始，订单个数："+list.size());
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务orderTimeOut】过期订单,当前订单号["+now_order+"]出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,过期订单设置,结束时间："+df.format(new Date()));
	}
	/**
	 * 抢购订单定时任务
	 */
	public void catchOrderTimeOut(){
		logger.info("[过期抢购订单],start,过期订单设置,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("catchOrderTimeOut");
		taskLog.setStartDate(startDate);
		
		String now_order = null;//当前订单号
		try {
			int timeout = Integer.parseInt(ParametersFactory.getMtmyParamValues("catchOrder_timeout"));
			
			logger.info("[过期抢购订单]，订单过期时间为："+timeout);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("minute", timeout);
			map.put("order_type", 1);//抢购订单
			List<Orders> list = ordersService.queryNotPayOrder(map);
			
			logger.info("[过期抢购订单]，扫面过期订单开始，订单个数："+list.size());
			for(Orders vo : list){
				now_order = vo.getOrderid();
				
				logger.info("[过期抢购订单]，订单id："+vo.getOrderid());
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("order_status", -2);
				m.put("cancel_type", 2);
				m.put("order_id", vo.getOrderid());
				//将过期的订单置为已作废
				ordersService.modifyOrderStatus(m);
				List<OrderGoods> vs = orderGoodsService.orderlist(vo.getOrderid());
				//归还订单商品的库存
				logger.info("[过期抢购订单]，归还商品数量："+vs.size());
				for(OrderGoods v : vs){
					logger.info("[过期抢购订单]，归还商品id："+v.getGoodsid());
					RedisLock lo = new RedisLock(redisClientTemplate, LOCK_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
					lo.lock();
					boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
					if(str){
						redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+v.getGoodsid(),v.getGoodsnum());
						redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey(),v.getGoodsnum());
					}
					lo.unlock();
					
					logger.info("归还用户限购资源,活动id："+v.getActionid()+",用户id："+v.getUserid()+",商品id："+v.getGoodsid()+",数量："+v.getGoodsnum());
					if(v.getActiontype() ==1){ //归还用户限购资源
						redisClientTemplate.hincrBy(RedisConfig.buying_limit_prefix+v.getActionid(), v.getUserid()+"_"+v.getGoodsid(), -v.getGoodsnum());
					}
				}
			}
			taskLog.setJobDescription("[过期抢购订单]，订单过期时间为："+timeout+"   扫面过期订单开始，订单个数："+list.size());
			taskLog.setStatus(0);//任务状态
		} catch (Exception e) {
			logger.error("#####【定时任务catchOrderTimeOut】过期订单,当前订单号["+now_order+"]出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		logger.info("[过期抢购订单],end,过期订单设置,结束时间："+df.format(new Date()));
	}
	
	
	
	/**
	 * 后台调用
	 * @param vs
	 */
	public void oto(List<OrderGoods> vs){
		if(vs != null && vs.size()>0){
			for(OrderGoods v : vs){
				RedisLock lo = new RedisLock(redisClientTemplate, LOCK_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
				lo.lock();
				boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey());
				if(str){
					redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+v.getGoodsid(),v.getGoodsnum());
					redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+v.getGoodsid()+"#"+v.getSpeckey(),v.getGoodsnum());
				}
				lo.unlock();
				logger.info("归还用户限购资源,活动id："+v.getActionid()+",用户id："+v.getUserid()+",商品id："+v.getGoodsid()+",数量："+v.getGoodsnum());
				if(v.getActiontype() ==1){ //归还用户限购资源
					redisClientTemplate.hincrBy(RedisConfig.buying_limit_prefix+v.getActionid(), v.getUserid()+"_"+v.getGoodsid(), -v.getGoodsnum());
				}
				
			}
		}
	}
}
