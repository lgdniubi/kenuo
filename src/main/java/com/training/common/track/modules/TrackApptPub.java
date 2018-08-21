/**
 * 项目名称：	kenuo
 * 类名称：  	TrackApptPub
 * 类描述：
 * 创建人：	kele
 * 创建时间：    2018年7月14日 下午1:32:34
 * 修改人：  	idata
 * 修改时间：    2018年7月14日 下午1:32:34
 * 修改备注：
 * @Version
 */
package com.training.common.track.modules;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.training.common.track.config.TrackConfig;
import com.training.common.track.utils.TrackUtils;
import com.training.common.utils.BeanUtil;
import com.training.modules.track.entity.TApptOrder;
import com.training.modules.track.service.TApptOrderService;

/**
 * 类名称：  	TrackApptPub
 * 类描述：	每天美耶-预约相关-埋点核心业务
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午1:32:34
 * 
 */
public class TrackApptPub {
	
	private static Log log = LogFactory.getLog(TrackApptPub.class);
	// 预约Service层
	private static TApptOrderService tApptOrderService = (TApptOrderService) BeanUtil.getBean("TApptOrderService");

	private Map<String, Object> paramMap;
	
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * 方法说明：	消耗业绩统计
	 * 创建时间：	2018年7月14日 下午1:45:36
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午1:45:36
	 */
	public void deplete_achievement() {
		try {
			log.info("[埋点-消耗业绩统计 { deplete_achievement } 入参：]"+this.paramMap.toString());
			// 订单ID
			String apptId = String.valueOf(this.paramMap.get("APPT_ID"));
			
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("apptId", apptId);
		    TApptOrder tApptOrder = tApptOrderService.queryApptOrderDetail(map);

		    // 登录用用户ID
 			String distinctId = TrackUtils.convertStr(tApptOrder.getUserId());
 			this.paramMap.put("DISTINCT_ID", distinctId);
			
			// 使用 DebugConsumer 初始化 SensorsAnalytics
			final SensorsAnalytics sa = TrackConfig.getMtmySensorsAnalytics(this.paramMap);
			
			// 预约项目类型
			String depleteGoodsType = "";
			Integer serviceTimes = TrackUtils.convertInteger(tApptOrder.getServiceTimes());
			if(serviceTimes == 999) {
				depleteGoodsType = "时限卡";
			}else {
				depleteGoodsType = "次数卡";
			}
			
			// 年卡不算业绩
			if(serviceTimes != 999) {
				Map<String, Object> properties = new HashMap<String, Object>();
				// 预约id
			    properties.put("appoint_id", apptId);
			    // 预约时间
			    properties.put("appoint_time", TrackUtils.convertStr(tApptOrder.getApptDate()));
			    // 预约商品id
			    properties.put("goods_id", TrackUtils.convertStr(tApptOrder.getGoodsId()));
			    // 预约商品名称
			    properties.put("goods_name", TrackUtils.convertStr(tApptOrder.getGoodsName()));
			    // 美容师id
			    properties.put("beautician_id", TrackUtils.convertStr(tApptOrder.getBeauticianId()));
			    // 预约美容师名称
			    properties.put("beautician_name", TrackUtils.convertStr(tApptOrder.getBeauticianName()));
			    // 美容师店铺id
			    properties.put("store_id", TrackUtils.convertStr(tApptOrder.getShopId()));
			    // 美容师所属店铺名称
			    properties.put("store_name", TrackUtils.convertStr(tApptOrder.getShopName()));
			    // 预约消耗业绩
			    properties.put("deplete_paymony", TrackUtils.convertDouble(tApptOrder.getDepletePayMoney()));
			    // 服务费
			    properties.put("service_charge", TrackUtils.convertDouble(tApptOrder.getServiceCharge()));
			    // 预约项目类型
			    properties.put("deplete_goods_type", depleteGoodsType);
			    
			    sa.track(distinctId, true, "deplete_achievement", properties);
			}
			
			// 程序结束前，停止 Sensors Analytics SDK 所有服务
			sa.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-消耗业绩统计[deplete_achievement],出现异常，信息为："+e.getMessage()); 
		}
	}
	
	/**
	 * 方法说明：	统计预约已完成的预约
	 * 创建时间：	2018年8月21日10:23:22
	 * 创建人：	xiaoye
	 */
	public void submit_appoint_success() {
		try {
			log.info("[埋点-消耗业绩统计 { submit_appoint_success } 入参：]"+this.paramMap.toString());
			// 预约ID
			String apptId = String.valueOf(this.paramMap.get("APPT_ID"));
			
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("apptId", apptId);
		    TApptOrder tApptOrder = tApptOrderService.queryApptDetail(map);

		    // 登录用用户ID
 			String distinctId = TrackUtils.convertStr(tApptOrder.getUserId());
 			this.paramMap.put("DISTINCT_ID", distinctId);
			
			// 使用 DebugConsumer 初始化 SensorsAnalytics
			final SensorsAnalytics sa = TrackConfig.getMtmySensorsAnalytics(this.paramMap);
			
			Map<String, Object> properties = new HashMap<String, Object>();
			// 预约id
		    properties.put("appoint_id", apptId);
		    // 预约商品id
		    properties.put("goods_id", TrackUtils.convertStr(tApptOrder.getGoodsId()));
		    //预约商品是否是实物虚拟
		    properties.put("goods_isReal", TrackUtils.convertStr(tApptOrder.getIsReal()));
		    // 预约商品名称
		    properties.put("goods_name", TrackUtils.convertStr(tApptOrder.getGoodsName()));
		    // 美容师店铺id
		    properties.put("store_id", TrackUtils.convertStr(tApptOrder.getShopId()));
		    // 美容师所属店铺名称
		    properties.put("store_name", TrackUtils.convertStr(tApptOrder.getShopName()));
		    // 美容师id
		    properties.put("beautician_id", TrackUtils.convertStr(tApptOrder.getBeauticianId()));
		    // 预约美容师名称
		    properties.put("beautician_name", TrackUtils.convertStr(tApptOrder.getBeauticianName()));
		    // 预约时间
		    properties.put("appoint_time", TrackUtils.convertStr(tApptOrder.getApptDate()));
		    
		    sa.track(distinctId, true, "submit_appoint_success", properties);
			
			// 程序结束前，停止 Sensors Analytics SDK 所有服务
			sa.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-统计预约已完成的预约[submit_appoint_success],出现异常，信息为："+e.getMessage()); 
		}
	}
}
