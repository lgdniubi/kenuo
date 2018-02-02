package com.training.modules.crm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.CrmOrders;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.service.OrdersService;

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
}
