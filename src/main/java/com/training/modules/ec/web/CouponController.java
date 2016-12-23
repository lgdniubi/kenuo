package com.training.modules.ec.web;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Coupon;
import com.training.modules.ec.entity.CouponAcmountMapping;
import com.training.modules.ec.entity.CouponCategoryMapping;
import com.training.modules.ec.entity.CouponGoodsMapping;
import com.training.modules.ec.entity.CouponUser;
import com.training.modules.ec.service.CouponService;
import com.training.modules.quartz.tasks.Gc;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 红包管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/coupon")
public class CouponController extends BaseController {

	@Autowired
	private CouponService couponService;
	@Autowired
	private FranchiseeService franchiseeService;
	@Autowired
	private Gc gc;

	// private static int UpdateStatus=0;

	@ModelAttribute
	public Coupon get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return couponService.get(id);
		} else {
			return new Coupon();
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
	@RequiresPermissions("ec:coupon:view")
	@RequestMapping(value = { "list", "" })
	public String list(Coupon coupon, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 查询更新已经过期的数据
		List<Coupon> couponlist=couponService.selectByTime();
		for (int i = 0; i < couponlist.size(); i++) {
			List<CouponAcmountMapping> list=couponService.acmountList(couponlist.get(i).getCouponId());
			for (int j = 0; j < list.size(); j++) {
				gc.gc(list.get(i).getAmountId());
			}
			int num =couponService.updateOldStatus(couponlist.get(i).getCouponId());
			System.err.println("更新过期数据" + num);
		}
		
		Page<Coupon> page = couponService.findCoupon(new Page<Coupon>(request, response), coupon);
		model.addAttribute("page", page);
		return "modules/ec/couponList";
	}

	/**
	 * 红包页面创建，修改
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:coupon:view", "ec:coupon:add", "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Coupon coupon, Model model) {
		try {
			List<Franchisee> franList=franchiseeService.findList(new Franchisee());
			if (coupon.getCouponId() != 0) {
				coupon = couponService.get(coupon.getCouponId() + "");
				if (coupon.getGoodsUseType() == 2) {
						List<CouponCategoryMapping> list=couponService.couponCateList(coupon.getCouponId());
						coupon.setCatelist(list);
				} else if (coupon.getGoodsUseType() == 3) {
					List<CouponGoodsMapping> list = couponService.CouponGoodslist(coupon.getCouponId());
					coupon.setList(list);
				}
			}
			model.addAttribute("franList",franList);
			model.addAttribute("coupon", coupon);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建红包页面", e);
			logger.error("创建红包页面：" + e.getMessage());
		}

		return "modules/ec/createCouponForm";
	}
	
	/**
	 * 面值列表
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:coupon:view", "ec:coupon:add", "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "addCoupon")
	public String addCoupon(HttpServletRequest request, CouponAcmountMapping couponAcmountMapping, Model model) {
		try {
			Date newDate=new Date();
			//红包下所有面值
			List<CouponAcmountMapping> list=couponService.acmountList(couponAcmountMapping.getCouponId());
			Coupon coupon = couponService.get(couponAcmountMapping.getCouponId()+"");
			//日期比较
			int i=newDate.compareTo(coupon.getGetBegintime());
			couponAcmountMapping.setDateCompare(i);
			model.addAttribute("list",list);
			model.addAttribute("couponAcmountMapping", couponAcmountMapping);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "红包列表", e);
			logger.error("红包列表：" + e.getMessage());
		}

		return "modules/ec/addCouponAcmountList";
	}
	
	/**
	 * 查看面值列表
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:coupon:view", "ec:coupon:add", "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "couponAmountList")
	public String couponAmountList(HttpServletRequest request, CouponAcmountMapping couponAcmountMapping, Model model) {
		try {

			//红包下所有面值
			List<CouponAcmountMapping> list=couponService.acmountList(couponAcmountMapping.getCouponId());
			model.addAttribute("list",list);
			model.addAttribute("couponAcmountMapping", couponAcmountMapping);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "红包列表", e);
			logger.error("红包列表：" + e.getMessage());
		}

		return "modules/ec/CouponAcmountList";
	}
	
	
	
	/**
	 * 添加红包
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addCouponForm")
	public String addCouponForm(HttpServletRequest request,CouponAcmountMapping couponAcmountMapping , Model model) {
		try {
			if(couponAcmountMapping.getAmountId()!=0){
				couponAcmountMapping=couponService.findByAcomountId(couponAcmountMapping.getAmountId());
			}
			
			model.addAttribute("couponAcmountMapping", couponAcmountMapping);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加红包", e);
			logger.error("添加红包页面：" + e.getMessage());
		}

		return "modules/ec/addCouponAcmountForm";
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
	@RequestMapping(value = "saveAmount")
	public String saveAmount(CouponAcmountMapping couponAcmountMapping, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
		
			if(couponAcmountMapping.getAmountId()==0){
				couponAcmountMapping.setCreateBy(user);
				couponAcmountMapping.setStatus(1);
				couponAcmountMapping.setCouponNumber(couponAcmountMapping.getTotalNumber());
				couponService.saveAcmount(couponAcmountMapping);
				
			}else{
				couponAcmountMapping.setUpdateBy(user);
				couponAcmountMapping.setCouponNumber(couponAcmountMapping.getTotalNumber());
				couponService.updateAcmount(couponAcmountMapping);
			}
			
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加活动商品", e);
			logger.error("方法：save，添加活动商品：" + e.getMessage());
			return "error";
		}

		return "success";

	}
	
	
	/**
	 * 更新状态
	 * @param actionInfo
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateStatus")
	public String updateStatus(CouponAcmountMapping couponAcmountMapping, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
			couponAcmountMapping.setUpdateBy(user);
			couponService.updateStatus(couponAcmountMapping);
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "添加活动商品", e);
			logger.error("方法：save，添加活动商品：" + e.getMessage());
			return "error";
		}

		return "success";

	}
	
	
	/**
	 * 红包推送页面
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:coupon:view", "ec:coupon:add", "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "sendform")
	public String sendform(HttpServletRequest request, Coupon coupon, Model model) {
		try {
			
			model.addAttribute("coupon", coupon);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "红包推送页面", e);
			logger.error("红包推送页面：" + e.getMessage());
		}

		return "modules/ec/couponSendUsersFrom";
	}
	
	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "saveSendCoupon")
	public String saveSendCoupon(Coupon coupon, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
				if(coupon.getSendType()==1){
					if(coupon.getAmountId().length()>0){
						int num=couponService.findByAmountidNum(coupon.getAmountId());
						if(num>0){
							addMessage(redirectAttributes, "已经有人拥有红包，不能发送！");
						}else{
							String idarry[]=coupon.getAmountId().split(",");
							for (int i = 0; i < idarry.length; i++) {
								CouponUser couponUser=new CouponUser();
								couponUser.setCouponId(coupon.getCouponId()+"");
								couponUser.setAmountId(idarry[i].toString());
								couponService.insertSend(couponUser);
							}
							addMessage(redirectAttributes, "发送红包成功");
						}
						
					}
				}
				if(coupon.getSendType()==2){
					int sueess=0;
					int fail=0;
					if(coupon.getUserId().length()>0 && coupon.getAmountId().length()>0){
						String[] idarry = coupon.getUserId().split(",");
						String[] idAmount=coupon.getAmountId().split(",");
						for (int i = 0; i < idarry.length; i++) {
							for (int j = 0; j < idAmount.length; j++) {
								CouponUser select=new CouponUser();
								select.setAmountId(idAmount[j].toString());
								select.setUserId(idarry[i]);
								int num=couponService.findByAIdandUserId(select);
								if(num>0){
									fail++;
								}else{
									CouponUser couponUser=new CouponUser();
									couponUser.setUserId(idarry[i].toString());
									couponUser.setAmountId(idAmount[j].toString());
									couponUser.setCouponId(coupon.getCouponId()+"");
									couponUser.setStatus("0");
									couponService.insertUserCoupon(couponUser);
									sueess++;
								}
								
							}
							
						}
						
						
					}
					addMessage(redirectAttributes, "已成功发送："+sueess+"个红包，已经有用户拥有："+fail+"个红包");
				}
				if (coupon.getSendType()==3) {
					if(coupon.getAmountId().length()>0){
						int num=couponService.findFeiByAmountidNum(coupon.getAmountId());
						if(num>0){
							addMessage(redirectAttributes, "已经有人拥有红包，不能发送！");
						}else{
							String idarry[]=coupon.getAmountId().split(",");
							for (int i = 0; i < idarry.length; i++) {
								CouponUser couponUser=new CouponUser();
								couponUser.setCouponId(coupon.getCouponId()+"");
								couponUser.setAmountId(idarry[i].toString());
								couponService.insertFeiSend(couponUser);
							}
							addMessage(redirectAttributes, "发送红包成功");
						}
						
					}
				}
				
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发送红包", e);
			logger.error("方法：save，发送红包：" + e.getMessage());
			addMessage(redirectAttributes, "发送红包失败");
		}

		return "redirect:" + adminPath + "/ec/coupon/list";

	}
	

	/**
	 * 改变优惠卷状态，开启，关闭
	 * 
	 * @param redEnvelope
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions("ec:coupon:edit")
	@RequestMapping(value = "couponStatus")
	public String couponStatus(HttpServletRequest request, Coupon coupon, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
			coupon.setUpdateBy(user);
			coupon.setStatus(1);
			couponService.updateStatus(coupon);
			addMessage(redirectAttributes, "关闭红包成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "关闭红包", e);
			addMessage(redirectAttributes, "关闭红包失败！");
			logger.error("关闭红包：" + e.getMessage());
		}

		return "redirect:" + adminPath + "/ec/coupon/list";
	}

	/**
	 * 开启设置领取时间
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

	@RequiresPermissions(value = { "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "timeform")
	public String timeform(HttpServletRequest request, Coupon coupon, Model model) {
		try {
			coupon = couponService.get(coupon.getCouponId() + "");
			model.addAttribute("coupon", coupon);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "红包页面", e);
			logger.error("开启红包页面：" + e.getMessage());
		}

		return "modules/ec/couponOpenTime";
	}

	/**
	 * 开启优惠卷改变时间，和状态
	 * 
	 * @param redEnvelope
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions("ec:coupon:edit")
	@RequestMapping(value = "updateTime")
	public String updateTime(HttpServletRequest request, Coupon coupon, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
			coupon.setUpdateBy(user);
			coupon.setStatus(0);
			//coupon.setTotalNumber(coupon.getCouponNumber());
			couponService.updateTime(coupon);
			addMessage(redirectAttributes, "开启红包成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "开启红包", e);
			addMessage(redirectAttributes, "开启红包失败！");
			logger.error("开启红包：" + e.getMessage());
		}

		return "redirect:" + adminPath + "/ec/coupon/list";
	}

	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions(value = { "ec:coupon:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Coupon coupon, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			List<CouponGoodsMapping> goodslist = new ArrayList<CouponGoodsMapping>();
			List<CouponCategoryMapping> cateList=new ArrayList<CouponCategoryMapping>();
			User user = UserUtils.getUser();
			coupon.setCreateBy(user);
			coupon.setStatus(1);
			//增加新红包
			if (coupon.getCouponId() == 0) {
				if(coupon.getCouponType().equals("4")){
					coupon.setGoodsUseType(3);
				}
				couponService.saveCoupon(coupon);
				
				if (coupon.getGoodsUseType() == 2) {			//指定分类
					cateList = CouponCategoryTolist(coupon);
					if (cateList.size() > 0) {
						couponService.insertCouponCategoryMapping(cateList);
					}

				}
				if (coupon.getGoodsUseType() == 3) {		//指定商品
					goodslist = CouponTolist(coupon);
					if (goodslist.size() > 0) {
						couponService.insertCouponMapping(goodslist);
					}

				}
			} else {
				//修改红包信息
				if(coupon.getCouponType().equals("4")){
					coupon.setGoodsUseType(3);
				}
				if(coupon.getCouponType().equals("3")){
					coupon.setGoodsUseType(1);
				}
				couponService.update(coupon);
				if(coupon.getGoodsUseType() == 2){
					couponService.deleteCoupCate(coupon.getCouponId());
					cateList = CouponCategoryTolist(coupon);
					if (cateList.size() > 0) {
						couponService.insertCouponCategoryMapping(cateList);
					}
				}
				
				if (coupon.getGoodsUseType() == 3) {
					couponService.deleteCoupGood(coupon.getCouponId());
					goodslist = CouponTolist(coupon);
					if (goodslist.size() > 0) {
						couponService.insertCouponMapping(goodslist);
					}

				}
			}

			addMessage(redirectAttributes, "保存红包成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存红包", e);
			logger.error("方法：save，保存红包：" + e.getMessage());
			addMessage(redirectAttributes, "保存红包失败");
		}

		return "redirect:" + adminPath + "/ec/coupon/list";

	}

	/**
	 * 优惠卷数组拼接
	 * 
	 * @param user
	 * @return
	 */
	public List<CouponGoodsMapping> CouponTolist(Coupon coupon) {
		List<CouponGoodsMapping> list = new ArrayList<CouponGoodsMapping>();
			if (coupon.getGoodsId().length() > 0) {
				String goodid = coupon.getGoodsId();
				String goodname = coupon.getGoodsName();
				String[] idarry = goodid.split(",");
				String[] namearry = goodname.split(",");
				for (int i = 0; i < idarry.length; i++) {
					CouponGoodsMapping couponegoods = new CouponGoodsMapping();
					couponegoods.setCouponId(coupon.getCouponId());
					couponegoods.setGoodsId(Integer.parseInt(idarry[i]));
					couponegoods.setGoodName(namearry[i]);
					list.add(couponegoods);
				}

			}
		

		return list;
	}
	/**
	 * 商品分类拼接
	 * @param coupon
	 * @return
	 */
	public List<CouponCategoryMapping> CouponCategoryTolist(Coupon coupon) {
		List<CouponCategoryMapping> list = new ArrayList<CouponCategoryMapping>();
			if(coupon.getCateId().length()>0){
				String[] idarry = coupon.getCateId().split(",");
				String[] namearry = coupon.getCateName().split(",");
				for (int i = 0; i < idarry.length; i++) {
					CouponCategoryMapping couponCategory = new CouponCategoryMapping();
					couponCategory.setCouponId(coupon.getCouponId());
					couponCategory.setCategoryId(Integer.parseInt(idarry[i].toString()));
					couponCategory.setCategoryName(namearry[i].toString());
					
					list.add(couponCategory);
				}
		}
		

		return list;
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
	@RequiresPermissions("ec:coupon:view")
	@RequestMapping(value = { "couponlist", "" })
	public String couponlist(CouponUser couponUser, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<CouponUser> page = couponService.findCouponUser(new Page<CouponUser>(request, response), couponUser);
		model.addAttribute("couponUser", couponUser);
		model.addAttribute("page", page);
		return "modules/ec/couponUserList";
	}
	
	
	/**
	 * 查询分类下的商品
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,String couponId,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		List<CouponAcmountMapping> list = couponService.findByCouIdList(Integer.parseInt(couponId));
		for (int i=0; i<list.size(); i++){
			CouponAcmountMapping e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getAmountId());
			map.put("name", e.getAmountName());
			mapList.add(map);
		}
		return mapList;
	}

}
