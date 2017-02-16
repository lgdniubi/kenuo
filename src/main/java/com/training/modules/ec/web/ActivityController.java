package com.training.modules.ec.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Activity;
import com.training.modules.ec.entity.ActivityCoupon;
import com.training.modules.ec.entity.ActivityCouponCategory;
import com.training.modules.ec.entity.ActivityCouponGoods;
import com.training.modules.ec.entity.ActivityCouponUser;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.ActivityService;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 活动
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/activity")
public class ActivityController extends BaseController {

	@Autowired
	private ActivityService activityService;
	@Autowired
	private FranchiseeService franchiseeService;

	@ModelAttribute
	public Activity get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return activityService.get(id);
		} else {
			return new Activity();
		}
	}

	/**
	 * 分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:activity:view")
	@RequestMapping(value = { "list", "" })
	public String list(Activity activity, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<Activity> page = activityService.findActivity(new Page<Activity>(request, response), activity);
		model.addAttribute("page", page);
		return "modules/ec/activityList";
	}

	/**
	 * 创建活动，修改
	 * @param activity
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:activity:view", "ec:activity:add", "ec:activity:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Activity activity, Model model) {
		try {
			List<Franchisee> franList=franchiseeService.findList(new Franchisee());
			if(activity.getId()!=null){
				activity=get(activity.getId()+"");
			}
			model.addAttribute("franList",franList);
			model.addAttribute("activity",activity);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("创建活动页面：" + e.getMessage());
		}

		return "modules/ec/createActivityForm";
	}
	
	/**
	 * 创建活动，修改
	 * 
	 * @param activity
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:activity:view", "ec:activity:add", "ec:activity:edit" }, logical = Logical.OR)
	@RequestMapping(value = "listform")
	public String listform(HttpServletRequest request, Activity activity, Model model) {
		try {
			
			if(activity.getId()!=null){
				List<Franchisee> franList=franchiseeService.findList(new Franchisee());
				activity=get(activity.getId()+"");
				List<ActivityCoupon> list=activityService.Couponlist(activity.getId());
				model.addAttribute("list",list);
				model.addAttribute("franList",franList);
				model.addAttribute("activity",activity);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("创建活动页面：" + e.getMessage());
		}

		return "modules/ec/ActivityListForm";
	}
	
	
	/**
	 * 保存数据
	 * 
	 * @param activity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:activity:add" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Activity activity, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			User user=UserUtils.getUser();
			activity.setCreateBy(user);
			if(activity.getId().length()==0){
				activity.setStatus(2);
				activityService.insertAction(activity);
			}else{
				activityService.update(activity);
			}
			addMessage(redirectAttributes, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("方法：save，创建活动：" + e.getMessage());
			addMessage(redirectAttributes, "创建活动失败");
		}

		return "redirect:" + adminPath + "/ec/activity/list";

	}
	
	
	
	
	/**
	 * 红包列表
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:activity:view", "ec:activity:add", "ec:activity:edit" }, logical = Logical.OR)
	@RequestMapping(value = "Couponlist")
	public String addActionGoodsList(HttpServletRequest request,Activity activity, Model model) {
		try {
			List<ActivityCoupon> list=activityService.Couponlist(activity.getId());
			model.addAttribute("list",list);
			model.addAttribute("activity", activity);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "抢购商品列表", e);
			logger.error("抢购商品列表：" + e.getMessage());
		}

		return "modules/ec/addCouponList";
	}

	
	/**
	 * 添加红包
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addCouponForm")
	public String addCouponForm(HttpServletRequest request,ActivityCoupon activityCoupon, Model model) {
		try {
			if(!activityCoupon.getId().equals("0")){
				activityCoupon=activityService.findByCouponId(activityCoupon.getId());
				if (activityCoupon.getUsedType() == 2) {
					List<ActivityCouponCategory> list=activityService.couponCateList(Integer.parseInt(activityCoupon.getId()));
					activityCoupon.setCatelist(list);
			} else if (activityCoupon.getUsedType() == 3) {
				List<ActivityCouponGoods> list = activityService.CouponGoodslist(Integer.parseInt(activityCoupon.getId()));
				activityCoupon.setList(list);
			}
			}
			
			model.addAttribute("activityCoupon", activityCoupon);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加红包", e);
			logger.error("添加红包页面：" + e.getMessage());
		}

		return "modules/ec/addCouponForm";
	}
	
	/**
	 * 商品红包分类列表
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "CateGoodsList")
	public String CateGoodsList(HttpServletRequest request,ActivityCoupon activityCoupon, Model model) {
		try {
			if(!activityCoupon.getId().equals("0")){
				activityCoupon=activityService.findByCouponId(activityCoupon.getId());
				if (activityCoupon.getUsedType() == 2) {
					List<ActivityCouponCategory> list=activityService.couponCateList(Integer.parseInt(activityCoupon.getId()));
					activityCoupon.setCatelist(list);
			} else if (activityCoupon.getUsedType() == 3) {
				List<ActivityCouponGoods> list = activityService.CouponGoodslist(Integer.parseInt(activityCoupon.getId()));
				activityCoupon.setList(list);
			}
			}
			
			model.addAttribute("activityCoupon", activityCoupon);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加红包", e);
			logger.error("添加红包页面：" + e.getMessage());
		}

		return "modules/ec/couponCateGoodsList";
	}
	
	
	/**
	 * 保存添加的红包
	 * @param actionInfo
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveCoupon")
	public String saveCoupon(ActivityCoupon activityCoupon, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {			
		try {
			
			activityService.saveCouponOrUpdate(activityCoupon);
			
			} catch (Exception e) {
				BugLogUtils.saveBugLog(request, "保存红包", e);
				logger.error("方法：save，保存红包：" + e.getMessage());
				
				return "error";
			}
		

		return "success";

	}
	
	/**
	 * 更改活动状态 开启 关闭
	 * @param activity
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	
	@RequestMapping(value = "actionStatus")
	public String actionStatus(Activity activity, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			
			activityService.updateStatus(activity);
			
			addMessage(redirectAttributes, "操作成功！");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "活动开启关闭", e);
			logger.error("方法：save，创建活动：" + e.getMessage());
			addMessage(redirectAttributes, "活动开启关闭");
		}

		return "redirect:" + adminPath + "/ec/activity/list";

	}
	
	
	
	
	/**
	 * 更改活动状态 开启 关闭
	 * @param activity
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateCouponStatus")
	public String updateCouponStatus(ActivityCoupon activityCoupon, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			
			activityService.updateCouponStatus(activityCoupon);
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "活动红包开启关闭", e);
			logger.error("方法：save，创建活动：" + e.getMessage());
			return "error";
			
		}

		return "success";

	}
	
	
	
	
	/**
	 * 红包是否有人领取
	 * @param id
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "numByCouponId")
	public int numByCouponId(String id, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		int num=0;
		try {
			
			num=activityService.numByCouponId(Integer.parseInt(id));
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "红包是否领取", e);
			logger.error("红包是否领取：" + e.getMessage());
			
		}
		return num;

	}
	
	
	
	/**
	 * 红包推送页面
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "sendform")
	public String sendform(HttpServletRequest request,Activity activity, Model model) {
		try {
			
			model.addAttribute("activity", activity);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "红包推送页面", e);
			logger.error("红包推送页面：" + e.getMessage());
		}

		return "modules/ec/couponSendUsersFrom";
	}
	
	/**
	 * 查询活动下的红包
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,String id,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		List<ActivityCoupon> list = activityService.findByCouIdList(Integer.parseInt(id));
		for (int i=0; i<list.size(); i++){
			ActivityCoupon e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("name", e.getCouponName());
			mapList.add(map);
		}
		return mapList;
	}
	
	
	/**
	 * 推送红包
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "saveSendCoupon")
	public String saveSendCoupon(Activity activity, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			StringBuilder failureMsg = new StringBuilder();
			int sueess=0;
			int fail=0;
				if(activity.getSendType()==1){
					if(activity.getCouponId().length()>0){
						int num=activityService.findByCouponNum(activity.getCouponId());
						if(num>0){
							failureMsg.insert(0, "已经有人拥有红包，不能发送！");
							addMessage(redirectAttributes,"失败："+failureMsg);
						}else{
							String idarry[]=activity.getCouponId().split(",");
							for (int i = 0; i < idarry.length; i++) {
								ActivityCouponUser activityCouponUser=new ActivityCouponUser();
								activityCouponUser.setCouponId(idarry[i].toString());
								sueess=activityService.insertSend(activityCouponUser);
							}
							addMessage(redirectAttributes, "发送红包成功");
						}
						
					}
				}
				if(activity.getSendType()==2){
					
					if(activity.getUserId().length()>0 && activity.getCouponId().length()>0){
						String[] idarry = activity.getUserId().split(",");
						String[] idAmount=activity.getCouponId().split(",");
						for (int i = 0; i < idarry.length; i++) {
							for (int j = 0; j < idAmount.length; j++) {
								ActivityCouponUser select=new ActivityCouponUser();
								select.setCouponId(idAmount[j].toString());
								select.setUserId(idarry[i]);
								int num=activityService.findByAIdandUserId(select);
								if(num>0){
									fail++;
								}else{
									ActivityCouponUser couponUser=new ActivityCouponUser();
									couponUser.setUserId(idarry[i].toString());
									couponUser.setCouponId(idAmount[j].toString());
									couponUser.setStatus("0");
									activityService.insertUserCoupon(couponUser);
									sueess++;
								}
								
							}
							
						}
						
						
					}
					addMessage(redirectAttributes, "已成功发送："+sueess+"个红包，已经有用户拥有："+fail+"个红包");
				}
				if (activity.getSendType()==3) {
					if(activity.getCouponId().length()>0){
						int num=activityService.findFeiByCouponNum(activity.getCouponId());
						if(num>0){
							failureMsg.insert(0, "已经有人拥有红包，不能发送！");
							addMessage(redirectAttributes, "失败："+failureMsg);
						}else{
							String idarry[]=activity.getCouponId().split(",");
							for (int i = 0; i < idarry.length; i++) {
								ActivityCouponUser couponUser=new ActivityCouponUser();
								couponUser.setCouponId(idarry[i].toString());
								sueess=activityService.insertFeiSend(couponUser);
							}
							addMessage(redirectAttributes, "发送红包成功");
						}
						
					}
				}
				if(activity.getSendType()==4){
					if(activity.getMobileNum().length()>0 && activity.getCouponId().length()>0){
						String moblie=activity.getMobileNum().replace("，", ",");
						String[] mobile = moblie.split(",");
						String[] couponId=activity.getCouponId().split(",");
						for (int i = 0; i < mobile.length; i++) {
							Users users=activityService.findByMobile(mobile[i]);
							if(users!=null){
								for (int j = 0; j < couponId.length; j++) {
									ActivityCouponUser select=new ActivityCouponUser();
									select.setCouponId(couponId[j].toString());
									select.setUserId(users.getUserid()+"");
									int num=activityService.findByAIdandUserId(select);
									if(num>0){
										failureMsg.append("<br/>手机号："+mobile[i]+"的用户已发放。");
										fail++;
									}else{
										ActivityCouponUser couponUser=new ActivityCouponUser();
										couponUser.setUserId(users.getUserid()+"");
										couponUser.setCouponId(couponId[j].toString());
										couponUser.setStatus("0");
										activityService.insertUserCoupon(couponUser);
										sueess++;
										
									}
								}
								
								
							}else{
								failureMsg.append("<br/>手机号码："+mobile[i]+" 不存在");
								fail++;
							}
						}
					}

					if (fail > 0) {
						failureMsg.insert(0, "，失败 " + fail + "个，如下信息：");
					}
					addMessage(redirectAttributes, "成功发放红包："+sueess+"个， "+failureMsg);
				}
				
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发送红包", e);
			logger.error("方法：save，发送红包：" + e.getMessage());
			addMessage(redirectAttributes, "发送红包失败");
		}

		return "redirect:" + adminPath + "/ec/activity/list?";

	}
	
	
	
	/**
	 * 优惠卷明细分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:activity:view")
	@RequestMapping(value = { "couponUserlist", "" })
	public String couponlist(ActivityCouponUser activityCouponUser, HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<ActivityCouponUser> page = activityService.findCouponUser(new Page<ActivityCouponUser>(request, response), activityCouponUser);
		model.addAttribute("activityCouponUser", activityCouponUser);
		model.addAttribute("page", page);
		return "modules/ec/couponUserList";
	}
	
	/**
	 * 导出数据
	 * @param activityCouponUser
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(ActivityCouponUser activityCouponUser, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "红包明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			List<ActivityCouponUser> list=activityService.exportCouponUser(activityCouponUser);
			System.out.println(list.size());
			new ExportExcel("红包明细", ActivityCouponUser.class).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "导出红包明细", e);
			addMessage(redirectAttributes, "导出红包明细失败！");
		}
		return "redirect:" + adminPath + "/ec/activity/couponUserlist";
	}
	

}
