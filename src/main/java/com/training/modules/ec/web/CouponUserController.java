package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.CouponUser;
import com.training.modules.ec.service.CouponService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 红包管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/couponUser")
public class CouponUserController extends BaseController {

	@Autowired
	private CouponService couponService;

	// @ModelAttribute
	// public Coupon get(@RequestParam(required = false) String id) {
	// if (StringUtils.isNotBlank(id)) {
	// return couponService.get(id);
	// } else {
	// return new CouponUser();
	// }
	// }

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
	public String couponlist(CouponUser couponUser, HttpServletRequest request, HttpServletResponse response,Model model) {
		try {
			Page<CouponUser> page = couponService.findCouponUser(new Page<CouponUser>(request, response), couponUser);
			model.addAttribute("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "优惠卷列表", e);
			logger.error("优惠卷列表：" + e.getMessage());
		}
		
		return "modules/ec/couponUserList";
	}

}
