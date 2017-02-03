package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.InvitationUser;
import com.training.modules.ec.service.InvitationUserService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 邀请明细管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/invitation")
public class InvitationUserController extends BaseController {

	@Autowired
	private InvitationUserService invitationUserService;

	// @ModelAttribute
	// public Coupon get(@RequestParam(required = false) String id) {
	// if (StringUtils.isNotBlank(id)) {
	// return couponService.get(id);
	// } else {
	// return new CouponUser();
	// }
	// }

	/**
	 * 明细分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:invitation:view")
	@RequestMapping(value = { "invitationlist", "" })
	public String invitationlist(InvitationUser invitationUser, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		try {
			Page<InvitationUser> page = invitationUserService
					.findInvitationUser(new Page<InvitationUser>(request, response), invitationUser);
			model.addAttribute("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "邀请明细页面", e);
			logger.error("邀请明细页面：" + e.getMessage());
		}
		
		return "modules/ec/invitationUserList";
	}

	/**
	 * 受邀人明细查询
	 * 
	 * @param redEnvelope
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:invitation:view")
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, InvitationUser invitationUser, HttpServletResponse response,
			Model model) {
		try {
			List<InvitationUser> list = invitationUserService.findByList(invitationUser);
			model.addAttribute("page", list);
			model.addAttribute("invitationUser", invitationUser);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "查看被邀请页面", e);
			logger.error("查看被邀请页面：" + e.getMessage());
		}

		return "modules/ec/byInvitationList";
	}

}
