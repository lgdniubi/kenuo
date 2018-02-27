package com.training.modules.ec.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.modules.ec.dao.OrdersLogDao;
import com.training.modules.ec.dao.ReservationDao;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.OrdersLog;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.ReservationLog;
import com.training.modules.ec.utils.NameUtil;
import com.training.modules.ec.utils.SaveLogUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONObject;

/**
 * 保存操作日志dao
 * @author coffee
 * 2018下午8:21:42
 */

@Service
@Transactional(readOnly = false)
public class SaveLogService {
	
	@Autowired
	private OrdersLogDao ordersLogDao;
	@Autowired
	private ReservationDao reservationDao;
	
	/**
	 * 订单操作日志           注：6.7是咖啡非得让加，区分开，没办法，一个物流的日志分为四块。。。。。。。。。。。
	 * @param type  1:修改订单  2:修改物流  3:确认收货  4:用户取货  5：导入物流  6：非第一次修改物流  7：非第一次导入物流  8：强制取消
	 * @param oldOrders
	 * @param orders
	 * @param request
	 */
	public void ordersLog(HttpServletRequest request,String title,List<Object> obj){
			Orders oldOrders = (Orders)obj.get(0);	// 修改前对象
			Orders orders = (Orders)obj.get(1);		// 修改后对象

			User user = UserUtils.getUser();
			StringBuffer str = new StringBuffer();	// 用于存储一些特殊的日志
			StringBuffer newStr = new StringBuffer();	// 用于存储一些特殊的日志的数据
			JSONObject json = new JSONObject();
			
			if("修改订单".equals(title)){     //修改订单
				//判断订单状态是否修改 和之前的订单状态进行比较
				if(orders.getOrderstatus() != oldOrders.getOrderstatus()){
					str.append("订单状态:修改前("+NameUtil.typeNames("0",String.valueOf(oldOrders.getOrderstatus()))+"),修改后("+NameUtil.typeNames("0",String.valueOf(orders.getOrderstatus()))+")--");
					newStr.append("订单状态:修改前("+oldOrders.getOrderstatus()+"),修改后("+orders.getOrderstatus()+")--");
				}
				
				//判断付款方式是否修改和之前的付款方式进行比较
				String payCode = orders.getPaycode() == null ? "" : orders.getPaycode();//修改之后的付款方式
				String oldPayCode = oldOrders.getPaycode() == null ? "" : oldOrders.getPaycode();//修改之前的付款方式
				if(!oldPayCode.equals(payCode)){
					str.append("付款方式:修改前("+NameUtil.typeNames("1",oldPayCode)+"),修改后("+NameUtil.typeNames("1",payCode)+")--");
					newStr.append("付款方式:修改前("+oldPayCode+"),修改后("+payCode+")--");
				}
				
				//判断发货类型是否修改和之前的发货类型进行比较
				if(orders.getShippingtype() != oldOrders.getShippingtype()){
					str.append("发货类型:修改前("+NameUtil.typeNames("3",String.valueOf(oldOrders.getShippingtype()))+"),修改后("+NameUtil.typeNames("3",String.valueOf(orders.getShippingtype()))+")--");
					newStr.append("发货类型:修改前("+oldOrders.getShippingtype()+"),修改后("+orders.getShippingtype()+")--");
				}
				
				json.put("property", "[\"userNote\",\"consignee\",\"mobile\",\"address\"]");
				json.put("name", "[\"留言备注\",\"收货人\",\"联系电话\",\"收货地址\"]");
				
			}else if("首次修改物流".equals(title)){    //第一次修改物流
				if(oldOrders.getOrderstatus() != 2){
					str.append("订单状态:修改前("+NameUtil.typeNames("0",String.valueOf(oldOrders.getOrderstatus()))+"),修改后(待收货)--");
					newStr.append("订单状态:修改前("+oldOrders.getOrderstatus()+"),修改后(2)--");
				}
				
				json.put("property", "[\"shippingtime\",\"shippingcode\",\"shippingname\"]");
				json.put("name", "[\"发货时间\",\"快递单号\",\"快递公司\"]");
				
			}else if("确认收货".equals(title)){    //确认收货
				str.append("订单状态:修改前("+NameUtil.typeNames("0",String.valueOf(oldOrders.getOrderstatus()))+"),修改后(已完成)--");
				newStr.append("订单状态:修改前("+oldOrders.getOrderstatus()+"),修改后(4)--");

				json.put("property", "[]");
				json.put("name", "[]");
				
			}else if("用户取货".equals(title)){   //用户取货
				//判断是否用户已取货
				str.append("用户已取货:修改前(未取货),修改后(已取货)--");
				newStr.append("用户已取货:修改前("+oldOrders.getIsPickUp()+"),修改后(1)--");
				
				json.put("property", "[\"pickUpNote\"]");
				json.put("name", "[\"确认取货备注\"]");
				
			}else if("首次导入物流".equals(title)){    //第一次导入物流
				if(oldOrders.getOrderstatus() != 2){
					str.append("订单状态:修改前("+NameUtil.typeNames("0",String.valueOf(oldOrders.getOrderstatus()))+"),修改后(待收货)--");
					newStr.append("订单状态:修改前("+oldOrders.getOrderstatus()+"),修改后(2)--");
				}
				
				json.put("property", "[\"shippingtime\",\"shippingcode\",\"shippingname\"]");
				json.put("name", "[\"发货时间\",\"快递单号\",\"快递公司\"]");
				
			}else if("修改物流".equals(title)){    //非第一次修改物流
				json.put("property", "[\"shippingtime\",\"shippingcode\",\"shippingname\"]");
				json.put("name", "[\"发货时间\",\"快递单号\",\"快递公司\"]");
				
			}else if("导入物流".equals(title)){    //非第一次导入物流
				json.put("property", "[\"shippingtime\",\"shippingcode\",\"shippingname\"]");
				json.put("name", "[\"发货时间\",\"快递单号\",\"快递公司\"]");
			
			}else if("强制取消".equals(title)){    //强制取消
				str.append("订单状态:修改前("+NameUtil.typeNames("0",String.valueOf(oldOrders.getOrderstatus()))+"),修改后(取消订单)--");
				newStr.append("订单状态:修改前("+oldOrders.getOrderstatus()+"),修改后(-2)--");

				json.put("property", "[]");
				json.put("name", "[]");
			}
			
			String string = SaveLogUtils.saveLog(json,str.toString(),oldOrders,orders);
			String newString = SaveLogUtils.saveLog(json,newStr.toString(),oldOrders,orders);
			if(!"".equals(string) && null != string && !"".equals(newString) && null != newString){
				OrdersLog ordersLog = new OrdersLog();
				ordersLog.setOrderid(orders.getOrderid());
				ordersLog.setTitle(title);
				ordersLog.setContent(string);
				ordersLog.setContentRecord(newString);
				ordersLog.setChannelFlag("bm");
				ordersLog.setPlatformFlag("mtmy");
				ordersLog.setCreateBy(user);
				ordersLog.setCreateOfficeIds(user.getOffice().getParentIds() + user.getOffice().getId() + ",");
				ordersLogDao.saveOrdersLog(ordersLog);
			}
			
	}
	
