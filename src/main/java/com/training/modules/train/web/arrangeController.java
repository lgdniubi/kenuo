package com.training.modules.train.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ArrangeBeautician;
import com.training.modules.train.entity.ArrangeEquipment;
import com.training.modules.train.entity.ArrangeShop;
import com.training.modules.train.service.ArrangeService;


/**
 * 排班管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/arrange")
public class arrangeController extends BaseController{
	
	public static final int RESERVATIONDATE = 8; //可预约天数 - 2
	
	@Autowired
	private ArrangeService arrangeService;
	/**
	 * 市场排班list
	 * @param model
	 * @param arrangeShop
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(Model model,ArrangeShop arrangeShop,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(null != arrangeShop.getSearchOfficeId() && !"".equals(arrangeShop.getSearchOfficeId())){
				if("nextMonth".equals(request.getParameter("nextMonth"))){
					//  下一个月
					 Calendar nextDate = Calendar.getInstance();   
					 nextDate.add(Calendar.MONTH,1);//下一个月   
					 nextDate.set(Calendar.DATE, 1);//把日期设置为当月第一天    
			         arrangeShop.setSearcTime(nextDate.getTime());
			         model.addAttribute("nextMonth", "nextMonth");
				}else{
					 arrangeShop.setSearcTime(new Date());
				}
				Map<String, Integer> map = calendarDay(arrangeShop.getSearcTime());	//获取月份天数、月份、时间
				String str = calendar(arrangeShop.getSearcTime(),arrangeShop.getSearchOfficeName(),"shop");		//table
				//  拼接表格  tr
				model.addAttribute("calendarStr", str);
				//   用于回写
				model.addAttribute("arrangeShop", arrangeShop);
				List<ArrangeBeautician> lists = new ArrayList<ArrangeBeautician>();
				//   查询当前市场下的所有店铺
				List<Office> list = arrangeService.findOffice(arrangeShop.getSearchOfficeId());
				for (Office office : list) {
					ArrangeBeautician arrangeBeautician = new ArrangeBeautician();
					arrangeBeautician.setOfficeId(office.getId());
					arrangeBeautician.setName(office.getName());
					List<ArrangeShop> l = new ArrayList<ArrangeShop>();
					//查询店铺上班时间
					List<Integer> iList = arrangeService.findArrange(office.getId(), map.get("month"));
					//  循环当月天数
					for (int i = 1; i <= map.get("maxDay"); i++) {
						ArrangeShop arr = new ArrangeShop();
						int y = 0;
		    			if(iList.size() > 0){
		    				for (Integer integer : iList) {
		    					if(integer == i){
		    						y = 1;
		    						arr.setStatus(1);
		    					}
		    				}
		    			}
		    			if(y == 0){
		    				arr.setStatus(2);
		    			}
						l.add(arr);
					}
					arrangeBeautician.setArrangeShops(l);
					lists.add(arrangeBeautician);
				}
				model.addAttribute("lists", lists);
				model.addAttribute("month",map.get("month"));
				model.addAttribute("bazaarName", arrangeShop.getSearchOfficeName());
			}else{
				model.addAttribute("message", "请选择市场");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询市场排班", e);
			logger.error("查询市场排班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/arrangeList";
	}
	/**
	 * 查看排班详情
	 * @param officeId
	 * @param day
	 * @param month
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "arrangeDetails")
	public Map<String, Object> arrangeDetails(String officeId,int day,int month,HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<ArrangeBeautician> listB = arrangeService.findArrangeBeautician(officeId, day, month);
			List<ArrangeBeautician> listE = arrangeService.findArrangeEquipment(officeId, day, month);
			jsonMap.put("STATUS", "OK");
			jsonMap.put("LISTB", listB);
			jsonMap.put("LISTE", listE);
		} catch (Exception e) {
			jsonMap.put("STATUS", "error");
			BugLogUtils.saveBugLog(request, "查看排班详情", e);
			logger.error("查看排班详情错误信息:"+e.getMessage());
		}
		return jsonMap;
	}
	/**
	 * 店铺特殊美容师list
	 * @param officeId
	 * @param officeName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "ArrangeBeautician")
	public String ArrangeBeautician(String officeId,String officeName,String searcTime,Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Date date;
			if(searcTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			    date = sdf.parse(searcTime);  
			}else{
				date = new Date();
			}
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			String str = calendar(date,officeName,"Beautician");
			model.addAttribute("calendarStr", str);
			List<ArrangeBeautician> lists = new ArrayList<ArrangeBeautician>();
		    List<ArrangeBeautician> list = arrangeService.findAllBeautician(officeId,map.get("month"));
		    for (ArrangeBeautician arrangeBeautician : list) {
	    		List<ArrangeShop> l = arrangeService.findBeautician(arrangeBeautician.getUserId(),map.get("month"));
	    		List<ArrangeShop> newL = new ArrayList<ArrangeShop>();
	    		for (int i = 1; i <= map.get("maxDay"); i++) {
	    			int y = 0;
	    			if(l.size() > 0){
	    				for (ArrangeShop arrangeShop : l) {
	    					if(arrangeShop.getDay() == i){
	    						y = 1;
	    						newL.add(arrangeShop);
	    					}
	    				}
	    			}
	    			if(y == 0){
	    				newL.add(new ArrangeShop());
	    			}
	    		}
	    		arrangeBeautician.setArrangeShops(newL);
	    		lists.add(arrangeBeautician);
	    	}
		    model.addAttribute("lists", lists);
		    model.addAttribute("officeId", officeId);
		    model.addAttribute("officeName", officeName);
		    model.addAttribute("searcTime", searcTime);
		    //验证排班不可操作日
		    String num = nextMonth(date);
		    if("true".equals(num)){
		    	model.addAttribute("arrangeMaxDay", map.get("day")+RESERVATIONDATE);
		    }else if("no".equals(num)){
		    	model.addAttribute("arrangeMaxDay", 0);
		    }else{
		    	model.addAttribute("arrangeMaxDay", Integer.parseInt(num));
		    }
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询特殊美容师排班", e);
			logger.error("查询特殊美容师排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/arrangeBeautician";
	}
	/**
	 * 保存特殊美容师排班
	 * @param arrangeBeautician
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(ArrangeBeautician arrangeBeautician,String searcTime,String nowOfficeId,String nowOfficeName,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		try {
			if(searcTime != null){
			    date = sdf.parse(searcTime);  
			}else{
				date = new Date();
			}
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			User currentUser = UserUtils.getUser(); 
			List<ArrangeShop> list = new ArrayList<ArrangeShop>();
			// 美容师id集合
			String idArray[] =arrangeBeautician.getUserids().split(",");
			// 修改排版id集合
			List<String> isUpdateArrange = new ArrayList<String>();
			// 循环美容师id
			for(String id : idArray){
				//循环天数
				for (int i = 1; i <= map.get("maxDay"); i++) {
					ArrangeShop arrangeShop = new ArrangeShop();
					String shopId = request.getParameter(id+i);					// 排班（shopIp、假、休、学）
					String flag = request.getParameter("flag"+id+i);			// 区分（0 店铺排班 1 市场排班）
					String isUpdate = request.getParameter("isUpdate"+id+i);	// 是否有过修改（0 否 1 是）
					String arrangeId = request.getParameter("id"+id+i);			// 排班id（train_arrange_shop 主键id）
					if(shopId != null && !"".equals(shopId) && "1".equals(isUpdate)){	// shopId 不为空 说明有排班 同时 有过修改
						arrangeShop.setBeauticianId(id);
						arrangeShop.setShopId(shopId);
						int d = 1;
						if(i%2 == 0){
							d = i/2;
						}else{
							d = (i/2)+1;
						}
						arrangeShop.setApptDate(sdf.parse(map.get("year")+"-"+map.get("month")+"-"+d));
						arrangeShop.setMonth(map.get("month"));
						arrangeShop.setDay(i);
						arrangeShop.setFlag(flag);
						arrangeShop.setCreateBy(currentUser);
						arrangeShop.setOfficeId(currentUser.getOffice().getId());
						list.add(arrangeShop);
					}
					if("1".equals(isUpdate)){
						if("" != arrangeId || null != arrangeId || "0".equals(arrangeId)){
							isUpdateArrange.add(arrangeId);
						}
					}
				}
			}
			arrangeService.saveBeautician(isUpdateArrange,list);
			addMessage(redirectAttributes, "保存美容师排班成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存特殊美容师排班", e);
			logger.error("保存特殊美容师排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/arrange/ArrangeBeautician?officeId="+nowOfficeId+"&officeName="+nowOfficeName+"&searcTime="+sdf.format(date);
	}
	/**
	 * 特殊设备排班List
	 * @param officeId
	 * @param officeName
	 * @param searcTime
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "ArrangeSpecEquipment")
	public String ArrangeSpecEquipment(String officeId,String officeName,String searcTime,Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Date date;
			if(searcTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			    date = sdf.parse(searcTime);  
			}else{
				date = new Date();
			}
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			String str = calendar(date,officeName,"Beautician");
			model.addAttribute("calendarStr", str);
			
			List<ArrangeBeautician> lists = new ArrayList<ArrangeBeautician>();
		    List<ArrangeBeautician> list = arrangeService.findAllEquipment(officeId,map.get("month"));
		    for (ArrangeBeautician arrangeBeautician : list) {
		    	List<ArrangeEquipment> l = arrangeService.findEquipment(arrangeBeautician.getEquipmentId(),map.get("month"));
		    	List<ArrangeEquipment> newL = new ArrayList<ArrangeEquipment>();
		    	for (int i = 1; i <= map.get("maxDay"); i++) {
		    		int y = 0;
		    		for (ArrangeEquipment arrangeEquipment : l) {
		    			if(arrangeEquipment.getDay() == i){
		    				y = 1;
		    				newL.add(arrangeEquipment);
		    			}
		    		}
		    		if(y == 0){
		    			newL.add(new ArrangeEquipment());
		    		}
				}
		    	arrangeBeautician.setArrangeEquipments(newL);
		    	lists.add(arrangeBeautician);
			}
		    model.addAttribute("lists", lists);
		    model.addAttribute("officeId", officeId);
		    model.addAttribute("officeName", officeName);
		    model.addAttribute("searcTime", searcTime);
		    //验证排班不可操作日
		    String num = nextMonth(date);
		    if("true".equals(num)){
		    	model.addAttribute("arrangeMaxDay", map.get("day")+RESERVATIONDATE);
		    }else if("no".equals(num)){
		    	model.addAttribute("arrangeMaxDay", 0);
		    }else{
		    	model.addAttribute("arrangeMaxDay", Integer.parseInt(num));
		    }
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询特殊设备排班", e);
			logger.error("查询特殊设备排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/arrangeSpecEquipment";
	}
	/**
	 * 保存特殊设备排班
	 * @param arrangeBeautician
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveEquipment")
	public String saveEquipment(ArrangeBeautician arrangeBeautician,String searcTime,String nowOfficeId,String nowOfficeName,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		try {
			if(searcTime != null){
			    date = sdf.parse(searcTime);  
			}else{
				date = new Date();
			}
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			User currentUser = UserUtils.getUser(); 
			List<ArrangeEquipment> list = new ArrayList<ArrangeEquipment>();
			//美容师id集合
			String idArray[] =arrangeBeautician.getEquipmentIds().split(",");
			// 修改排版id集合
			List<String> isUpdateArrange = new ArrayList<String>();
			// 循环设备id
			for(String id : idArray){
				for (int i = 1; i <= map.get("maxDay"); i++) {
					ArrangeEquipment arrangeEquipment = new ArrangeEquipment();
					String shopId = request.getParameter(id+i);					// 排班（shopIp、修）
					String isUpdate = request.getParameter("isUpdate"+id+i);	// 是否有过修改（0 否 1 是）
					String arrangeId = request.getParameter("id"+id+i);			// 排班id（train_arrange_equipment 主键id）
					if(shopId != null && !"".equals(shopId) && "1".equals(isUpdate)){	// shopId 不为空 说明有排班 同时 有过修改
						arrangeEquipment.setEquipmentId(Integer.parseInt(id));
						arrangeEquipment.setShopId(shopId);
						int d = 1;
						if(i%2 == 0){
							d = i/2;
						}else{
							d = (i/2)+1;
						}
						arrangeEquipment.setApptDate(sdf.parse(map.get("year")+"-"+map.get("month")+"-"+d));
						arrangeEquipment.setMonth(map.get("month"));
						arrangeEquipment.setDay(i);
						arrangeEquipment.setCreateBy(currentUser);
						arrangeEquipment.setOfficeId(currentUser.getOffice().getId());
						list.add(arrangeEquipment);
					}
					if("1".equals(isUpdate)){
						if("" != arrangeId || null != arrangeId || "0".equals(arrangeId)){
							isUpdateArrange.add(arrangeId);
						}
					}
				}
			}
			arrangeService.saveEquipment(isUpdateArrange,list);
			addMessage(redirectAttributes, "保存特殊设备排班成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存特殊设备排班", e);
			logger.error("保存特殊设备排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/arrange/ArrangeSpecEquipment?officeId="+nowOfficeId+"&officeName="+nowOfficeName+"&searcTime="+sdf.format(date);
	}
	/**
	 * 普通美容师排班list
	 * @param officeId
	 * @param officeName
	 * @param searcTime
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "ArrangeOrdinary")
	public String ArrangeOrdinary(String officeId,String officeName,String searcTime,Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Date date;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			if("nextMonth".equals(request.getParameter("nextMonth"))){
				//  下一个月
				 Calendar nextDate = Calendar.getInstance();   
				 nextDate.add(Calendar.MONTH,1);//下一个月   
				 nextDate.set(Calendar.DATE, 1);//把日期设置为当月第一天    
				 date = nextDate.getTime();
		         model.addAttribute("nextMonth", "nextMonth");
			}else{
				 date = new Date();
			}
			searcTime = sdf.format(date); 
			
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			String str = calendar(date,officeName,"Beautician");
			model.addAttribute("calendarStr", str);
			
			List<ArrangeBeautician> lists = new ArrayList<ArrangeBeautician>();
		    List<ArrangeBeautician> list = arrangeService.findAllOrdinary(officeId,map.get("month"));
		    for (ArrangeBeautician arrangeBeautician : list) {
	    		List<ArrangeShop> l = arrangeService.findBeautician(arrangeBeautician.getUserId(),map.get("month"));
	    		List<ArrangeShop> newL = new ArrayList<ArrangeShop>();
	    		for (int i = 1; i <= map.get("maxDay"); i++) {
	    			int y = 0;
	    			if(l.size() > 0){
	    				for (ArrangeShop arrangeShop : l) {
	    					if(arrangeShop.getDay() == i){
	    						y = 1;
	    						newL.add(arrangeShop);
	    					}
	    				}
	    			}
	    			if(y == 0){
	    				newL.add(new ArrangeShop());
	    			}
	    		}
	    		arrangeBeautician.setArrangeShops(newL);
	    		lists.add(arrangeBeautician);
	    	}
		    model.addAttribute("lists", lists);
		    model.addAttribute("officeId", officeId);
		    model.addAttribute("officeName", officeName);
		    model.addAttribute("searcTime", searcTime);
		    //验证排班不可操作日
		    String num = nextMonth(date);
		    if("true".equals(num)){
		    	model.addAttribute("arrangeMaxDay", map.get("day")+RESERVATIONDATE);
		    }else if("no".equals(num)){
		    	model.addAttribute("arrangeMaxDay", 0);
		    }else{
		    	model.addAttribute("arrangeMaxDay", Integer.parseInt(num));
		    }
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询普通美容师排班", e);
			logger.error("查询普通美容师排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/arrangeOrdinary";
	}
	/**
	 * 保存普通美容师排班
	 * @param arrangeBeautician
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveOrdinary")
	public String saveOrdinary(ArrangeBeautician arrangeBeautician,String searcTime,String nowOfficeId,String nowOfficeName,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,Model model){
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			if(searcTime != null){
			    date = sdf.parse(searcTime);  
			}else{
				date = new Date();
			}
			Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
			User currentUser = UserUtils.getUser(); 
			List<ArrangeShop> list = new ArrayList<ArrangeShop>();
			//美容师id集合
			String idArray[] =arrangeBeautician.getUserids().split(",");
			// 修改排版id集合
			List<String> isUpdateArrange = new ArrayList<String>();
			// 循环美容师id
			for(String id : idArray){
				for (int i = 1; i <= map.get("maxDay"); i++) {
					ArrangeShop arrangeShop = new ArrangeShop();
					String shopId = request.getParameter(id+i);					// 排班（shopIp、假、休、学）
					String flag = request.getParameter("flag"+id+i);			// 区分（0 店铺排班 1 市场排班）
					String isUpdate = request.getParameter("isUpdate"+id+i);	// 是否有过修改（0 否 1 是）
					String arrangeId = request.getParameter("id"+id+i);			// 排班id（train_arrange_shop 主键id）
					if(shopId != null && !"".equals(shopId) && "1".equals(isUpdate)){	// shopId 不为空 说明有排班 同时 有过修改
						arrangeShop.setBeauticianId(id);
						arrangeShop.setShopId(shopId);
						int d = 1;
						if(i%2 == 0){
							d = i/2;
						}else{
							d = (i/2)+1;
						}
						arrangeShop.setApptDate(sdf.parse(map.get("year")+"-"+map.get("month")+"-"+d));
						arrangeShop.setMonth(map.get("month"));
						arrangeShop.setDay(i);
						arrangeShop.setFlag(flag);
						arrangeShop.setCreateBy(currentUser);
						arrangeShop.setOfficeId(currentUser.getOffice().getId());
						list.add(arrangeShop);
					}
					if("1".equals(isUpdate)){
						if("" != arrangeId || null != arrangeId || "0".equals(arrangeId)){
							isUpdateArrange.add(arrangeId);
						}
					}
				}
			}
			arrangeService.saveBeautician(isUpdateArrange,list);
			addMessage(redirectAttributes, "保存美容师排班成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存普通美容师排班", e);
			logger.error("保存普通美容师排班班错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		if("true".equals(nextMonth(date))){
			return "redirect:" + adminPath + "/train/arrange/ArrangeOrdinary?officeId="+nowOfficeId+"&officeName="+nowOfficeName;
		}else{
			return "redirect:" + adminPath + "/train/arrange/ArrangeOrdinary?officeId="+nowOfficeId+"&officeName="+nowOfficeName+"&nextMonth=nextMonth";
		}
	}
	// 验证排班不可操作日
	public String nextMonth(Date date){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;						//当前月份
		
		Calendar NowCal = Calendar.getInstance();
		int nowMaxDay = NowCal.getActualMaximum(Calendar.DAY_OF_MONTH);		//当前月份天数
		int nowDay = NowCal.get(Calendar.DATE);								//当前时间
		int nowMonth = NowCal.get(Calendar.MONTH) + 1;						//当前月份
		if(month == nowMonth){
			// 当前月 返回true  按正常流程走
			return "true";
		}else{
			// 下一月
			if(nowDay + (RESERVATIONDATE / 2) <= nowMaxDay){
				return "no";
			}else{
				int num = ((nowDay + (RESERVATIONDATE / 2)) - nowMaxDay) * 2;
				return Integer.toString(num);
			}
		}
	}
	//获取月份天数、月份、时间
	public Map<String, Integer> calendarDay(Date date){
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null == date){
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);  							//获取年
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);		//当前月份天数
		int day = cal.get(Calendar.DATE);								//当前时间
		int month = cal.get(Calendar.MONTH) + 1;						//当前月份
		map.put("year", year);
		map.put("maxDay", maxDay*2);
		map.put("day", day*2);
		map.put("month", month);
		return map;
	}
	// 拼接日历table
	public String calendar(Date date,String name,String string){
		if(null == date){
			date = new Date();
		}
		Map<String, Integer> map = calendarDay(date);	//获取月份天数、月份、时间
		StringBuffer str = new StringBuffer();
		str.append("<tr class='freezeTr'>");
		if ("shop".equals(string)){
			str.append("<th style=\"text-align: center;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\">操作</div></th>");
		}
		str.append("<th style=\"text-align: center;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\" title=\""+name+">\""+name+"</div></th>");
		for (int i = 0; i < map.get("maxDay"); i++) {
			if(i == map.get("day")-2){
				str.append("<th style=\"text-align:center;background:#17b593;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\">"+map.get("month")+"/"+(i/2+1)+"/AM</div>");
			}else if(i == map.get("day")-1){
				str.append("<th style=\"text-align: center;background:#17b593;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\">"+map.get("month")+"/"+((i-1)/2+1)+"/PM<div></th>");
			}else if(i%2 == 0){
				str.append("<th style=\"text-align: center;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\">"+map.get("month")+"/"+(i/2+1)+"/AM<div></th>");
			}else{
				str.append("<th style=\"text-align: center;\"><div style=\"width: 100px;max-width: 100px;overflow: hidden;max-height: 17px;\">"+map.get("month")+"/"+((i-1)/2+1)+"/PM<div></th>");
			}
		}
		str.append("</tr>");
		return str.toString();
	}
}
