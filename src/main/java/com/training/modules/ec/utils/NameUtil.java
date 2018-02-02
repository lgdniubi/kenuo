package com.training.modules.ec.utils;

import net.sf.json.JSONObject;

/**
 * 类型或者状态名称工具类（0：订单状态:1：支付方式；2：预约状态；3：发货类型）
 * @author xiaoye  2018年1月23日
 *
 */
public class NameUtil {
	
	public static String typeNames(String type,String str){
		String result = "";
		if(!"".equals(type) && type != null && !"".equals(str) && str != null){
			JSONObject jsonO = new JSONObject();
			if("0".equals(type)){
				jsonO = JSONObject.fromObject("{-2:'取消订单',-1:'待付款',1:'待发货',2:'待收货',3:'已退款',4:'已完成'}");
			}else if("1".equals(type)){
				jsonO = JSONObject.fromObject("{wx:'微信支付',alipay:'支付宝App支付',upacp_wap:'银联',wx_pub:'微信公众号',wx_pub_qr:'微信公众号扫码',alipay_wap:'支付宝手机网页',alipay_qr:'支付宝扫码支付',money:'现金支付',yeepay_wap:'易宝手机网页支付'}");
			}else if("2".equals(type)){
				jsonO = JSONObject.fromObject("{0:'等待服务',1:'已完成',2:'已评价',3:'已取消',4:'客户爽约'}");
			}else if("3".equals(type)){
				jsonO = JSONObject.fromObject("{0:'快递发货',1:'到店自取',2:'无需发货'}");
			}else{
				result = "无";
			}
			
			if(jsonO.get(str) != null){
				result = jsonO.get(str).toString();
			}else{
				result = "无";
			}
		}
		return result;
	}
}
