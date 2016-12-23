package com.training.modules.ec.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.ReservationLog;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONObject;
/**
 * 预约 Controller
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyMnappointment")
public class MtmyMnappointmentController extends BaseController{
	
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private OrderGoodsService orderGoodsService;
	
	/**
	 * 预约管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "mnappointment")
	public String mnappointment(Reservation reservation,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<Reservation> page=reservationService.findPage(new Page<Reservation>(request, response), reservation);
		model.addAttribute("page", page);
		model.addAttribute("reservation", reservation);
		return "modules/ec/mnappointment";
	}
	
	/**
	 * 添加预约界面跳转
	 * @return
	 */
	@RequiresPermissions("ec:mtmyMnappointment:addReservation")
	@RequestMapping(value = "addReservation")
	public String addReservation(){
		return "modules/ec/addMnappointment";
	}
	
	/**
	 * 异步加载当前美容师的可用时间
	 * @param reservation
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ReservationTime")
	public String ReservationTime(Reservation reservation,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		try {
			String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
			String webReservationTime =	ParametersFactory.getMtmyParamValues("mtmy_reservationtime");	
			
			logger.info("##### web接口版本号："+connector+",路径:"+webReservationTime);
			String parpm = "{'version':'"+connector+"','beauty_id':'"+reservation.getBeauticianId()+"'}";
			String url=webReservationTime;
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:异步加载当前美容师的可用时间", e);
			logger.error("调用接口:异步加载当前美容师的可用时间错误信息:"+e.getMessage());
		}
		return jsonObject.toString();
	}
	/**
	 * 通过订单号查询订单下的虚拟商品
	 * @param orderGoods
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findOrderGoods")
	public Map<String, List<OrderGoods>> findOrderGoods(OrderGoods orderGoods){
		Map<String, List<OrderGoods>> jsonMap = new HashMap<String, List<OrderGoods>>();
		List<OrderGoods> list = orderGoodsService.findOrderGoods(orderGoods);
		jsonMap.put("orderGoods", list);
		return jsonMap;
	}
	/**
	 * 添加预约
	 * @param orderGoods
	 * @return
	 */
	@RequestMapping(value = "saveReservation")
	public String saveReservation(Users users,String beauticianId,String officeIdbeaut,String times,String recid,String servicemin,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
//			时间转换异常
//			logger.info("##### 预约时间："+times+";预约时间的长度(正常19位)："+times.length());
//			SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd EHH:mm");
//			Date d = sim.parse(times.toString());
//			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
//			logger.info("##### 预约时间（格式化后）："+dateStr);
			String dateStr=times.replace("星期一", "").replace("星期二","").replace("星期三","").replace("星期四","").replace("星期五","").replace("星期六","").replace("星期日","");
			
			String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
			String websaveReservation = ParametersFactory.getMtmyParamValues("mtmy_savereservation");
			
			logger.info("##### web接口版本号："+connector+",路径:"+websaveReservation);
			String parpm = "{'version':'"+connector+"','beautician_id':'"+beauticianId+"','user_phoneNum':'"+users.getMobile()+"','user_id':'"+users.getUserid()+"','reservation_dayTime':'"+dateStr+"','shop_id':'"+officeIdbeaut+"','user_name':'"+users.getName()+"','rec_id':'"+recid+"','service_min':'"+servicemin+"'}";
			System.out.println(parpm);
			String url=websaveReservation;
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg"));
			if("200".equals(jsonObject.get("code"))){
				addMessage(redirectAttributes, "添加预约成功");
			}else{
				addMessage(redirectAttributes, "添加预约失败:"+jsonObject.get("msg"));
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:添加预约", e);
			logger.error("调用接口:添加预约错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "添加预约出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/mnappointment";
	}
	/**
	 * 取消预约
	 * @param reservation
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyMnappointment:cancel"},logical=Logical.OR)
	@RequestMapping(value = "cancel")
	public String cancel(Reservation reservation,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try {
			
			String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
			String weburl = ParametersFactory.getMtmyParamValues("mtmy_delphysubscribe_url");
					
			logger.info("##### web接口版本号："+connector+",路径:"+weburl);
			String parpm = "{'version':'"+connector+"','reservation_id':"+reservation.getReservationId()+"}";
			String url=weburl;
			WebUtils webUtils = new WebUtils();
			String result = webUtils.webUtilsMain(parpm, url);
			JSONObject jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg"));
			if("200".equals(jsonObject.get("code"))){
				addMessage(redirectAttributes, "取消预约成功");
			}else{
				addMessage(redirectAttributes, "取消预约失败");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:取消预约", e);
			logger.error("调用接口:取消预约错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "取消预约出现异常，请与管理员联系");
		}
		
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/mnappointment";
	}
	/**
	 * 查询单个预约详情
	 * @param reservation
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "oneMnappointment")
	public String oneMnappointment(Reservation reservation,ReservationLog reservationLog,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		reservation=reservationService.oneMnappointment(reservation);
		model.addAttribute("reservation", reservation);
		
		List<ReservationLog> list = reservationService.findLog(reservationLog);
		model.addAttribute("reservationLog", list);
		return "modules/ec/mnappointmentFrom";
	}
	/**
	 * 分页查询预约日志详情
	 * @param reservationLog
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findListLog")
	public String findListLog(ReservationLog reservationLog,HttpServletRequest request,HttpServletResponse response,Model model){
		Page<ReservationLog> page=reservationService.findListLog(new Page<ReservationLog>(request, response), reservationLog);
		model.addAttribute("page", page);
		model.addAttribute("reservationLog", reservationLog);
		return "modules/ec/mnappointmentLogForm";
	}
	/**
	 * 修改预约详情
	 * @param reservation
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "updateMnappointment")
	public String updateMnappointment(Reservation reservation,ReservationLog reservationLog,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		User currentUser = UserUtils.getUser();
		
		reservationLog.setCreateBy(currentUser);
		reservationLog.setShopId(request.getParameter("officeIdbeaut"));
		
		//修改前先保存修改前日志
		Reservation newReservation = reservationService.oneMnappointment(reservation);
		ReservationLog newReservationLog = new ReservationLog();
		newReservationLog.setCreateBy(currentUser);
		newReservationLog.setReservationId(newReservation.getReservationId());
		newReservationLog.setBeauticianId(newReservation.getBeauticianId());
		newReservationLog.setShopId(newReservation.getShopId());
		newReservationLog.setReservationStatus(Integer.parseInt(newReservation.getReservationStatus()));
		newReservationLog.setRemarks(newReservation.getRemarks());
		reservationService.saveLog(newReservationLog);
		
		reservation.setId(currentUser.getId());
		reservationService.updateMnappointment(reservation);
		
		addMessage(redirectAttributes, "修改预约成功");
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/mnappointment";
	}
	/**
	 * 异步加载所有美容师
	 * @param reservation
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadBeauty")
	public Map<String, List<User>> loadBeauty(Reservation reservation,HttpServletRequest request, HttpServletResponse response){
		Map<String, List<User>> jsonMap = new HashMap<String, List<User>>();
		List<User> user= reservationService.loadBeauty(reservation);
		jsonMap.put("user",user);
		return jsonMap;
	}
	/**
	 * 客户详情
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "customerdetails")
	public String customerdetails(Reservation reservation,Model model) {
		
		return "modules/ec/customerdetails";
	}
}
