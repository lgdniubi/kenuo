/**
 * 项目名称：	kenuo
 * 类名称：  	TrackCore
 * 类描述：
 * 创建人：	kele
 * 创建时间：    2018年7月14日 下午4:15:42
 * 修改人：  	idata
 * 修改时间：    2018年7月14日 下午4:15:42
 * 修改备注：
 * @Version
 */
package com.training.modules.track.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.common.config.Global;
import com.training.common.track.thread.TrackThread;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.sys.entity.User;


/**
 * 类名称：  	TrackCore
 * 类描述：	埋点-业务-核心代码
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午4:15:42
 */
public class TrackCore {

	private static Log log = LogFactory.getLog(TrackCore.class);
	
	/**
	 * 方法说明：	同步每天美耶用户埋点方法
	 * 创建时间：	2018年4月28日 上午11:01:18
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年4月28日 上午11:01:18
	 * @param user
	 */
	public static void trackSyncMtmyUser(User user) {
		/*##########[神策埋点{sign_up}-Begin]##########*/
		if(null != user && 0 != user.getMtmyUserId()) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 方法名称-注册
			paramMap.put("METHOD_NAME", "sign_up");
			// 用户ID
			paramMap.put("DISTINCT_ID", user.getMtmyUserId());
			// 匿名ID
			paramMap.put("ANONYMOUS_ID", "");
			// 来源类型
			paramMap.put("SOURCE_TYPE", user.getSourceType());
			// 来源类型名称
			paramMap.put("ACTION_SOURCE", user.getActionSource());
			
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(paramMap));
		}
		/*##########[神策埋点end]##########*/
	}
	
	/**
	 * 方法说明：	fzx注册埋点事件
	 * 创建时间：	2018年7月11日 上午10:13:50
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月11日 上午10:13:50
	 * @param user
	 */
	public static void trackSyncFzxUser(User user) {
		/*##########[神策埋点{sign_up}-Begin]##########*/
		if(null != user && !"".equals(user.getId())) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 方法名称-注册
			paramMap.put("METHOD_NAME", "sign_up_fzx");
			// 用户ID
			paramMap.put("DISTINCT_ID", user.getId());
			// 来源类型
			paramMap.put("SOURCE_TYPE", user.getSourceType());
			// 来源类型名称
			paramMap.put("ACTION_SOURCE", user.getActionSource());
			
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(paramMap));
		}
		/*##########[神策埋点end]##########*/
	}
	
	/**
	 * 方法说明：	订单充值
	 * 创建时间：	2018年7月14日 下午4:18:10
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午4:18:10
	 */
	public static void orderRecharge(OrderGoodsDetails details) {
		try {
			
			Map<String, Object> trackParamsMap = new HashMap<String, Object>();
			// 方法名称-提交一键预约
			trackParamsMap.put("METHOD_NAME", "order_recharge");
			// 订单ID
			trackParamsMap.put("ORDER_ID", details.getOrderId());
			// 订单商品ID
			trackParamsMap.put("REC_ID", details.getGoodsMappingId());
			// 用户充值金额
			trackParamsMap.put("ORDER_RECHARGE_NOW", details.getAppTotalAmount());
			// 归属机构ID
			trackParamsMap.put("BELONG_OFFICE_ID", details.getBelongOfficeId());
			
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(trackParamsMap));
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-订单-充值信息[orderRecharge],出现异常，信息为："+e.getMessage()); 
		}
	}
	
	/**
	 * 方法说明：	消耗业绩统计
	 * 创建时间：	2018年7月14日 下午5:33:22
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午5:33:22
	 * @param apptId 预约ID
	 * @param status 状态
	 */
	public static void depleteAchievement(Integer apptId) {
		try {
			Map<String, Object> trackParamsMap = new HashMap<String, Object>();
			// 方法名称-提交一键预约
			trackParamsMap.put("METHOD_NAME", "deplete_achievement");
			// 预约ID
			trackParamsMap.put("APPT_ID", apptId);
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(trackParamsMap));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-预约-消耗业绩统计[depleteAchievement],出现异常，信息为："+e.getMessage()); 
		}
	}
	
	/**
	 * 方法说明：	支付订单
	 * 创建时间：	2018年7月14日 下午5:50:24
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午5:50:24
	 * @param orderId
	 */
	public static void payOrder(String orderId) {
		try {
			Map<String, Object> trackParamsMap = new HashMap<String, Object>();
			// 方法名称-提交一键预约
			trackParamsMap.put("METHOD_NAME", "pay_order");
			// 预约ID
			trackParamsMap.put("ORDER_ID", orderId);
			// 异步线程执行方法
			Global.newFixed.execute(new TrackThread(trackParamsMap));
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-订单-支付订单[payOrder],出现异常，信息为："+e.getMessage()); 
		}
	}
}