	/**
	 * 保存预约日志
	 * @param request
	 * @param title
	 * @param object
	 * @param object1
	 */
	public void saveApptOrderLog(HttpServletRequest request, String title,List<Object> obj){
		
		// 修改内容（用于界面展示）,修改内容（用于记录）
		String content = null,contentRecord = null;
		int reservationId = 0;	// 预约id
		JSONObject json = new JSONObject();
		
		if("修改预约".equals(title)){
			Reservation oldR = (Reservation)obj.get(0);	// 修改前对象
			Reservation newR = (Reservation)obj.get(1); // 修改后对象
			reservationId = oldR.getReservationId();
			String str = "";
			// 要比较的参数
			json.put("property", "[\"apptDate\",\"apptStartTime\",\"apptEndTime\",\"userNote\",\"remarks\"]");
			json.put("name", "[\"预约日期\",\"预约开始时间\",\"预约结束时间\",\"消费者备注\",\"管理员备注\"]");
			
			oldR.setRemarks((oldR.getRemarks() == null || "".equals(oldR.getRemarks())) ? "无" : oldR.getRemarks());
			newR.setRemarks((newR.getRemarks() == null || "".equals(newR.getRemarks())) ? "无" : newR.getRemarks());
			
			if (!newR.getRemarks().equals(oldR.getRemarks())) {
				str = "管理员备注:修改前("+oldR.getRemarks()+"),修改后("+newR.getRemarks()+")--";
			}
			
			String string = SaveLogUtils.saveLog(json,str,oldR,newR);
			
			if (!oldR.getApptStatus().equals(newR.getApptStatus())) {
				String oldApptStatus;
				String newApptStatus;
				
				// 预约状态（0：等待服务；1：已完成；2：已评价；3：已取消；4：客户爽约）
				oldApptStatus = NameUtil.typeNames("2",oldR.getApptStatus());
				newApptStatus = NameUtil.typeNames("2",newR.getApptStatus());
				
				content = "预约状态:修改前("+oldApptStatus+"),修改后("+newApptStatus+")--";
				contentRecord = "预约状态:修改前("+oldR.getApptStatus()+"),修改后("+newR.getApptStatus()+")--";
				
			}
			
			content = string + content;	//	修改日志，用于界面展示
			contentRecord = string + contentRecord;	//	修改日志，用于记录
			
		}else if("添加/修改实际服务时间".equals(title)){
			Reservation oldR = (Reservation)obj.get(0);	// 修改前对象
			Reservation newR = (Reservation)obj.get(1); // 修改后对象
			reservationId = oldR.getReservationId();
			// 要比较的参数
			json.put("property", "[\"serviceStartTime\",\"serviceEndTime\"]");
			json.put("name", "[\"实际服务开始时间\",\"实际服务结束时间\"]");
			
			String string = SaveLogUtils.saveLog(json,null,oldR,newR);
			content = contentRecord = string;
			
		}else if ("预约自动完成".equals(title)){
			reservationId = (int)obj.get(0);
			content = contentRecord = "预约状态:修改前(等待服务),修改后(已完成)--";
		}

		// 保存操作日志
		if ((null != content && !"".equals(content)) || (null != contentRecord && !"".equals(contentRecord))) {
			User user = UserUtils.getUser();
			ReservationLog reservationLog = new ReservationLog();
			reservationLog.setReservationId(reservationId);
			reservationLog.setTitle(title);
			reservationLog.setContent(content);
			reservationLog.setContentRecord(contentRecord);
			reservationLog.setChannelFlag("bm");
			reservationLog.setPlatformFlag("mtmy");
			reservationLog.setCreateBy(user);
			reservationLog.setCreateOfficeIds(user.getOffice().getParentIds()+user.getOffice().getId()+",");
			reservationDao.saveApptOrderLog(reservationLog);
		}
		
	}
		
}
