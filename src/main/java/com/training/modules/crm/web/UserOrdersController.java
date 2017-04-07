package com.training.modules.crm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.CrmOrders;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Payment;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.service.PaymentService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * kenuo 订单记录
 * @author：sharp 
 * @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/orders")
public class UserOrdersController extends BaseController {
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private PaymentService paymentService;
	/**
	 * 默认返回用户信息
	 * @param 
	 * @return UserDetail
	 */
	@ModelAttribute
	public UserDetail  get(@RequestParam(required = false) String userId) {
		if (StringUtils.isNotBlank(userId)) {
			UserDetail detail= userDetailService.getUserNickname(userId); 
		    return detail;
		} else {
			return new UserDetail();
		}
	}
	/**
	 * @param
	 * @return String 客户订单列表
	 */
	@RequestMapping(value = "list")
	public String orders(String userId, CrmOrders orders, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		if (null!=userId && userId.trim().length()>0) {
			orders.setUserid(Integer.valueOf(userId));
			Page<CrmOrders> page = ordersService.findByUser(new Page<CrmOrders>(request, response), orders);
			model.addAttribute("page", page);
			model.addAttribute("userId", userId);
			model.addAttribute("orders",orders);
		}
		return "modules/crm/orders";
	}

	/**
	 * 跳转虚拟添加订单页
	 * 
	 * @param orders
	 * @param model
	 * @return
	 */

//	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createOrder")
	public String createOrder(String userId,Orders orders,HttpServletRequest request,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);
            model.addAttribute("userId", userId);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建虚拟订单页面", e);
			logger.error("方法：createOrder，跳转创建虚拟订单页面出错：" + e.getMessage());
		}

		return "modules/crm/createOrderForm";
	}

	/**
	 * 添加虚拟订单
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */

//	@RequiresPermissions(value = { "ec:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveVirtualOrder")
	public String saveVirtualOrder(String userId,Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveVirtualOrder(orders);
			addMessage(redirectAttributes, "创建订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建订单", e);
			logger.error("方法：saveOrder，创建新订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建订单失败！");
		}

		return "redirect:" + adminPath + "/crm/orders/list?userId="+userId;

	}
	
	/**
	 * 跳转实物订单创建页
	 * @param request
	 * @param orders
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value = { "crm:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "createKindOrder")
	public String createKindOrder(String userId,Orders orders,HttpServletRequest request,Model model){
		try {

			List<Payment> paylist = paymentService.paylist();
			List<GoodsCategory> cateList = ordersService.cateList();
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("cateList", cateList);
            model.addAttribute("userId", userId);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "跳转创建实物订单页面", e);
			logger.error("方法：createOrder，跳转创建实物订单页面出错：" + e.getMessage());
		}
		return "modules/crm/createKindOrder";
	}
	
	/**
	 * 添加实物订单
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
//	@RequiresPermissions(value = { "crm:orders:add" }, logical = Logical.OR)
	@RequestMapping(value = "saveKindOrder")
	public String saveKindOrder(String userId,Orders orders, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ordersService.saveKindOrder(orders);
			addMessage(redirectAttributes, "创建实物订单'" + orders.getOrderid() + "'成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "创建实物订单", e);
			logger.error("方法：saveOrder，创建实物订单出现错误：" + e.getMessage());
			addMessage(redirectAttributes, "创建实物订单失败！");
		}

		return "redirect:" + adminPath + "/crm/orders/list?userId="+userId;
	}
	
	/**
	 * 返回订单详情
	 * @param orders
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "orderform")
	public String orderform(HttpServletRequest request, Orders orders,String type, Model model) {
		try {
			User user = UserUtils.getUser(); //登陆用户
			List<Payment> paylist = paymentService.paylist();
			orders = ordersService.selectOrderById(orders.getOrderid());
			model.addAttribute("orders", orders);
			model.addAttribute("paylist", paylist);
			model.addAttribute("user", user);
			model.addAttribute("type", type);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "订单跳转修改页面", e);
			logger.error("跳转修改页面出错：" + e.getMessage());
		}
		return "modules/crm/ordersForm";
	}
	
}
