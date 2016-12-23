package com.training.modules.ec.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Activity;
import com.training.modules.ec.entity.Courier;
import com.training.modules.ec.service.CourierService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 活动
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/courier")
public class CourierController extends BaseController {

	@Autowired
	private CourierService courierService;

	@ModelAttribute
	public Courier get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return courierService.get(id);
		} else {
			return new Courier();
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
	@RequiresPermissions("ec:courier:view")
	@RequestMapping(value = { "list", "" })
	public String list(Courier courier, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<Courier> page = courierService.findCourier(new Page<Courier>(request, response), courier);
		model.addAttribute("page", page);
		return "modules/ec/CourierList";
	}

	/**
	 * 创建活动，修改
	 * 
	 * @param activity
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:courier:view", "ec:courier:add", "ec:courier:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, Activity activity, Model model) {
		try {
			// List<Franchisee> franList=franchiseeService.findList(new
			// Franchisee());
			if (activity.getId() != null) {
				// activity=get(activity.getId()+"");
			}
			// model.addAttribute("franList",franList);
			model.addAttribute("activity", activity);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("创建活动页面：" + e.getMessage());
		}

		return "modules/ec/createActivityForm";
	}

	/**
	 * 保存
	 * 
	 * @param activity
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Activity activity, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			User user = UserUtils.getUser();
			activity.setCreateBy(user);
			if (activity.getId().length() == 0) {
				activity.setStatus(2);
				// activityService.insertAction(activity);
			} else {
				// activityService.update(activity);
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

}
