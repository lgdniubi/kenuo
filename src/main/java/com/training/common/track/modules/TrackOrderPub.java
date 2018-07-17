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
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.training.common.track.config.TrackConfig;
import com.training.common.track.utils.TrackUtils;
import com.training.common.utils.BeanUtil;
import com.training.modules.track.entity.TOffice;
import com.training.modules.track.entity.TOrder;
import com.training.modules.track.entity.TOrderGoods;
import com.training.modules.track.entity.TOrderGoodsRecharge;
import com.training.modules.track.service.TOrderGoodsService;

/**
 * 类名称：  	TrackApptPub
 * 类描述：	每天美耶-订单相关-埋点核心业务
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午1:32:34
 * 
 */
public class TrackOrderPub {

	private static Log log = LogFactory.getLog(TrackOrderPub.class);
	
	// 订单Service层
	private static TOrderGoodsService tOrderGoodsService = (TOrderGoodsService) BeanUtil.getBean("TOrderGoodsService");

	private Map<String, Object> paramMap;
	
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * 方法说明:	支付订单
	 * 创建时间:	2018年1月17日 下午3:35:35
	 * 创建人:	kele
	 * 修改记录:	修改人	修改记录	2018年1月17日 下午3:35:35
	 */
	public void pay_order() {
	    try {
	    	log.info("[埋点-支付订单 { pay_order } 入参：]"+this.paramMap.toString());
	    	
	    	// 订单号
		    String orderId = (String) this.paramMap.get("ORDER_ID");
		    Map<String, Object> orderMap = new HashMap<String, Object>();
		    orderMap.put("orderId", orderId);
		    TOrder tOrder = tOrderGoodsService.queryTOrder(orderMap);
	    	
		    this.paramMap.put("DISTINCT_ID", tOrder.getUserId());
		    this.paramMap.put("CLIENT", tOrder.getChannelFlag());
	    	// 使用 DebugConsumer 初始化 SensorsAnalytics
		    final SensorsAnalytics sa = TrackConfig.getMtmySensorsAnalytics(this.paramMap);
		    
		    // 用户的 Distinct Id	(登录用用户ID，非登录用户用设备ID)
		    String distinctId = String.valueOf(tOrder.getUserId());
		    String couponIds = tOrder.getCouponIds();
		    if(null == couponIds || "null".equals(couponIds)){
		    	couponIds = "";
		    }
		    String couponNames = tOrder.getCouponNames();
		    if(null == couponNames || "null".equals(couponNames)){
		    	couponNames = "";
		    }
		    
		    // 用户购买成功过的订单数量
		    Map<String, Object> userOrderMap = new HashMap<String, Object>();
	    	userOrderMap.put("userId", tOrder.getUserId());
		    int userOrderNum = tOrderGoodsService.queryUserOrderNum(userOrderMap);
		    
		    // 是否实物（0：实物商品；1：虚拟商品）
		    int isReal = tOrder.getIsReal();
		    
		    // 是否新订单（0：新订单；1：老订单）
		    String orderType = "";
		    Integer isNeworder = tOrder.getIsNeworder();
		    if("0".equals(TrackUtils.convertStr(isNeworder))) {
		    	orderType = "新订单";
		    }else if("1".equals(TrackUtils.convertStr(isNeworder))) {
		    	orderType = "老订单";
		    }
	    	
		    /* 支付订单 */
		    Map<String, Object> properties = new HashMap<String, Object>();
		    // 订单ID
		    properties.put("order_id", tOrder.getOrderId());
		    // 订单金额
		    properties.put("order_total_price_double", tOrder.getOrderAmount());
		    // 实付价格
		    properties.put("order_price_double", tOrder.getTotalAmount());
		    // 支付方式
		    properties.put("order_pay_method", String.valueOf(tOrder.getPayName()));
		    // 是否使用优惠券
		    properties.put("is_useCoupon", tOrder.getCouponPrice() > 0 ?true:false);
		    // 优惠金额
		    properties.put("coupon_price_double", tOrder.getCouponPrice());
		    // 优惠券个数
//		    properties.put("coupon_num_int", 0);
		    // 是否实物虚拟订单
		    properties.put("goods_isReal", String.valueOf(isReal));
		    // 运费
		    properties.put("order_shipping_price_double", tOrder.getShippingPrice());
		    // 支付状态(成功，失败)
		    properties.put("order_pay_status", "成功");
		    // 失败原因
//		    properties.put("order_pay_errorMsg", "");
		    // 面值Ids
		    properties.put("coupon_ids", String.valueOf(couponIds));
		    // 面值Names
		    properties.put("coupon_names", String.valueOf(couponNames));
		    
		    /*二期新增属性，时间：2018年7月12日*/
		    // 是否首次 (bool)
		    properties.put("is_first_order", userOrderNum == 1?true:false);
		    // 订单类型
		    properties.put("order_type", orderType);
		    
		    // 记录用户注册事件
		    sa.track(distinctId, true, "pay_order", properties);
		    
		    /* 支付订单详情 */
		    Map<String, Object> orderGoodsMap = new HashMap<String, Object>();
		    orderGoodsMap.put("orderId", orderId);
		    List<TOrderGoods> tOrderGoodsList = tOrderGoodsService.queryOrderGoodsList(orderGoodsMap);
		    if(null != tOrderGoodsList && tOrderGoodsList.size() > 0) {
		    	for (int i = 0; i < tOrderGoodsList.size(); i++) {
		    		TOrderGoods tOrderGoods = tOrderGoodsList.get(i);
			    	Map<String, Object> orderGoodsParam = new HashMap<String, Object>();
			    	
			    	// 订单ID
			    	orderGoodsParam.put("order_id", orderId);
			    	// 商品ID
			    	orderGoodsParam.put("goods_id", String.valueOf(tOrderGoods.getGoodsId()));
			    	// 商品名称
			    	orderGoodsParam.put("goods_name", tOrderGoods.getGoodsName());
			    	// 商品规格id
			    	orderGoodsParam.put("goods_spec_id", String.valueOf(tOrderGoods.getSpecKey()));
			    	// 商品规格名称
			    	orderGoodsParam.put("goods_spec_name", tOrderGoods.getSpecKeyName());
			    	// 商品类别
			    	orderGoodsParam.put("goods_actiontype", String.valueOf(tOrderGoods.getActionType()));
			    	// 是否实物虚拟
			    	orderGoodsParam.put("goods_isReal", String.valueOf(tOrderGoods.getIsReal()));
			    	// 商品单价
			    	orderGoodsParam.put("goods_price_double", tOrderGoods.getGoodsPrice());
			    	// 商品数量
			    	orderGoodsParam.put("goods_num_int", tOrderGoods.getGoodsNum());
			    	// 满减金额
			    	orderGoodsParam.put("coupon_full_price_double", tOrderGoods.getCouponPrice());
			    	// 商品总价
			    	orderGoodsParam.put("goods_total_price_double", tOrderGoods.getOrderAmount());
			    	// 商品预约金
			    	orderGoodsParam.put("goods_advance_price_double", tOrderGoods.getAdvancePrice());
			    	// 一级分类名称
			    	orderGoodsParam.put("category_name_one", tOrderGoods.getCategoryNameOne());
			    	// 二级分类名称
			    	orderGoodsParam.put("category_name_two", tOrderGoods.getCategoryNameTwo());
			    	
			    	/*二期新增属性，时间：2018年7月12日*/
			    	// 商品商家id
			    	orderGoodsParam.put("goods_franchisee_id", TrackUtils.convertStr(tOrderGoods.getFranchiseeId()));
				    // 商品所属商家名称
			    	orderGoodsParam.put("goods_franchisee_name", TrackUtils.convertStr(tOrderGoods.getFranchiseeName()));
			    	// 商品品牌id
			    	orderGoodsParam.put("goods_brand_id", TrackUtils.convertStr(tOrderGoods.getGoodsBrandId()));
			    	// 商品品牌名称
			    	orderGoodsParam.put("goods_brand_name", TrackUtils.convertStr(tOrderGoods.getGoodsBrandName()));
			    	// 订单类型
			    	orderGoodsParam.put("order_type", orderType);
			    	
			    	// 提交订单详情
				    sa.track(distinctId, true, "pay_order_detail", orderGoodsParam);
		    	}
		    }

			// 程序结束前，停止 Sensors Analytics SDK 所有服务
		    sa.shutdown();
		    
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-支付订单[pay_order],出现异常，信息为："+e.getMessage());
		}
	}
	
	
	/**
	 * 方法说明：	充值信息
	 * 创建时间：	2018年7月14日 下午1:45:36
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午1:45:36
	 */
	public void order_recharge() {
		try {
			log.info("[埋点-充值信息 { order_recharge } 入参：]"+this.paramMap.toString());
			// 订单ID
			String orderId = String.valueOf(this.paramMap.get("ORDER_ID"));
			// 订单商品ID
			Integer recId = TrackUtils.convertInteger(this.paramMap.get("REC_ID"));
			
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("orderId", orderId);
		    map.put("recId", recId);
		    TOrderGoodsRecharge tOrderGoodsRecharge = tOrderGoodsService.queryOrderDetail(map);
			
		    // 登录用用户ID
 			String distinctId = TrackUtils.convertStr(tOrderGoodsRecharge.getUserId());
 			this.paramMap.put("DISTINCT_ID", distinctId);
			
			// 使用 DebugConsumer 初始化 SensorsAnalytics
			final SensorsAnalytics sa = TrackConfig.getMtmySensorsAnalytics(this.paramMap);
			
			// 当前充值金额
			double orderRechargeNow = TrackUtils.convertDouble(this.paramMap.get("ORDER_RECHARGE_NOW"));
			// 店铺ID
			String belongOfficeId = TrackUtils.convertStr(this.paramMap.get("BELONG_OFFICE_ID"));
			// 店铺名称
			TOffice tOffice = TrackUtils.getOffcieDetail(belongOfficeId);
			String belongOfficeName = TrackUtils.convertStr(tOffice.getOfficeName());
		    
			Map<String, Object> properties = new HashMap<String, Object>();
			// 订单id
		    properties.put("order_id", orderId);
		    // 商品预约金价格
		    properties.put("goods_advance_price_double", tOrderGoodsRecharge.getGoodsAdvancePrice());
		    // 商品总价
		    properties.put("goods_price_double", tOrderGoodsRecharge.getGoodsPrice());
		    // 本次充值金额
		    properties.put("order_recharge_now", orderRechargeNow);
		    // 总付款金额
		    properties.put("order_total_recharge_now", tOrderGoodsRecharge.getOrderTotalRecharge());
		    // 商品欠款
		    properties.put("goods_arrears", tOrderGoodsRecharge.getGoodsArrears());
		    // 商品余额
		    properties.put("order_unconsumed", tOrderGoodsRecharge.getOrderUnconsumed());
		    // 归属商店id
		    properties.put("store_id", belongOfficeId);
		    // 归属店铺ID
		    properties.put("store_name", belongOfficeName);
		    
		    sa.track(distinctId, true, "order_recharge", properties);
		    
		    // 程序结束前，停止 Sensors Analytics SDK 所有服务
		    sa.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("埋点-订单-充值信息[order_recharge],出现异常，信息为："+e.getMessage()); 
		}
	}
}
