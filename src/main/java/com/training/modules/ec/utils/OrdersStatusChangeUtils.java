package com.training.modules.ec.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.mapper.JsonMapper;
import com.training.common.utils.BeanUtil;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.utils.igtpush.GetTUtil;
import com.training.modules.ec.utils.igtpush.exception.ResponseError;
import com.training.modules.ec.utils.igtpush.exception.SysConstants;

import net.sf.json.JSONObject;

/**
 * 当订单状态发生改变，变成已发货时给用户推送消息
 * @author 
 *
 */
public class OrdersStatusChangeUtils {
	
	@Autowired
	private static OrdersService ordersService;
	
	static{
		ordersService = (OrdersService) BeanUtil.getBean("ordersService");
	}
	
	public static void pushMsg(@RequestParam Orders orders,@RequestParam Integer push_type){
		Map<String, Object> map = null;
		try {
			map = (Map) JsonMapper.fromJsonString(newPushMsg(orders, push_type), Map.class);
			System.out.println(map.get("result"));
		} catch (ResponseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public static String newPushMsg(Orders orders,Integer push_type) throws ResponseError{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		
		List<OrderGoods> orderGoodsList = ordersService.selectOrdersToUser(orders);
		OrderGoods orderGoods = orderGoodsList.get(0);
		m.put("orderStatusName","订单已发货");
		m.put("logisticsId",ordersService.get(orders.getOrderid()).getShippingcode());
		m.put("orderId",orders.getOrderid());
		m.put("goodsPic",orderGoods.getOriginalimg());
		m.put("goodsName",orderGoods.getGoodsname());
		m.put("goodsNum",orderGoods.getGoodsnum());
		
		map.put("content", m);
		map.put("push_type", push_type);
		
		if(push_type == 3){
			map.put("cid_list", ordersService.selectCidByUserId(orders));
			return push(JsonMapper.toJsonString(map));
		}else{
			//推送类型有误
			JSONObject json = new JSONObject();
			json.put("result", SysConstants.ERROR_CONTENT_MESSAGE);
			json.put("message", SysConstants.ERROR_CONTENT_RESULT);
			return json.toString();
		}
	}
	
	/**
	 * 调用推送接口
	 * @param jsonObj
	 * @throws ResponseError 
	 */
	private static String push(String jsonObj) throws ResponseError{
		JSONObject jsStr = JSONObject.fromObject(jsonObj); 
		String resutl = GetTUtil.list(jsStr);
		return resutl;
	}
	
}
