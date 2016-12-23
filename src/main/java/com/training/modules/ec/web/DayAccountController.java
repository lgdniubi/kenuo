package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.DayAccount;
import com.training.modules.ec.service.DayAccountService;

/**
 * 日结算表
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/dayaccount")
public class DayAccountController extends BaseController {

	@Autowired
	private DayAccountService dayAccountService;

	@ModelAttribute
	public DayAccount get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dayAccountService.get(id);
		} else {
			return new DayAccount();
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
	@RequiresPermissions("ec:dayaccount:view")
	@RequestMapping(value = { "list", "" })
	public String list(DayAccount dayAccount, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<DayAccount> page = dayAccountService.findDayAccount(new Page<DayAccount>(request, response), dayAccount);
		model.addAttribute("page", page);
		return "modules/ec/dayAccountList";
	}

	

}
