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
import com.training.modules.ec.entity.CourierCompany;
import com.training.modules.ec.service.CourierCompanyService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 物流快递
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/courierCompany")
public class CourierCompanyController extends BaseController {

	@Autowired
	private CourierCompanyService courierCompanyService;


	@ModelAttribute
	public CourierCompany get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return courierCompanyService.get(id);
		} else {
			return new CourierCompany();
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
	@RequiresPermissions("ec:courierCompany:view")
	@RequestMapping(value = { "list", "" })
	public String list(CourierCompany courierCompany, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<CourierCompany> page = courierCompanyService.findCourier(new Page<CourierCompany>(request, response),courierCompany);
		model.addAttribute("page", page);
		return "modules/ec/courierCompanyList";
	}

	/**
	 * 创建，修改
	 * 
	 * @param activity
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:courierCompany:view", "ec:courierCompany:add", "ec:courierCompany:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,CourierCompany courierCompany, Model model) {
		try {
			if(courierCompany.getId()!=null){
				courierCompany=get(courierCompany.getId()+"");
			}
			
			model.addAttribute("courierCompany",courierCompany);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "物流页面", e);
			logger.error("物流页面：" + e.getMessage());
		}

		return "modules/ec/createCourComForm";
	}
	
	
	/**
	 * 保存
	 * @param courierCompany
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(CourierCompany courierCompany, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			if(courierCompany.getId()!=null){
				courierCompanyService.insertCour(courierCompany);
			}
			
			addMessage(redirectAttributes, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建物流", e);
			logger.error("方法：save，创建物流：" + e.getMessage());
			addMessage(redirectAttributes, "创建物流失败");
		}

		return "redirect:" + adminPath + "/ec/activity/list";

	}

}
