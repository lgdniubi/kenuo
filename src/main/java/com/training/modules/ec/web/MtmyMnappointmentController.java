package com.training.modules.ec.web;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.CommentService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
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
	@Autowired
	private SystemService systemService;
	@Autowired
	private CommentService commentService;
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
			User user = UserUtils.getUser(); // 当前登录人
			String weburl = ParametersFactory.getMtmyParamValues("mtmy_delphysubscribe_url");
			logger.info("##### web接口路径:"+weburl);
			String parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"client\":\"bm\",\"update_by\":\""+user.getId()+"\",\"create_office_ids\":\""+user.getOffice().getParentIds()+user.getOffice().getId()+","+"\"}";
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
	@RequestMapping(value = "addReservation")     //addMnappointment
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
	public Map<String, Object> loadOffice(String areaId,String goodsIds,String franchiseeId,Boolean isAll,HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Set<Map<String, Object>> setMain = new HashSet<Map<String, Object>>();
		List<Office> list1= reservationService.loadOffice(goodsIds,franchiseeId,areaId);  	// 可用店铺
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
		User loginUser = UserUtils.getUser();	//获取登录用户
		jsonMap.put("loginOfficeId", loginUser.getOffice().getId());
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
			User loginUser = UserUtils.getUser();	//获取登录用户
			jsonObject.put("loginUserId", loginUser.getId());
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
//			if(StringUtils.isEmpty(labelId)){
//				labelId = "100000";
//			}
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
	public String saveReservation(Users users,String beauticianId,String shopId,String times,String recid,String servicemin,String userNote,String groupId,String isReal,String goodsName,int sendToUserFlag,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try {
			List<Map<String, Object>> mapList = Lists.newArrayList();
			Map<String, Object> map = Maps.newHashMap();
			User loginUser = UserUtils.getUser();	//获取登录用户
			String dateStr = times.replace("星期一", "").replace("星期二","").replace("星期三","").replace("星期四","").replace("星期五","").replace("星期六","").replace("星期日","");
			Date date = DateUtils.parseDate(dateStr);
			String appt_date = DateUtils.formatDate(date, "yyyy-MM-dd");
			String appt_start_time = DateUtils.formatDate(date, "HH:mm");
			String websaveReservation = ParametersFactory.getMtmyParamValues("mtmy_savereservation");
			String shopName = null;	// 预约店铺id
			String beauticianName = null;	// 美容师名称
			String beaOfficeId = null;		// 美容师所属机构id
			String beauticianPhoto = null;   //美容师照片
			Office office = officeService.get(shopId);
			if(null != office){
				shopName = office.getName();	
			}
			User user = systemService.getUser(beauticianId);
			if(null != user){
				beauticianName = user.getName();
				beaOfficeId = user.getOffice().getId();
				beauticianPhoto = user.getPhoto();
			}
			
			map.put("beautician_id",beauticianId);
			map.put("beautician_name",beauticianName);
			map.put("beautician_photo",beauticianPhoto);
			map.put("bea_office_id",beaOfficeId);
			map.put("group_id",Integer.valueOf(groupId));
			map.put("appt_start_time",appt_start_time);
			map.put("rec_id",recid);
			map.put("is_real",isReal);
			map.put("service_min",servicemin);
			map.put("appt_date",appt_date);
			map.put("goods_name",goodsName);
			
			mapList.add(map);
			
			
			JSONArray jsonArray = JSONArray.fromObject(mapList);
			logger.info("##### web接口路径:"+websaveReservation);
			String parpm = "{\"shop_id\":\""+shopId+"\",\"shop_name\":\""+shopName+"\",\"user_id\":\""+users.getUserid()+"\",\"user_name\":\""+users.getName()+"\",\"user_phone\":\""+users.getMobile()+"\",\"client\":\"bm\",\"subscribes\":"+jsonArray.toString()+",\"user_note\":\""+userNote+"\",\"create_by\":\""+loginUser.getId()+"\",\"sendToUserFlag\":"+sendToUserFlag+"}";
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
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"appt_date\":\""+appt_date+"\",\"appt_start_time\":\""+reservation.getApptStartTime()+"\",\"appt_end_time\":\""+reservation.getApptEndTime()+"\",\"client\":\"bm\",\"remarks\":\""+reservation.getRemarks()+"\",\"update_by\":\""+user.getId()+"\",\"create_office_ids\":\""+user.getOffice().getParentIds()+user.getOffice().getId()+","+"\"}";
				}else if("3".equals(reservation.getApptStatus())){
					url = ParametersFactory.getMtmyParamValues("mtmy_delphysubscribe_url");
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"client\":\"bm\",\"update_by\":\""+user.getId()+"\",\"create_office_ids\":\""+user.getOffice().getParentIds()+user.getOffice().getId()+","+"\"}";
				}else{
					url = ParametersFactory.getMtmyParamValues("mtmy_updateSubscribeStatus");	//修改预约状态
					parpm = "{\"appt_id\":"+reservation.getReservationId()+",\"appt_status\":"+reservation.getApptStatus()+",\"client\":\"bm\",\"remarks\":\""+reservation.getRemarks()+"\",\"update_by\":\""+user.getId()+"\",\"create_office_ids\":\""+user.getOffice().getParentIds()+user.getOffice().getId()+","+"\"}";
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
	/**
	 * 添加预约界面跳转
	 * @return
	 */
	@RequiresPermissions("ec:mtmyMnappointment:addMoreReservation")
	@RequestMapping(value = "addMoreReservation")     //addMoreReservation
	public String addMoreReservation(){
		return "modules/ec/addMoreMnappointment";
	}
	/**
	 * 多项目预约查询用户已购买的项目
	 * @param userid
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="alreadyBuyProject")
	public String alreadyBuyProject(String userid,String chooseRecIds,String chooseParentRecIds,HttpServletRequest request,Model model){
		try{
			if(!"".equals(userid) && userid != null){
				List<OrderGoods> lists = reservationService.findOrderGoodsByUserId(Integer.valueOf(userid));
				model.addAttribute("lists",lists);
				model.addAttribute("chooseRecIds",chooseRecIds);
				model.addAttribute("chooseParentRecIds",chooseParentRecIds);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "多项目预约查询用户已购买的项目", e);
			logger.error("多项目预约查询用户已购买的项目错误信息:"+e.getMessage());
		}
		return "modules/ec/alreadyBuyProject";
	}
	
	/**
	 * 多项目预约加载美容师
	 * @param shopIp
	 * @param skillId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadMoreBeaut")
	public String loadMoreBeaut(String shopId,String skillId,String recId,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		try {
			String webReservationTime =	ParametersFactory.getMtmyParamValues("mtmy_querySubscribeShopDetai");	
			logger.info("##### web接口路径:"+webReservationTime);
			String parpm = "{\"shop_id\":\""+shopId+"\",\"skill_id\":\""+skillId+"\"}";
			String url=webReservationTime;
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			User loginUser = UserUtils.getUser();	//获取登录用户
			jsonObject.put("loginUserId", loginUser.getId());
			jsonObject.put("recId", recId);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:多项目预约异步加载当前美容师", e);
			logger.error("调用接口:多项目预约异步加载当前美容师错误信息:"+e.getMessage());
		}
		return jsonObject.toString();
	}
	
	/**
	 * 添加多项目预约
	 * @param orderGoods
	 * @return
	 */
	@RequestMapping(value = "saveMoreReservation")
	@ResponseBody
	public String saveMoreReservation(Users users,String beauticianId,String shopId,String times,String chooseRecId,String serviceMin,String userNote,String chooseParentRecId,String isReal,String goodsName,int sendToUserFlag,RedirectAttributes redirectAttributes,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		try {
			String[] beauticianIds = beauticianId.split(",");
			String[] newTimes = times.split(",");
			String[] chooseRecIds = chooseRecId.split(",");
			String[] serviceMins = serviceMin.split(",");
			String[] chooseParentRecIds = chooseParentRecId.split(",");
			String[] isReals = isReal.split(",");
			String[] goodsNames = goodsName.split(",");
			
			List<Map<String, Object>> mapList = Lists.newArrayList();
			if(chooseRecIds.length > 0){
				for(int i=0;i<chooseRecIds.length;i++){
					Map<String, Object> map = Maps.newHashMap();
					String dateStr = newTimes[i].replace("星期一", "").replace("星期二","").replace("星期三","").replace("星期四","").replace("星期五","").replace("星期六","").replace("星期日","");
					Date date = DateUtils.parseDate(dateStr);
					String appt_date = DateUtils.formatDate(date, "yyyy-MM-dd");
					String appt_start_time = DateUtils.formatDate(date, "HH:mm");
					String beauticianName = null;	// 美容师名称
					String beaOfficeId = null;		// 美容师所属机构id
					String beauticianPhoto = null;   //美容师照片
					User user = systemService.getUser(beauticianIds[i]);
					if(null != user){
						beauticianName = user.getName();
						beauticianPhoto = user.getPhoto();
						beaOfficeId = user.getOffice().getId();
					}
					map.put("beautician_id",beauticianIds[i]);
					map.put("beautician_name",beauticianName);
					map.put("beautician_photo",beauticianPhoto);
					map.put("bea_office_id",beaOfficeId);
					map.put("group_id",Integer.valueOf(chooseParentRecIds[i]));
					map.put("appt_start_time",appt_start_time);
					map.put("rec_id",chooseRecIds[i]);
					map.put("is_real",isReals[i]);
					map.put("service_min",serviceMins[i]);
					map.put("appt_date",appt_date);
					map.put("goods_name",goodsNames[i]);
					
					
					mapList.add(map);
				}
			}
			
			JSONArray jsonArray = JSONArray.fromObject(mapList);
			User loginUser = UserUtils.getUser();	//获取登录用户
			String shopName = null;	// 预约店铺名称
			Office office = officeService.get(shopId);
			if(null != office){
				shopName = office.getName();	
			}
			String websaveReservation = ParametersFactory.getMtmyParamValues("mtmy_savereservation");
			logger.info("##### web接口路径:"+websaveReservation);
			String parpm = "{\"shop_id\":\""+shopId+"\",\"shop_name\":\""+shopName+"\",\"user_id\":\""+users.getUserid()+"\",\"user_name\":\""+users.getName()+"\",\"user_phone\":\""+users.getMobile()+"\",\"client\":\"bm\",\"subscribes\":"+jsonArray.toString()+",\"user_note\":\""+userNote+"\",\"create_by\":\""+loginUser.getId()+"\",\"sendToUserFlag\":"+sendToUserFlag+"}";
			logger.info("#### 添加预约参数"+parpm);
			String url=websaveReservation;
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg"));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:添加多项目预约", e);
			logger.error("调用接口:添加多项目预约错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "添加多项目预约出现异常，请与管理员联系");
		}
		return jsonObject.toString();
	}
	
	/**
	 * 跳转到 添加/修改  实际服务时长
	 * @param reservation
	 * @return
	 */
	@RequestMapping(value = "getServiceTime")
	public String getServiceTime(Reservation reservation, Model model){
		reservation = reservationService.getServiceTime(reservation);
		model.addAttribute("reservation",reservation);
		return "modules/ec/editServiceTime";
	}
	
	/**
	 * 添加/修改  实际服务时长
	 * @param reservation
	 * @return
	 */
	@RequestMapping(value = "editServiceTime")
	public String editServiceTime(Reservation reservation, HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			reservationService.editServiceTime(reservation);
			addMessage(redirectAttributes, "预约管理  添加/修改实际服务时间'" + reservation.getReservationId() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "预约管理  添加/修改实际服务时间", e);
			logger.error("预约管理  添加/修改实际服务时间错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "预约管理  添加/修改实际服务时间");
		}
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/mnappointment";
	}
	
	/**
	 * 查看美容师和店铺的评价
	 * @param reservation
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "viewComments")
	public String viewComments(Reservation reservation, Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			List<Comment> beautyComment = commentService.queryBeautyForReservation(reservation.getReservationId());
			List<Comment> shopComment = commentService.queryShopForReservation(reservation.getReservationId());
			model.addAttribute("beautyComment",beautyComment);
			model.addAttribute("shopComment",shopComment);
			model.addAttribute("reservationId",reservation.getReservationId());
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看美容师和店铺的评价", e);
			logger.error("查看美容师和店铺的评价错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看美容师和店铺的评价");
		}
		return "modules/ec/viewComments";
	}
	
	/**
	 * 回复美容师评论
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="replybeautyComment")
	public String replybeautyComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try {
			User currentUser = UserUtils.getUser();
			comment.setReplyId(currentUser.getId());
			commentService.insterbeautyComment(comment);
			//修改单个用户所涉及的商品评论
			commentService.updateBeautyComment(comment);
			addMessage(redirectAttributes, "回复用户评论成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "回复美容师评论", e);
			logger.error("回复美容师评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "回复美容师评论");
		}
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/viewComments?reservationId="+comment.getReservationId();
	}
	
	/**
	 * 回复店铺评论
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="replyShopComment")
	public String replyShopComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try {
			User currentUser = UserUtils.getUser();
			comment.setReplyId(currentUser.getId());
			commentService.insterShopComment(comment);
			//修改单个用户所涉及的商品评论
			commentService.updateShopComment(comment);
			addMessage(redirectAttributes, "回复用户评论成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "回复店铺评论", e);
			logger.error("回复店铺评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "回复店铺评论");
		}
		return "redirect:" + adminPath + "/ec/mtmyMnappointment/viewComments?reservationId="+comment.getReservationId();
	}
}
