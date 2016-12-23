package com.training.modules.train.web;

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
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.InvitationUserFZ;
import com.training.modules.train.service.InvitationUserFZService;

/**
 * 邀请明细管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/train/invitation")
public class InvitationUserFZController extends BaseController {

	@Autowired
	private InvitationUserFZService invitationUserFZService;

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
	@RequiresPermissions("train:invitation:view")
	@RequestMapping(value = { "invitationlist", "" })
	public String invitationlist(InvitationUserFZ invitationUserFZ, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		try {
			Page<InvitationUserFZ> page = invitationUserFZService.findInvitationUser(new Page<InvitationUserFZ>(request, response), invitationUserFZ);
			model.addAttribute("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
			BugLogUtils.saveBugLog(request, "妃子校邀请明细页面", e);
			logger.error("妃子校邀请明细页面：" + e.getMessage());
		}
		
		return "modules/train/invitationUserList";
	}

	/**
	 * 受邀人明细查询
	 * @param redEnvelope
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:invitation:view")
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, InvitationUserFZ invitationUserFZ, HttpServletResponse response,
			Model model) {
		try {
			List<InvitationUserFZ> list = invitationUserFZService.findByList(invitationUserFZ);
			model.addAttribute("page", list);
			model.addAttribute("invitationUser", invitationUserFZ);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "妃子校查看被邀请页面", e);
			logger.error("妃子校查看被邀请页面：" + e.getMessage());
		}

		return "modules/train/byInvitationList";
	}

}
