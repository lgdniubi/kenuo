package com.training.modules.ec.web;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
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
	@Autowired
	private OfficeService officeService;
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
	 * 查询单个预约详情
	 * @param reservation
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "oneMnappointment")
	public String oneMnappointment(Reservation reservation,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		reservation=reservationService.oneMnappointment(reservation);
		model.addAttribute("reservation", reservation);
		return "modules/ec/mnappointmentFrom";
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
			String weburl = ParametersFactory.getMtmyParamValues("mtmy_delphysubscribe_url");
			logger.info("##### web接口路径:"+weburl);
			String parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"client\":\"bm\"}";
			String url=weburl;
			String result = WebUtils.postObject(parpm, url);
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
	 * 添加预约界面跳转
	 * @return
	 */
	@RequiresPermissions("ec:mtmyMnappointment:addReservation")
	@RequestMapping(value = "addReservation")
	public String addReservation(){
		return "modules/ec/addMnappointment";
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
	 * 异步加载可用店铺
	 * @param reservation
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadOffice")
	public Map<String, Object> loadOffice(String areaId,int goodsId,Boolean isAll,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Set<Map<String, Object>> setMain = new HashSet<Map<String, Object>>();
		List<Office> list1= reservationService.loadOffice(goodsId,areaId);  	// 可用店铺
		List<Office> list2 = officeService.findList(isAll);		//有权限机构
		if(list1.size() > 0 && list2.size() > 0){		// 当两个集合都存在值时  取交集 
			Map<String, Object> setMap = new HashMap<String, Object>();
			
			Set<Map<String, Object>> set1 = new HashSet<Map<String, Object>>();
			for (int j = 0; j < list2.size(); j++) {	// 循环 有权限的机构
				setMap = new HashMap<String, Object>();
				setMap.put("officeId", list2.get(j).getId());
				setMap.put("officeName", list2.get(j).getName());
				set1.add(setMap);
			}
			
			Set<Map<String, Object>> set2 = new HashSet<Map<String, Object>>();
			for (int i = 0; i < list1.size(); i++) {
				setMap = new HashMap<String, Object>();
				setMap.put("officeId", list1.get(i).getId());
				setMap.put("officeName", list1.get(i).getName());
				set2.add(setMap);
			}
			
			setMain.clear();
			setMain.addAll(set1);
			setMain.retainAll(set2);
		}
		jsonMap.put("office", setMain);
		return jsonMap;
	}
	/**
	 * 加载美容师
	 * @param shopIp
	 * @param skillId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadBeaut")
	public String loadBeaut(String shopId,String skillId,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		try {
			String webReservationTime =	ParametersFactory.getMtmyParamValues("mtmy_querySubscribeShopDetai");	
			logger.info("##### web接口路径:"+webReservationTime);
			String parpm = "{\"shop_id\":\""+shopId+"\",\"skill_id\":\""+skillId+"\"}";
			String url=webReservationTime;
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:异步加载当前美容师", e);
			logger.error("调用接口:异步加载当前美容师错误信息:"+e.getMessage());
		}
		return jsonObject.toString();
	}
	/**
	 * 异步加载当前美容师的可用时间
	 * @param reservation
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ReservationTime")
	public String ReservationTime(String beauticianId,String serviceMin,String shopId,String labelId,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		try {
			if(StringUtils.isEmpty(labelId)){
				labelId = "100000";
			}
			String webReservationTime =	ParametersFactory.getMtmyParamValues("mtmy_reservationtime");	
			logger.info("##### web接口路径:"+webReservationTime);
			String parpm = "{\"beauty_id\":\""+beauticianId+"\",\"service_min\":\""+serviceMin+"\",\"shop_id\":\""+shopId+"\",\"label_id\":\""+labelId+"\"}";
			String url=webReservationTime;
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:异步加载当前美容师的可用时间", e);
			logger.error("调用接口:异步加载当前美容师的可用时间错误信息:"+e.getMessage());
		}
		return jsonObject.toString();
	}
	/**
	 * 添加预约
	 * @param orderGoods
	 * @return
	 */
	@RequestMapping(value = "saveReservation")
	public String saveReservation(Users users,String beauticianId,String shopId,String times,String recid,String servicemin,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			String dateStr = times.replace("星期一", "").replace("星期二","").replace("星期三","").replace("星期四","").replace("星期五","").replace("星期六","").replace("星期日","");
			Date date = DateUtils.parseDate(dateStr);
			String appt_date = DateUtils.formatDate(date, "yyyy-MM-dd");
			String appt_start_time = DateUtils.formatDate(date, "HH:mm");
			String websaveReservation = ParametersFactory.getMtmyParamValues("mtmy_savereservation");
			
			logger.info("##### web接口路径:"+websaveReservation);
			String parpm = "{\"beautician_id\":\""+beauticianId+"\",\"user_id\":\""+users.getUserid()+"\",\"user_phone\":\""+users.getMobile()+"\",\"user_name\":\""+users.getName()+"\",\"rec_id\":\""+recid+"\",\"appt_date\":\""+appt_date+"\",\"appt_start_time\":\""+appt_start_time+"\",\"shop_id\":\""+shopId+"\",\"service_min\":\""+servicemin+"\",\"client\":\"bm\"}";
			logger.info("#### 添加预约参数"+parpm);
			String url=websaveReservation;
			String result = WebUtils.postObject(parpm, url);
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
	 * 修改预约
	 * @param reservation
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateMnappointment")
	public String updateMnappointment(Reservation reservation,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			if(!"0".equals(reservation.getApptStatus())){
				User user = UserUtils.getUser();
				String url= null; 
				String parpm = null;
				if("1".equals(reservation.getApptStatus())){
					url = ParametersFactory.getMtmyParamValues("mtmy_finishSubscribeStatus");	//修改预约时间且状态为已完成
					String appt_date = DateUtils.formatDate(reservation.getApptDate(), "yyyy-MM-dd");
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"appt_date\":\""+appt_date+"\",\"appt_start_time\":\""+reservation.getApptStartTime()+"\",\"appt_end_time\":\""+reservation.getApptEndTime()+"\",\"client\":\"bm\",\"remarks\":\""+reservation.getRemarks()+"\",\"update_by\":\""+user.getId()+"\"}";
				}else if("3".equals(reservation.getApptStatus())){
					url = ParametersFactory.getMtmyParamValues("mtmy_delphysubscribe_url");
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"client\":\"bm\"}";
				}else{
					url = ParametersFactory.getMtmyParamValues("mtmy_updateSubscribeStatus");	//修改预约状态
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"appt_status\":"+reservation.getApptStatus()+",\"client\":\"bm\",\"remarks\":\""+reservation.getRemarks()+"\",\"update_by\":\""+user.getId()+"\"}";
				}
				logger.info("##### web接口路径:"+url);
				String result = WebUtils.postObject(parpm, url);
				JSONObject jsonObject = JSONObject.fromObject(result);
				logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
				if("200".equals(jsonObject.get("code"))){
					addMessage(redirectAttributes, "修改预约成功");
				}else{
					addMessage(redirectAttributes, "修改预约失败:"+jsonObject.get("msg"));
				}
			}else{
				addMessage(redirectAttributes, "修改预约出现异常，预约状态有误");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:修改预约", e);
			logger.error("调用接口:修改预约错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "修改预约出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/mnappointment";
	}
}
